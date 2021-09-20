package me.ponktacology.clashmc.safe;

import lombok.Getter;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.item.adapter.SafeItemParameterAdapter;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.item.command.SafeCommand;
import me.ponktacology.clashmc.safe.item.factory.SafeItemFactory;
import me.ponktacology.clashmc.safe.item.menu.factory.SafeMenuFactory;
import me.ponktacology.clashmc.safe.item.updater.SafeItemUpdater;
import me.ponktacology.clashmc.safe.item.updater.packet.PacketSafeItemRemove;
import me.ponktacology.clashmc.safe.item.updater.packet.PacketSafeItemUpdate;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.ponktacology.clashmc.safe.player.adapter.SafePlayerParameterAdapter;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import me.ponktacology.clashmc.safe.player.factory.SafePlayerFactory;
import me.ponktacology.clashmc.safe.player.listener.PlayerJoinListener;
import me.ponktacology.clashmc.safe.player.updater.LimitUpdater;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Getter
public enum SafePlugin implements BukkitPlugin {
  INSTANCE;

  private final SafeItemFactory itemFactory =
      new SafeItemFactory(CorePlugin.INSTANCE.getDataService());
  private final SafeItemCache itemCache = new SafeItemCache();
  private final SafeItemUpdater itemUpdater =
      new SafeItemUpdater(CorePlugin.INSTANCE.getNetworkService(), this.itemFactory);
  private final SafePlayerFactory playerFactory = new SafePlayerFactory();
  private final SafePlayerCache playerCache = new SafePlayerCache(this.playerFactory);
  private final LimitUpdater limitUpdater = new LimitUpdater(this.itemCache);
  private final SafeMenuFactory menuFactory =
      new SafeMenuFactory(
          this.itemCache, this.playerCache, CorePlugin.INSTANCE.getTaskDispatcher());

  @Override
  public void enable(JavaPlugin plugin) {
    registerSafeItems();
    registerPlayers();

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("safes")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(SafePlayer.class, new SafePlayerParameterAdapter(this.playerCache))
        .bind(SafeItem.class, new SafeItemParameterAdapter(this.itemCache))
        .build()
        .register(
            new SafeCommand(this.itemUpdater, this.itemFactory, this.itemCache, this.menuFactory));
  }

  @Override
  public void disable() {}

  private void registerPlayers() {
    PluginUtil.registerListener(
        new PlayerJoinListener(
            CorePlugin.INSTANCE.getTaskDispatcher(), this.playerCache, this.playerFactory));

    if (!SectorPlugin.INSTANCE.getLocalSector().isSpawn()) {
      CorePlugin.INSTANCE
          .getTaskDispatcher()
          .runTimerAsync(
              new LimitUpdater.LimitUpdateTask(this.playerCache, this.limitUpdater),
              1L,
              TimeUnit.SECONDS);
    }
  }

  private void registerSafeItems() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketSafeItemUpdate.class);
    packetCache.add(PacketSafeItemRemove.class);

    this.itemCache.addAll(this.itemFactory.loadAll());

    CorePlugin.INSTANCE
        .getNetworkService()
        .subscribe(new SafeItemUpdater.SafeItemUpdateListener(this.itemFactory, this.itemCache));
  }
}
