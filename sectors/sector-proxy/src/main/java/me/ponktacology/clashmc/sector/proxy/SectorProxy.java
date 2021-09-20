package me.ponktacology.clashmc.sector.proxy;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.ponktacology.clashmc.api.dispatcher.ExecutorTaskDispatcher;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.serializer.gson.GsonSerializer;
import me.ponktacology.clashmc.api.service.data.mongo.MongoDataService;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.api.service.network.packet.listener.cache.PacketListenerCache;
import me.ponktacology.clashmc.api.service.network.redis.RedisDataService;
import me.ponktacology.clashmc.sector.api.motd.cache.MotdSettingsCache;
import me.ponktacology.clashmc.sector.api.motd.factory.MotdSettingsFactory;
import me.ponktacology.clashmc.sector.api.motd.updater.MotdUpdater;
import me.ponktacology.clashmc.sector.api.motd.updater.packet.PacketMotdUpdate;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.player.packet.PacketPlayerTransferRequest;
import me.ponktacology.clashmc.sector.api.redirect.PacketPlayerRedirectUpdate;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.api.sector.time.TimeUpdater;
import me.ponktacology.clashmc.sector.api.sector.time.packet.PacketTimeUpdate;
import me.ponktacology.clashmc.sector.api.sector.updater.SectorUpdater;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorRemove;
import me.ponktacology.clashmc.sector.api.sector.updater.packet.PacketSectorUpdate;
import me.ponktacology.clashmc.sector.api.whitelist.cache.WhitelistSettingsCache;
import me.ponktacology.clashmc.sector.api.whitelist.factory.WhitelistSettingsFactory;
import me.ponktacology.clashmc.sector.api.whitelist.updater.WhitelistUpdater;
import me.ponktacology.clashmc.sector.api.whitelist.updater.packet.PacketWhitelistUpdate;
import me.ponktacology.clashmc.sector.proxy.configuration.SectorConfiguration;
import me.ponktacology.clashmc.sector.proxy.player.listener.PlayerListener;
import me.ponktacology.clashmc.sector.proxy.player.listener.PlayerTransferUpdateListener;
import me.ponktacology.clashmc.sector.proxy.player.redirect.RedirectUpdateListener;
import me.ponktacology.clashmc.sector.proxy.player.updater.PlayerUpdater;
import me.ponktacology.clashmc.sector.proxy.time.TimeUpdateTask;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;


import java.util.concurrent.TimeUnit;

@Getter
public enum SectorProxy {
  INSTANCE;

  private ProxyServer proxy;
  private Plugin plugin;
  private final TaskDispatcher taskDispatcher = new ExecutorTaskDispatcher();
  private final Gson entitySerializerGson = new GsonBuilder().create();
  private final Gson configurationSerializerGson =
      new GsonBuilder().serializeNulls().setPrettyPrinting().create();
  private final GsonSerializer configurationSerializer =
      new GsonSerializer(configurationSerializerGson);
  private final GsonSerializer entitySerializer = new GsonSerializer(entitySerializerGson);
  private final SectorConfiguration configuration =
      new SectorConfiguration(configurationSerializer);
  private final PacketListenerCache packetListenerCache =
      new PacketListenerCache(this.taskDispatcher);
  private final PacketCache packetCache = new PacketCache();
  private final MongoDataService mongoDataService =
      new MongoDataService(configuration.getMongoUrl(), entitySerializer);
  private final RedisDataService redisDataService =
      new RedisDataService(
          configuration.getRedisUrl(),
          configuration.getRedisPassword(),
          packetListenerCache,
          packetCache,
          entitySerializer);
  private final SectorPlayerFactory playerFactory = new SectorPlayerFactory(redisDataService);
  private final WhitelistSettingsFactory whitelistSettingsFactory =
      new WhitelistSettingsFactory(mongoDataService);
  private final WhitelistSettingsCache whitelistSettingsCache =
      new WhitelistSettingsCache(whitelistSettingsFactory);
  private final TimeUpdater timeUpdater = new TimeUpdater(redisDataService);
  private final SectorCache sectorCache = new SectorCache();
  private final SectorUpdater sectorUpdater = new SectorUpdater(redisDataService, mongoDataService);
  private final MotdSettingsFactory motdSettingsFactory = new MotdSettingsFactory(mongoDataService);
  private final MotdSettingsCache motdSettingsCache = new MotdSettingsCache(motdSettingsFactory);
  private final PlayerUpdater playerUpdater = new PlayerUpdater(redisDataService);

  public final void enable( Plugin plugin) {
    this.plugin = plugin;
    this.proxy = plugin.getProxy();

    PlayerListener playerListener =
        new PlayerListener(
            whitelistSettingsCache,
            configuration,
            sectorCache,
            playerUpdater,
            taskDispatcher,
            playerFactory,
            motdSettingsCache,
            redisDataService);

    proxy.getPluginManager().registerListener(plugin, playerListener);

    this.packetCache.add(PacketPlayerRedirectUpdate.class);
    this.redisDataService.subscribe(new RedirectUpdateListener(playerListener));

    registerWhitelist();
    registerSector();
    registerPlayerTransfer();
    registerMotd();

    if (configuration.isMain()) registerTime();
  }

  public void disable() {}

  public void registerWhitelist() {
    packetCache.add(PacketWhitelistUpdate.class);
    redisDataService.subscribe(
        new WhitelistUpdater(
            mongoDataService, redisDataService, whitelistSettingsCache, whitelistSettingsFactory));
  }

  public void registerTime() {
    packetCache.add(PacketTimeUpdate.class);
    taskDispatcher.runTimerAsync(new TimeUpdateTask(this.timeUpdater), 1L, TimeUnit.MINUTES);
  }

  public void registerSector() {
    packetCache.add(PacketSectorRemove.class);
    packetCache.add(PacketSectorUpdate.class);
    redisDataService.subscribe(
        new SectorUpdater.SectorUpdaterListener(mongoDataService, sectorCache));
  }

  public void registerPlayerTransfer() {
    packetCache.add(PacketPlayerTransferRequest.class);
    redisDataService.subscribe(new PlayerTransferUpdateListener(proxy, sectorCache));
  }

  public void registerMotd() {
    packetCache.add(PacketMotdUpdate.class);
    redisDataService.subscribe(
        new MotdUpdater(
            mongoDataService, redisDataService, motdSettingsCache, motdSettingsFactory));
  }
}
