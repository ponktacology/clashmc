package me.ponktacology.clashmc.queue;

import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.clashmc.queue.entry.cache.QueueEntryCache;
import me.ponktacology.clashmc.queue.entry.listener.PlayerChatListener;
import me.ponktacology.clashmc.queue.entry.listener.PlayerDataListener;
import me.ponktacology.clashmc.queue.entry.updater.QueueEntryUpdater;
import me.ponktacology.clashmc.queue.settings.cache.QueueSettingsCache;
import me.ponktacology.clashmc.queue.settings.command.QueueCommand;
import me.ponktacology.clashmc.queue.settings.factory.QueueSettingsFactory;
import me.ponktacology.clashmc.queue.settings.updater.QueueSettingsUpdater;
import me.ponktacology.clashmc.queue.settings.updater.packet.PacketQueueSettingsUpdate;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Slf4j
public enum QueuePlugin implements BukkitPlugin {
  INSTANCE;

  private final QueueSettingsFactory settingsFactory =
      new QueueSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final QueueSettingsCache settingsCache = new QueueSettingsCache(this.settingsFactory);
  private final QueueSettingsUpdater settingsUpdater =
      new QueueSettingsUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          this.settingsCache,
          this.settingsFactory);
  private final QueueEntryCache entryCache = new QueueEntryCache();
  private final QueueEntryUpdater queueUpdater =
      new QueueEntryUpdater(
          SectorPlugin.INSTANCE.getSectorCache(),
          this.entryCache,
          SectorPlugin.INSTANCE.getRedirectUpdater(),
          CorePlugin.INSTANCE.getPlayerCache(),
          this.settingsCache);

  @Override
  public void enable(JavaPlugin plugin) {
    CorePlayerCache playerCache = CorePlugin.INSTANCE.getPlayerCache();
    PluginUtil.registerListener(
        new PlayerDataListener(
            CorePlugin.INSTANCE.getTaskDispatcher(), this.entryCache, playerCache));
    PluginUtil.registerListener(new PlayerChatListener(playerCache));
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimerAsync(this.queueUpdater::update, 500L, TimeUnit.MILLISECONDS);

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("queue")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter()).helpGenerator(corePluginBlade.getHelpGenerator())
        .build()
        .register(new QueueCommand(this.entryCache, this.settingsCache, this.settingsUpdater));

    this.registerQueueSettings();
  }

  @Override
  public void disable() {}

  public void registerQueueSettings() {
    log.info("Registering queue settings");
    CorePlugin.INSTANCE.getPacketCache().add(PacketQueueSettingsUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.settingsUpdater);
  }
}
