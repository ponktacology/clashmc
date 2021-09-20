package me.ponktacology.clashmc.crate;

import lombok.Getter;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.adapter.CrateParameterAdapter;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.command.CrateCommand;
import me.ponktacology.clashmc.crate.crate.factory.CrateFactory;
import me.ponktacology.clashmc.crate.crate.listener.PlayerInteractListener;
import me.ponktacology.clashmc.crate.crate.opening.announcer.CrateOpeningAnnouncer;
import me.ponktacology.clashmc.crate.crate.opening.announcer.packet.PacketCrateOpeningAnnounce;
import me.ponktacology.clashmc.crate.crate.updater.CrateUpdater;
import me.ponktacology.clashmc.crate.crate.updater.PlayerCrateUpdater;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketAllPlayersCrateUpdate;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketCrateRemove;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketCrateUpdate;
import me.ponktacology.clashmc.crate.crate.updater.packet.PacketPlayerCrateUpdate;
import me.ponktacology.clashmc.crate.player.CratePlayer;
import me.ponktacology.clashmc.crate.player.adapter.CratePlayerParameterAdapter;
import me.ponktacology.clashmc.crate.player.cache.CratePlayerCache;
import me.ponktacology.clashmc.crate.player.factory.CratePlayerFactory;
import me.ponktacology.clashmc.crate.player.listener.PlayerDataListener;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum CratePlugin implements BukkitPlugin {
  INSTANCE;

  private Blade blade;
  private final CratePlayerFactory playerFactory = new CratePlayerFactory();
  private final CratePlayerCache playerCache = new CratePlayerCache(this.playerFactory);
  private final CrateFactory crateFactory = new CrateFactory(CorePlugin.INSTANCE.getDataService());
  private final CrateCache crateCache = new CrateCache();
  private final CrateUpdater crateUpdater =
      new CrateUpdater(CorePlugin.INSTANCE.getNetworkService(), this.crateFactory);
  private final CrateOpeningAnnouncer openingAnnouncer =
      new CrateOpeningAnnouncer(
          CorePlugin.INSTANCE.getNetworkService(), GuildPlugin.INSTANCE.getPlayerCache());
  private final PlayerCrateUpdater playerCrateUpdater =
      new PlayerCrateUpdater(CorePlugin.INSTANCE.getNetworkService(), this.crateCache);

  @Override
  public void enable(JavaPlugin plugin) {
    PluginUtil.registerListener(
        new PlayerDataListener(
            CorePlugin.INSTANCE.getTaskDispatcher(), this.playerCache, this.playerFactory));
    PluginUtil.registerListener(
        new PlayerInteractListener(
            this.crateCache,
            this.playerCache,
            this.openingAnnouncer,
            CorePlugin.INSTANCE.getTaskDispatcher()));

    registerCrates();

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    this.blade =
        Blade.of()
            .fallbackPrefix("crates")
            .containerCreator(BukkitCommandContainer.CREATOR)
            .bindings(corePluginBlade.getBindings())
            .asyncExecutor(corePluginBlade.getAsyncExecutor())
            .customProviderMap(corePluginBlade.getCustomProviderMap())
            .tabCompleter(corePluginBlade.getTabCompleter())
            .helpGenerator(corePluginBlade.getHelpGenerator())
            .bind(Crate.class, new CrateParameterAdapter(this.crateCache))
            .bind(CratePlayer.class, new CratePlayerParameterAdapter(this.playerCache))
            .build()
            .register(
                new CrateCommand(
                    this.crateCache,
                    this.crateFactory,
                    this.crateUpdater,
                    this.playerCrateUpdater,
                    CorePlugin.INSTANCE.getPlayerCache()));
  }

  @Override
  public void disable() {}

  private void registerCrates() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketCrateRemove.class);
    packetCache.add(PacketCrateUpdate.class);
    packetCache.add(PacketCrateOpeningAnnounce.class);
    packetCache.add(PacketAllPlayersCrateUpdate.class);
    packetCache.add(PacketPlayerCrateUpdate.class);

    CorePlugin.INSTANCE.getNetworkService().subscribe(this.playerCrateUpdater);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.openingAnnouncer);
    CorePlugin.INSTANCE
        .getNetworkService()
        .subscribe(new CrateUpdater.CrateUpdateListener(this.crateFactory, this.crateCache));

    this.crateCache.addAll(this.crateFactory.loadAll());
  }
}
