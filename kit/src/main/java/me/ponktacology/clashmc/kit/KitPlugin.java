package me.ponktacology.clashmc.kit;

import lombok.Getter;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.adapter.KitParameterAdapter;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.command.KitCommand;
import me.ponktacology.clashmc.kit.kit.factory.KitFactory;
import me.ponktacology.clashmc.kit.menu.KitMenuFactory;
import me.ponktacology.clashmc.kit.kit.updater.KitUpdater;
import me.ponktacology.clashmc.kit.kit.updater.packet.PacketKitRemove;
import me.ponktacology.clashmc.kit.kit.updater.packet.PacketKitUpdate;
import me.ponktacology.clashmc.kit.player.KitPlayer;
import me.ponktacology.clashmc.kit.player.adapter.KitPlayerParameterAdapter;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import me.ponktacology.clashmc.kit.player.factory.KitPlayerFactory;
import me.ponktacology.clashmc.kit.player.listener.PlayerDataListener;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum KitPlugin implements BukkitPlugin {
  INSTANCE;

  private final KitPlayerFactory playerFactory =
      new KitPlayerFactory();
  private final KitPlayerCache playerCache =
      new KitPlayerCache(this.playerFactory);
  private final KitCache kitCache = new KitCache();
  private final KitFactory kitFactory = new KitFactory(CorePlugin.INSTANCE.getDataService());
  private final KitUpdater kitUpdater =
      new KitUpdater(this.kitFactory, CorePlugin.INSTANCE.getNetworkService());
  private final KitMenuFactory menuFactory = new KitMenuFactory(this.playerCache, this.kitCache);

  @Override
  public void enable(JavaPlugin plugin) {
    this.registerKits();

    PluginUtil.registerListener(
        new PlayerDataListener(
            CorePlugin.INSTANCE.getTaskDispatcher(), this.playerCache, this.playerFactory));

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("kits")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter()).helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(KitPlayer.class, new KitPlayerParameterAdapter(this.playerCache))
        .bind(Kit.class, new KitParameterAdapter(this.kitCache))
        .build()
        .register(
            new KitCommand(
                this.kitCache,
                this.kitFactory,
                this.playerCache,
                this.kitUpdater,
                this.menuFactory));
  }

  @Override
  public void disable() {}

  private void registerKits() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketKitUpdate.class);
    packetCache.add(PacketKitRemove.class);

    CorePlugin.INSTANCE
        .getNetworkService()
        .subscribe(new KitUpdater.KitUpdateListener(this.kitFactory, this.kitCache));

    this.kitCache.addAll(this.kitFactory.loadAll());
  }
}
