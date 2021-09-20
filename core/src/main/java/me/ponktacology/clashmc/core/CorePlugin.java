package me.ponktacology.clashmc.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.serializer.gson.GsonSerializer;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.api.service.network.packet.listener.cache.PacketListenerCache;
import me.ponktacology.clashmc.core.blazingpack.bar.manager.BarManager;
import me.ponktacology.clashmc.core.chat.global.announcer.GlobalChatAnnouncer;
import me.ponktacology.clashmc.core.chat.global.announcer.GlobalChatAnnouncerImpl;
import me.ponktacology.clashmc.core.chat.global.announcer.LocalChatAnnouncer;
import me.ponktacology.clashmc.core.chat.global.broadcast.adapter.BroadcastTypeParameterAdapter;
import me.ponktacology.clashmc.core.chat.global.broadcast.announcer.BroadcastAnnouncer;
import me.ponktacology.clashmc.core.chat.global.broadcast.announcer.packet.PacketBroadcastAnnounce;
import me.ponktacology.clashmc.core.chat.global.broadcast.command.BroadcastCommand;
import me.ponktacology.clashmc.core.chat.global.broadcast.type.BroadcastType;
import me.ponktacology.clashmc.core.chat.global.packet.PacketGlobalChatAnnounce;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.chat.global.settings.command.ChatSettingsCommand;
import me.ponktacology.clashmc.core.chat.global.settings.factory.ChatSettingsFactory;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatClearUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatSettingsUpdater;
import me.ponktacology.clashmc.core.chat.global.settings.updater.packet.PacketChatClearUpdate;
import me.ponktacology.clashmc.core.chat.global.settings.updater.packet.PacketChatSettingsUpdate;
import me.ponktacology.clashmc.core.chat.listener.PlayerAsyncChatListener;
import me.ponktacology.clashmc.core.chat.staff.announcer.StaffChatAnnouncer;
import me.ponktacology.clashmc.core.chat.staff.command.StaffChatCommand;
import me.ponktacology.clashmc.core.chat.staff.packet.PacketStaffChatAnnounce;
import me.ponktacology.clashmc.core.chat.staff.request.announcer.StaffRequestAnnouncer;
import me.ponktacology.clashmc.core.chat.staff.request.announcer.packet.PacketStaffRequestAnnounce;
import me.ponktacology.clashmc.core.chat.staff.request.command.StaffRequestCommand;
import me.ponktacology.clashmc.core.configuration.CoreConfiguration;
import me.ponktacology.clashmc.core.dispatcher.BukkitTaskDispatcher;
import me.ponktacology.clashmc.core.help.CustomHelpGenerator;
import me.ponktacology.clashmc.core.log.LogsCommand;
import me.ponktacology.clashmc.core.menu.CoreMenuFactory;
import me.ponktacology.clashmc.core.menu.MenuListener;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.adapter.CorePlayerParameterAdapter;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.command.*;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.player.listener.EntityDamageListener;
import me.ponktacology.clashmc.core.player.listener.PlayerCommandPreprocessListener;
import me.ponktacology.clashmc.core.player.listener.PlayerDataListener;
import me.ponktacology.clashmc.core.player.privatemessage.announcer.PrivateMessageAnnouncer;
import me.ponktacology.clashmc.core.player.privatemessage.announcer.packet.PacketPrivateMessageAnnounce;
import me.ponktacology.clashmc.core.player.privatemessage.command.PrivateMessageCommand;
import me.ponktacology.clashmc.core.player.privatemessage.listener.PlayerPrivateMessageListener;
import me.ponktacology.clashmc.core.player.vanish.VanishTask;
import me.ponktacology.clashmc.core.punishment.anouncer.PunishmentAnnouncer;
import me.ponktacology.clashmc.core.punishment.anouncer.packet.PacketPunishmentAnnounce;
import me.ponktacology.clashmc.core.punishment.command.PunishmentCommand;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.adapter.RankParameterAdapter;
import me.ponktacology.clashmc.core.rank.cache.RankCache;
import me.ponktacology.clashmc.core.rank.command.RankCommand;
import me.ponktacology.clashmc.core.rank.factory.RankFactory;
import me.ponktacology.clashmc.core.rank.updater.RankUpdater;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketPlayerAddRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketPlayerRemoveRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketRemoveRank;
import me.ponktacology.clashmc.core.rank.updater.packet.PacketUpdateRank;
import me.ponktacology.clashmc.core.service.BukkitMongoDataService;
import me.ponktacology.clashmc.core.service.BukkitRedisDataService;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.time.adapter.TimeParameterAdapter;
import me.ponktacology.clashmc.core.util.*;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.bindings.impl.BukkitBindings;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;

