package me.ponktacology.clashmc.sector;

import io.github.thatkawaiisam.assemble.Assemble;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.api.service.network.redis.RedisDataService;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.sector.api.motd.cache.MotdSettingsCache;
import me.ponktacology.clashmc.sector.api.motd.factory.MotdSettingsFactory;
import me.ponktacology.clashmc.sector.api.motd.updater.MotdUpdater;
import me.ponktacology.clashmc.sector.api.motd.updater.packet.PacketMotdUpdate;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.packet.PacketPlayerTransferRequest;
import me.ponktacology.clashmc.sector.api.redirect.PacketPlayerRedirectUpdate;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorRegion;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.api.sector.time.packet.PacketTimeUpdate;
import me.ponktacology.clashmc.sector.api.sector.updater.SectorUpdater;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorRemove;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorUpdate;
import me.ponktacology.clashmc.sector.api.whitelist.cache.WhitelistSettingsCache;
import me.ponktacology.clashmc.sector.api.whitelist.factory.WhitelistSettingsFactory;
import me.ponktacology.clashmc.sector.api.whitelist.updater.WhitelistUpdater;
import me.ponktacology.clashmc.sector.api.whitelist.updater.packet.PacketWhitelistUpdate;
import me.ponktacology.clashmc.sector.border.cache.BorderCache;
import me.ponktacology.clashmc.sector.border.updater.BorderUpdater;
import me.ponktacology.clashmc.sector.border.updater.task.BorderUpdateTask;
import me.ponktacology.clashmc.sector.configuration.SectorConfiguration;
import me.ponktacology.clashmc.sector.player.command.EnderchestCommand;
import me.ponktacology.clashmc.sector.player.command.InvSeeCommand;
import me.ponktacology.clashmc.sector.player.command.SpawnCommand;
import me.ponktacology.clashmc.sector.player.command.TrashCommand;
import me.ponktacology.clashmc.sector.player.inventory.cache.InventoryCache;
import me.ponktacology.clashmc.sector.player.inventory.updater.InventoryUpdater;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryRequest;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryResponse;
import me.ponktacology.clashmc.sector.player.inventory.updater.packet.PacketPlayerInventoryUpdate;
import me.ponktacology.clashmc.sector.player.listener.PlayerDataListener;
import me.ponktacology.clashmc.sector.player.listener.PlayerJumpPadListener;
import me.ponktacology.clashmc.sector.player.listener.PlayerMoveListener;
import me.ponktacology.clashmc.sector.player.listener.PlayerTransferListener;
import me.ponktacology.clashmc.sector.player.redirect.PlayerRedirectUpdater;
import me.ponktacology.clashmc.sector.player.teleport.cache.TeleportCache;
import me.ponktacology.clashmc.sector.player.teleport.command.TeleportCommand;
import me.ponktacology.clashmc.sector.player.teleport.packet.PacketPlayerTeleportAccept;
import me.ponktacology.clashmc.sector.player.teleport.packet.PacketPlayerTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.board.SectorBoard;
import me.ponktacology.clashmc.sector.sector.command.ChannelCommand;
import me.ponktacology.clashmc.sector.sector.command.MotdCommand;
import me.ponktacology.clashmc.sector.sector.command.SectorCommand;
import me.ponktacology.clashmc.sector.sector.listener.SectorProtectionListener;
import me.ponktacology.clashmc.sector.sector.menu.SectorMenuFactory;
import me.ponktacology.clashmc.sector.sector.task.SectorUpdateTask;
import me.ponktacology.clashmc.sector.sector.time.TimeUpdateListener;
import me.ponktacology.clashmc.sector.whitelist.WhitelistCommand;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public enum SectorPlugin implements BukkitPlugin {
  INSTANCE;

  private JavaPlugin plugin;
  private final SectorConfiguration configuration = new SectorConfiguration();
  private final SectorPlayerFactory playerFactory =
      new SectorPlayerFactory(CorePlugin.INSTANCE.getNetworkService());
  private final BorderCache borderCache = new BorderCache();
  private final Sector localSector =
      new Sector(
          CorePlugin.INSTANCE.getConfiguration().getServerName(),
          configuration.getSectorType(),
          new SectorRegion(
              configuration.getMinX(),
              configuration.getMaxX(),
              configuration.getMinZ(),
              configuration.getMaxZ()));
  private final PlayerTransferUpdater transferUpdater =
      new PlayerTransferUpdater(
          playerFactory,
          localSector,
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getTaskDispatcher());
  private final SectorCache sectorCache = new SectorCache();
  private final SectorUpdater sectorUpdater =
      new SectorUpdater(
          CorePlugin.INSTANCE.getNetworkService(), CorePlugin.INSTANCE.getDataService());
  private final BorderUpdater borderUpdater =
      new BorderUpdater(sectorCache, borderCache, localSector, CorePlugin.INSTANCE.getBarManager());
  private final SectorMenuFactory menuFactory =
      new SectorMenuFactory(
          this.sectorCache,
          CorePlugin.INSTANCE.getPlayerCache(),
          CorePlugin.INSTANCE.getTaskDispatcher(),
          this.transferUpdater,
          this.localSector);
  private final WhitelistSettingsFactory whitelistSettingsFactory =
      new WhitelistSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final WhitelistSettingsCache whitelistSettingsCache =
      new WhitelistSettingsCache(whitelistSettingsFactory);
  private final WhitelistUpdater whitelistUpdater =
      new WhitelistUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          whitelistSettingsCache,
          whitelistSettingsFactory);
  private final MotdSettingsFactory motdSettingsFactory =
      new MotdSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final MotdSettingsCache motdSettingsCache = new MotdSettingsCache(motdSettingsFactory);
  private final MotdUpdater motdUpdater =
      new MotdUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          motdSettingsCache,
          motdSettingsFactory);
  private final TeleportCache teleportCache = new TeleportCache();
  private final PlayerTeleportUpdater teleportUpdater =
      new PlayerTeleportUpdater(CorePlugin.INSTANCE.getNetworkService(), transferUpdater);
  private final PlayerRedirectUpdater redirectUpdater =
      new PlayerRedirectUpdater(CorePlugin.INSTANCE.getNetworkService());
  private final InventoryCache inventoryCache =
      new InventoryCache(CorePlugin.INSTANCE.getNetworkService());
  private final InventoryUpdater inventoryUpdater =
      new InventoryUpdater(
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getPlayerCache(),
          this.playerFactory,
          this.inventoryCache);

  public void enable(JavaPlugin plugin) {
    this.plugin = plugin;

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade blade =
        Blade.of()
            .fallbackPrefix("sector")
            .containerCreator(BukkitCommandContainer.CREATOR)
            .bindings(corePluginBlade.getBindings())
            .asyncExecutor(corePluginBlade.getAsyncExecutor())
            .customProviderMap(corePluginBlade.getCustomProviderMap())
            .tabCompleter(corePluginBlade.getTabCompleter())
            .helpGenerator(corePluginBlade.getHelpGenerator())
            .build()
            .register(new InvSeeCommand(this.inventoryUpdater))
            .register(new EnderchestCommand(this.inventoryUpdater))
            .register(new MotdCommand(this.motdUpdater, this.motdSettingsCache))
            .register(new WhitelistCommand(this.whitelistSettingsCache, this.whitelistUpdater))
            .register(new SectorCommand(this.menuFactory))
            .register(
                new SpawnCommand(
                    CorePlugin.INSTANCE.getPlayerCache(), this.teleportUpdater, this.sectorCache));

    if (this.localSector.isSpawn()) {
      PluginUtil.registerListener(new PlayerJumpPadListener());
      blade.register(new TrashCommand());
      blade.register(new ChannelCommand(this.menuFactory));
    }

    if (!this.localSector.isSpecial()) {
      blade.register(
          new TeleportCommand(
              this.teleportUpdater,
              this.teleportCache,
              CorePlugin.INSTANCE.getPlayerCache(),
              this.localSector));
    }

    this.registerSector();
    this.registerSectorPlayer();
    this.registerWhitelistSettings();
    this.registerMotdSettings();
    this.registerTeleport();
    this.registerRedirect();
    this.registerInventoryUpdate();

    Assemble assemble =
        new Assemble(
            plugin,
            new SectorBoard(
                this.localSector, this.sectorCache, CorePlugin.INSTANCE.getPlayerCache()));
    assemble.setTicks(40L);
  }

  public void disable() {
    this.sectorUpdater.remove(this.localSector);
  }

  public void registerSectorPlayer() {
    if (!this.localSector.isSpecial()) {
      PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();
      packetCache.add(PacketPlayerTransferRequest.class);

      TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();

      Arrays.asList(
              new PlayerDataListener(
                  taskDispatcher,
                  playerFactory,
                  borderCache,
                  localSector,
                  transferUpdater,
                  CorePlugin.INSTANCE.getNetworkService(),
                  teleportCache,
                  teleportUpdater,
                  CorePlugin.INSTANCE.getBarManager(),
                  this.inventoryCache),
              new PlayerTransferListener(
                  transferUpdater, sectorCache, CorePlugin.INSTANCE.getPlayerCache(), localSector),
              new SectorProtectionListener(
                  this.transferUpdater, this.teleportUpdater, this.localSector))
          .forEach(PluginUtil::registerListener);
    }
  }

  public void registerSector() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketTimeUpdate.class);
    packetCache.add(PacketSectorRemove.class);
    packetCache.add(PacketSectorUpdate.class);

    RedisDataService redisDataService = CorePlugin.INSTANCE.getNetworkService();

    redisDataService.subscribe(
        new SectorUpdater.SectorUpdaterListener(CorePlugin.INSTANCE.getDataService(), sectorCache));
    redisDataService.subscribe(new TimeUpdateListener(CorePlugin.INSTANCE.getTaskDispatcher()));

    TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
    SectorUpdateTask updateTask = new SectorUpdateTask(localSector, sectorUpdater);

    taskDispatcher.runTimerAsync(updateTask, 1, TimeUnit.SECONDS);

    if (!this.localSector.isSpecial()) {
      taskDispatcher.runTimerAsync(new BorderUpdateTask(borderUpdater), 200, TimeUnit.MILLISECONDS);
    }
  }

  public void registerWhitelistSettings() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketWhitelistUpdate.class);

    CorePlugin.INSTANCE.getNetworkService().subscribe(whitelistUpdater);
  }

  public void registerTeleport() {
    if (!this.localSector.isSpecial()) {
      PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

      packetCache.add(PacketPlayerTeleportRequest.class);
      packetCache.add(PacketPlayerTeleportAccept.class);

      CorePlugin.INSTANCE
          .getNetworkService()
          .subscribe(
              new PlayerTeleportUpdater.PlayerSectorTeleportUpdateListener(
                  CorePlugin.INSTANCE.getPlayerCache(),
                  teleportCache,
                  sectorCache,
                  transferUpdater,
                  teleportUpdater,
                  localSector));

      PluginUtil.registerListener(new PlayerMoveListener(this.teleportUpdater));
    }
  }

  public void registerMotdSettings() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketMotdUpdate.class);

    CorePlugin.INSTANCE.getNetworkService().subscribe(motdUpdater);
  }

  public void registerRedirect() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketPlayerRedirectUpdate.class);
  }

  public void registerInventoryUpdate() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketPlayerInventoryUpdate.class);
    packetCache.add(PacketPlayerInventoryResponse.class);
    packetCache.add(PacketPlayerInventoryRequest.class);

    CorePlugin.INSTANCE.getNetworkService().subscribe(this.inventoryUpdater);
  }
}