import java.util.concurrent.TimeUnit;

@Slf4j
@Getter
public enum CorePlugin implements BukkitPlugin {
  INSTANCE;

  private JavaPlugin plugin;

  private final Gson entitySerializerGson =
      new GsonBuilder()
          .registerTypeHierarchyAdapter(Enchantment.class, new EnchantmentAdapter())
          .registerTypeHierarchyAdapter(Location.class, new LocationAdapter())
          .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
          .registerTypeHierarchyAdapter(PotionEffect.class, new PotionEffectAdapter())
          .serializeNulls()
          .create();
  private final GsonSerializer entitySerializer = new GsonSerializer(entitySerializerGson);
  private final TaskDispatcher taskDispatcher = new BukkitTaskDispatcher();
  private final CoreConfiguration configuration =
      new CoreConfiguration(entitySerializer);
  private final PacketListenerCache packetListenerCache =
      new PacketListenerCache(this.taskDispatcher);
  private final PacketCache packetCache = new PacketCache();
  private final BarManager barManager = new BarManager();
  private final BukkitMongoDataService dataService =
      new BukkitMongoDataService(configuration.getMongoUrl(), entitySerializer);
  private final BukkitRedisDataService cacheNetworkService =
      new BukkitRedisDataService(
          configuration.getRedisCacheUrl(),
          configuration.getRedisCachePassword(),
          packetListenerCache,
          packetCache,
          entitySerializer);
  private final BukkitRedisDataService networkService =
      new BukkitRedisDataService(
          configuration.getRedisUrl(),
          configuration.getRedisPassword(),
          packetListenerCache,
          packetCache,
          entitySerializer);
  private final RankFactory rankFactory = new RankFactory(dataService);
  private final RankCache rankCache = new RankCache();
  private final CorePlayerFactory playerFactory = new CorePlayerFactory();
  private final CorePlayerCache playerCache = new CorePlayerCache(playerFactory);
  private final RankUpdater rankUpdater = new RankUpdater(rankFactory, networkService, playerCache);
  private final ChatSettingsFactory chatSettingsFactory = new ChatSettingsFactory(dataService);
  private final ChatSettingsCache chatSettingsCache = new ChatSettingsCache(chatSettingsFactory);
  private final ChatSettingsUpdater chatSettingsUpdater =
      new ChatSettingsUpdater(dataService, networkService, chatSettingsCache, chatSettingsFactory);
  private final StaffChatAnnouncer staffChatAnnouncer =
      new StaffChatAnnouncer(playerCache, configuration, networkService);
  private final ChatClearUpdater chatClearUpdater =
      new ChatClearUpdater(networkService, chatSettingsCache);
  private final CoreMenuFactory coreMenuFactory =
      new CoreMenuFactory(
          playerFactory,
          playerCache,
          chatSettingsCache,
          chatSettingsUpdater,
          chatSettingsFactory,
          chatClearUpdater,
          taskDispatcher);
  private final PunishmentAnnouncer punishmentAnnouncer =
      new PunishmentAnnouncer(playerFactory, playerCache, networkService, taskDispatcher);
  private final BroadcastAnnouncer broadcastAnnouncer =
      new BroadcastAnnouncer(this.barManager, this.networkService, this.taskDispatcher);

  @Setter
  private GlobalChatAnnouncer chatAnnouncer =
      configuration.useLocalChatAnnouncer()
          ? new LocalChatAnnouncer(playerCache)
          : configuration.useDefaultGlobalChatAnnouncer()
              ? new GlobalChatAnnouncerImpl(networkService, playerCache)
              : null;

  private final PrivateMessageAnnouncer privateMessageAnnouncer =
      new PrivateMessageAnnouncer(networkService, playerCache);
  private final StaffRequestAnnouncer staffRequestAnnouncer =
      new StaffRequestAnnouncer(networkService, playerCache, configuration);
  private Blade blade;

  public void enable(JavaPlugin plugin) {
    log.info("Initializing core plugin");
    this.plugin = plugin;
    this.blade =
        Blade.of()
            .fallbackPrefix("core")
            .containerCreator(BukkitCommandContainer.CREATOR)
            .asyncExecutor(this.taskDispatcher::runAsync)
            .binding(new BukkitBindings())
            .bind(Rank.class, new RankParameterAdapter(this.rankCache))
            .bind(CorePlayer.class, new CorePlayerParameterAdapter(this.playerCache))
            .bind(Time.class, new TimeParameterAdapter())
            .bind(BroadcastType.class, new BroadcastTypeParameterAdapter())
            // .tabCompleter(new ProtocolLibTabCompleter(plugin))
            .helpGenerator(new CustomHelpGenerator())
            .build()
            .register(new RedisCommand(this.networkService, this.cacheNetworkService))
            .register(new WorkbenchCommand())
            .register(new RepairCommand())
            .register(new FlyCommand())
            .register(new VanishCommand(this.taskDispatcher, this.playerCache))
            .register(new BroadcastCommand(this.broadcastAnnouncer))
            .register(new HelpCommand())
            .register(new DonorCommand())
            .register(new HealCommand())
            .register(new GodCommand(this.playerCache))
            .register(new RankCommand(rankCache, rankFactory, rankUpdater, coreMenuFactory))
            .register(new PunishmentCommand(punishmentAnnouncer, playerCache, coreMenuFactory))
            .register(new StaffChatCommand(staffChatAnnouncer, playerCache))
            .register(new StaffRequestCommand(playerCache, staffRequestAnnouncer))
            .register(new LogsCommand(coreMenuFactory))
            .register(new ListCommand())
            .register(new SpeedCommand())
            .register(new OnlineCommand(this.playerCache))
            .register(new PrivateMessageCommand(privateMessageAnnouncer, playerCache))
            .register(new ChatSettingsCommand(coreMenuFactory));
    PluginUtil.registerListener(new PlayerDataListener(playerFactory, playerCache, taskDispatcher));
    PluginUtil.registerListener(new PlayerPrivateMessageListener());
    PluginUtil.registerListener(new EntityDamageListener(this.playerCache));
    registerRanks();
    registerPunishments();
    registerGlobalChat();
    registerStaffChat();
    registerMenus();
    registerCommands();
    registerPrivateMessage();
    registerVanish();
    registerBroadcast();
    log.info("Enabled plugin and running!");
  }

  public void disable() {}

  private void registerMenus() {
    PluginUtil.registerListener(new MenuListener(taskDispatcher));
  }

  private void registerPunishments() {
    log.debug("Registering punishments");
    packetCache.add(PacketPunishmentAnnounce.class);
    networkService.subscribe(
        new PunishmentAnnouncer.PunishmentAnnounceListener(punishmentAnnouncer));
  }

  private void registerRanks() {
    log.debug("Registering ranks");
    packetCache.add(PacketUpdateRank.class);
    packetCache.add(PacketRemoveRank.class);
    packetCache.add(PacketPlayerAddRank.class);
    packetCache.add(PacketPlayerRemoveRank.class);

    rankCache.addAll(rankFactory.loadAll());

    networkService.subscribe(
        new RankUpdater.RankUpdateListener(rankFactory, rankCache, playerCache));
  }

  private void registerCommands() {
    log.debug("Registering commands");

    PluginUtil.registerListener(new PlayerCommandPreprocessListener(playerCache));
  }

  private void registerGlobalChat() {
    log.debug("Registering global chat");
    packetCache.add(PacketGlobalChatAnnounce.class);
    PluginUtil.registerListener(
        new PlayerAsyncChatListener(taskDispatcher, playerCache, chatSettingsCache));

    if (!configuration.useLocalChatAnnouncer() && configuration.useDefaultGlobalChatAnnouncer()) {
      networkService.subscribe(
          new GlobalChatAnnouncerImpl.GlobalChatAnnounceListener(chatAnnouncer));
    }

    log.debug("Registering chat settings");
    packetCache.add(PacketChatSettingsUpdate.class);
    packetCache.add(PacketChatClearUpdate.class);
    networkService.subscribe(new ChatClearUpdater.ChatClearUpdateListener(playerCache));
    networkService.subscribe(chatSettingsUpdater);
  }

  private void registerStaffChat() {
    log.debug("Registering staff chat");
    packetCache.add(PacketStaffChatAnnounce.class);
    packetCache.add(PacketStaffRequestAnnounce.class);
    networkService.subscribe(new StaffChatAnnouncer.StaffChatAnnounceListener(staffChatAnnouncer));
    networkService.subscribe(
        new StaffRequestAnnouncer.RequestAnnounceListener(staffRequestAnnouncer));
  }

  private void registerPrivateMessage() {
    log.debug("Registering private message");
    packetCache.add(PacketPrivateMessageAnnounce.class);
    networkService.subscribe(
        new PrivateMessageAnnouncer.PrivateMessageAnnounceListener(privateMessageAnnouncer));
  }

  private void registerVanish() {
    this.taskDispatcher.runTimerAsync(new VanishTask(this.playerCache), 1L, TimeUnit.SECONDS);
  }

  private void registerBroadcast() {
    this.packetCache.add(PacketBroadcastAnnounce.class);
    this.networkService.subscribe(this.broadcastAnnouncer);
  }
}
