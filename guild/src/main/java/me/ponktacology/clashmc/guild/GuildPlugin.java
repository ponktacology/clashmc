package me.ponktacology.clashmc.guild;

import io.github.thatkawaiisam.ziggurat.Ziggurat;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.chat.global.announcer.GlobalChatAnnouncer;
import me.ponktacology.clashmc.core.util.ItemUtil;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.command.EnchantLimiterCommand;
import me.ponktacology.clashmc.guild.enchantmentlimiter.factory.EnchantLimiterSettingsFactory;
import me.ponktacology.clashmc.guild.enchantmentlimiter.listener.EnchantLimiterListener;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.EnchantLimiterSettingsUpdater;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.packet.PacketEnchantLimiterSettingsUpdate;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.announcer.GuildActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.action.announcer.packet.PacketGuildActionAnnounce;
import me.ponktacology.clashmc.guild.guild.adapter.GuildParameterAdapter;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.command.*;
import me.ponktacology.clashmc.guild.guild.factory.GuildFactory;
import me.ponktacology.clashmc.guild.guild.invite.updater.InviteUpdater;
import me.ponktacology.clashmc.guild.guild.invite.updater.packet.PacketGuildInviteUpdate;
import me.ponktacology.clashmc.guild.guild.leave.updater.LeaveUpdater;
import me.ponktacology.clashmc.guild.guild.leave.updater.packet.PacketGuildLeaveUpdate;
import me.ponktacology.clashmc.guild.guild.listener.GuildHeartRoomProtectionListener;
import me.ponktacology.clashmc.guild.guild.listener.GuildRegionProtectionListener;
import me.ponktacology.clashmc.guild.guild.listener.GuildWarListener;
import me.ponktacology.clashmc.guild.guild.listener.WaterFlowListener;
import me.ponktacology.clashmc.guild.guild.message.announcer.GuildMessageAnnouncer;
import me.ponktacology.clashmc.guild.guild.message.announcer.packet.PacketGuildMessageAnnounce;
import me.ponktacology.clashmc.guild.guild.settings.cache.GuildSettingsCache;
import me.ponktacology.clashmc.guild.guild.settings.factory.GuildSettingsFactory;
import me.ponktacology.clashmc.guild.guild.settings.updater.GuildSettingsUpdater;
import me.ponktacology.clashmc.guild.guild.settings.updater.packet.PacketGuildSettingsUpdate;
import me.ponktacology.clashmc.guild.guild.updater.GuildCuboidUpdater;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import me.ponktacology.clashmc.guild.guild.updater.packet.PacketGuildRemove;
import me.ponktacology.clashmc.guild.guild.updater.packet.PacketGuildUpdate;
import me.ponktacology.clashmc.guild.guild.war.action.GuildWarActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.war.action.packet.PacketGuildWarActionAnnounce;
import me.ponktacology.clashmc.guild.guild.war.updater.GuildWarUpdater;
import me.ponktacology.clashmc.guild.guild.war.updater.packet.PacketGuildWarRemove;
import me.ponktacology.clashmc.guild.guild.war.updater.packet.PacketGuildWarUpdate;
import me.ponktacology.clashmc.guild.menu.GuildMenuFactory;
import me.ponktacology.clashmc.guild.nametag.FrozenNametagHandler;
import me.ponktacology.clashmc.guild.nametag.GuildNameTagProvider;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.UtilityCommands;
import me.ponktacology.clashmc.guild.player.adapter.GuildPlayerParameterAdapter;
import me.ponktacology.clashmc.guild.player.automessage.AutoMessageTask;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.announcer.GuildGlobalChatAnnouncer;
import me.ponktacology.clashmc.guild.player.chat.command.ChatSettingsCommand;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import me.ponktacology.clashmc.guild.player.combattag.global.command.CombatTagSettingsCommand;
import me.ponktacology.clashmc.guild.player.combattag.global.factory.CombatTagSettingsFactory;
import me.ponktacology.clashmc.guild.player.combattag.global.updater.CombatTagSettingsUpdater;
import me.ponktacology.clashmc.guild.player.combattag.global.updater.packet.PacketCombatTagSettingsUpdate;
import me.ponktacology.clashmc.guild.player.combattag.updater.CombatTagUpdater;
import me.ponktacology.clashmc.guild.player.command.ResetRankCommand;
import me.ponktacology.clashmc.guild.player.death.announcer.PlayerDeathAnnouncer;
import me.ponktacology.clashmc.guild.player.death.announcer.packet.PacketPlayerDeathAnnounce;
import me.ponktacology.clashmc.guild.player.factory.GuildPlayerFactory;
import me.ponktacology.clashmc.guild.player.grief.PlacedBlockCache;
import me.ponktacology.clashmc.guild.player.grief.listener.AntiGriefListener;
import me.ponktacology.clashmc.guild.player.grief.task.AntiGriefTask;
import me.ponktacology.clashmc.guild.player.home.command.HomeCommand;
import me.ponktacology.clashmc.guild.player.incognito.listener.IncognitoApplyListener;
import me.ponktacology.clashmc.guild.player.incognito.updater.IncognitoUpdater;
import me.ponktacology.clashmc.guild.player.listener.*;
import me.ponktacology.clashmc.guild.player.region.GuildRegionBarUpdater;
import me.ponktacology.clashmc.guild.player.region.RegionCacheUpdater;
import me.ponktacology.clashmc.guild.player.spawnprotection.command.SpawnProtectionCommand;
import me.ponktacology.clashmc.guild.player.spawnprotection.updater.SpawnProtectionUpdater;
import me.ponktacology.clashmc.guild.player.statistics.command.StatisticsCommand;
import me.ponktacology.clashmc.guild.player.tab.TabAdapter;
import me.ponktacology.clashmc.guild.recipe.CrappleRecipe;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorRecipeCache;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;
import me.ponktacology.clashmc.guild.recipe.armor.cache.ArmorSettingsCache;
import me.ponktacology.clashmc.guild.recipe.armor.factory.ArmorSettingsFactory;
import me.ponktacology.clashmc.guild.recipe.armor.updater.ArmorSettingsUpdater;
import me.ponktacology.clashmc.guild.recipe.armor.updater.packet.PacketArmorSettingsUpdate;
import me.ponktacology.clashmc.guild.recipe.cache.RecipeCache;
import me.ponktacology.clashmc.guild.recipe.command.CraftingCommand;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Getter
@Slf4j
public enum GuildPlugin {
  INSTANCE;

  private JavaPlugin plugin;
  private final GuildPlayerFactory playerFactory = new GuildPlayerFactory();
  private final GuildPlayerCache playerCache = new GuildPlayerCache(this.playerFactory);
  private final GuildCuboidUpdater guildCuboidUpdater =
      new GuildCuboidUpdater(CorePlugin.INSTANCE.getPlayerCache(), this.playerCache);
  private final GuildCache guildCache = new GuildCache(this.guildCuboidUpdater);
  private final GuildFactory guildFactory = new GuildFactory(CorePlugin.INSTANCE.getDataService());
  private final GuildSettingsFactory guildSettingsFactory =
      new GuildSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final GuildSettingsCache guildSettingsCache =
      new GuildSettingsCache(guildSettingsFactory);
  private final GuildSettingsUpdater guildSettingsUpdater =
      new GuildSettingsUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          guildSettingsCache,
          guildSettingsFactory);
  private final GuildUpdater guildUpdater =
      new GuildUpdater(
          CorePlugin.INSTANCE.getNetworkService(), CorePlugin.INSTANCE.getDataService());
  private final RecipeCache recipeCache = new RecipeCache();
  private final SpawnProtectionUpdater spawnProtectionUpdater =
      new SpawnProtectionUpdater(CorePlugin.INSTANCE.getBarManager());
  private final CombatTagSettingsFactory combatTagSettingsFactory =
      new CombatTagSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final CombatTagSettingsCache combatTagSettingsCache =
      new CombatTagSettingsCache(combatTagSettingsFactory);
  private final CombatTagSettingsUpdater combatTagSettingsUpdater =
      new CombatTagSettingsUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          combatTagSettingsCache,
          combatTagSettingsFactory);
  private final CombatTagUpdater combatTagUpdater =
      new CombatTagUpdater(combatTagSettingsCache, CorePlugin.INSTANCE.getBarManager());
  private final GuildActionAnnouncer guildActionAnnouncer =
      new GuildActionAnnouncer(CorePlugin.INSTANCE.getNetworkService(), playerCache, guildCache);
  private final InviteUpdater inviteUpdater =
      new InviteUpdater(CorePlugin.INSTANCE.getNetworkService(), guildActionAnnouncer);
  private final LeaveUpdater leaveUpdater =
      new LeaveUpdater(CorePlugin.INSTANCE.getNetworkService(), guildActionAnnouncer);
  private final GuildMessageAnnouncer guildMessageAnnouncer =
      new GuildMessageAnnouncer(CorePlugin.INSTANCE.getNetworkService(), guildCache, playerCache);
  private final GlobalChatAnnouncer chatAnnouncer =
      new GuildGlobalChatAnnouncer(
          CorePlugin.INSTANCE.getNetworkService(),
          CorePlugin.INSTANCE.getPlayerCache(),
          playerCache,
          CorePlugin.INSTANCE.getTaskDispatcher());
  private final PlayerDeathAnnouncer deathAnnouncer =
      new PlayerDeathAnnouncer(CorePlugin.INSTANCE.getNetworkService(), playerCache);
  private final GuildRegionBarUpdater guildRegionUpdater =
      new GuildRegionBarUpdater(CorePlugin.INSTANCE.getBarManager());
  private final EnchantLimiterSettingsFactory enchantLimiterSettingsFactory =
      new EnchantLimiterSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final EnchantLimiterSettingsCache enchantLimiterSettingsCache =
      new EnchantLimiterSettingsCache(this.enchantLimiterSettingsFactory);
  private final EnchantLimiterSettingsUpdater enchantLimiterSettingsUpdater =
      new EnchantLimiterSettingsUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          this.enchantLimiterSettingsCache,
          this.enchantLimiterSettingsFactory);
  private final GuildMenuFactory menuFactory =
      new GuildMenuFactory(
          this.playerCache,
          CorePlugin.INSTANCE.getTaskDispatcher(),
          this.enchantLimiterSettingsCache,
          this.enchantLimiterSettingsUpdater);
  private final PlacedBlockCache placedBlockCache = new PlacedBlockCache();
  private final IncognitoUpdater incognitoUpdater =
      new IncognitoUpdater(CorePlugin.INSTANCE.getTaskDispatcher());
  private final GuildWarActionAnnouncer warActionAnnouncer =
      new GuildWarActionAnnouncer(
          CorePlugin.INSTANCE.getNetworkService(), this.playerCache, this.guildCache);
  private final GuildWarUpdater warUpdater =
      new GuildWarUpdater(
          CorePlugin.INSTANCE.getNetworkService(), this.warActionAnnouncer, this.guildCache);
  private final ArmorSettingsFactory armorSettingsFactory =
      new ArmorSettingsFactory(CorePlugin.INSTANCE.getDataService());
  private final ArmorSettingsCache armorSettingsCache =
      new ArmorSettingsCache(this.armorSettingsFactory);
  private final ArmorRecipeCache armorRecipeCache = new ArmorRecipeCache();
  private final ArmorSettingsUpdater armorSettingsUpdater =
      new ArmorSettingsUpdater(
          CorePlugin.INSTANCE.getDataService(),
          CorePlugin.INSTANCE.getNetworkService(),
          this.armorSettingsCache,
          this.armorSettingsFactory,
          this.armorRecipeCache);

  public void enable(JavaPlugin plugin) {
    this.plugin = plugin;
    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("guilds")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(GuildPlayer.class, new GuildPlayerParameterAdapter(playerCache))
        .bind(Guild.class, new GuildParameterAdapter(guildCache, playerCache))
        .build()
        .register(new ArmorCommand())
        .register(new IncognitoCommand(this.playerCache, this.incognitoUpdater))
        .register(new StatisticsCommand(this.playerCache))
        .register(new SpawnProtectionCommand(this.playerCache))
        .register(new CraftingCommand())
        .register(new RepairPickaxeCommand())
        .register(new EnchantLimiterCommand())
        .register(new GuildSettingsCommand(this.guildSettingsCache, this.guildSettingsUpdater))
        .register(new ResetRankCommand(this.playerCache))
        .register(
            new UtilityCommands(
                SectorPlugin.INSTANCE.getSectorCache(), SectorPlugin.INSTANCE.getTransferUpdater()))
        .register(
            new HomeCommand(
                CorePlugin.INSTANCE.getPlayerCache(),
                this.playerCache,
                SectorPlugin.INSTANCE.getTeleportUpdater()))
        .register(
            new CombatTagSettingsCommand(
                this.combatTagSettingsCache, this.combatTagSettingsUpdater))
        .register(
            new GuildCommand(
                this.guildSettingsCache,
                this.playerCache,
                this.guildFactory,
                this.guildCache,
                this.guildUpdater,
                this.guildActionAnnouncer,
                this.inviteUpdater,
                this.leaveUpdater,
                this.guildMessageAnnouncer,
                SectorPlugin.INSTANCE.getTeleportUpdater(),
                CorePlugin.INSTANCE.getPlayerCache(),
                CorePlugin.INSTANCE.getTaskDispatcher(),
                this.warUpdater))
        .register(new ChatSettingsCommand(this.menuFactory));

    new PacketListener(this.playerCache, plugin);

    Arrays.asList(
            new SpawnProtectionListener(this.playerCache, CorePlugin.INSTANCE.getPlayerCache()),
            new GuildPermissionListener(this.playerCache),
            new SubClaimListener(this.playerCache, CorePlugin.INSTANCE.getPlayerCache()),
            new GuildHeartRoomProtectionListener(
                this.playerCache, CorePlugin.INSTANCE.getPlayerCache(), this.guildCache),
            new GuildWarListener(
                this.guildCache,
                this.playerCache,
                this.warUpdater,
                this.guildUpdater,
                this.warActionAnnouncer,
                CorePlugin.INSTANCE.getTaskDispatcher()),
            new IncognitoApplyListener(
                this.playerCache, CorePlugin.INSTANCE.getPlayerCache(), this.incognitoUpdater),
            new AntiGriefListener(
                this.placedBlockCache, this.guildCache, CorePlugin.INSTANCE.getPlayerCache()),
            new PlayerItemConsumeListener(CorePlugin.INSTANCE.getTaskDispatcher()),
            new EnchantLimiterListener(this.enchantLimiterSettingsCache),
            new PlayerDataListener(
                playerCache,
                CorePlugin.INSTANCE.getPlayerCache(),
                SectorPlugin.INSTANCE.getTransferUpdater(),
                playerFactory,
                this.guildCuboidUpdater,
                CorePlugin.INSTANCE.getTaskDispatcher()),
            new PlayerRespawnListener(),
            new PluginDisableListener(
                combatTagSettingsCache, playerCache, this.guildCache, this.placedBlockCache),
            new EntityDamageListener(playerCache, combatTagSettingsCache),
            new GlobalChatListener(playerCache, CorePlugin.INSTANCE.getPlayerCache()),
            new PlayerCommandPreProcessListener(
                playerCache, CorePlugin.INSTANCE.getPlayerCache(), combatTagSettingsCache),
            new PlayerDeathListener(
                playerCache, deathAnnouncer, CorePlugin.INSTANCE.getTaskDispatcher()),
            new GuildRegionProtectionListener(
                this.guildCache,
                this.guildSettingsCache,
                CorePlugin.INSTANCE.getPlayerCache(),
                this.playerCache),
            new PlayerInteractListener(
                this.playerCache,
                CorePlugin.INSTANCE.getPlayerCache(),
                SectorPlugin.INSTANCE.getLocalSector(),
                SectorPlugin.INSTANCE.getSectorCache(),
                SectorPlugin.INSTANCE.getTransferUpdater(),
                CorePlugin.INSTANCE.getTaskDispatcher()),
            new PluginEnableListener(this.guildCache),
            new ProjectileHitListener(),
            new PlayerTeleportListener(playerCache, CorePlugin.INSTANCE.getTaskDispatcher()),
            new PrivateMessageListener(playerCache),
            new WaterFlowListener(this.guildCache),
            new PlayerSectorTransferListener(
                CorePlugin.INSTANCE.getPlayerCache(), this.playerCache))
        .forEach(PluginUtil::registerListener);

    this.registerGuilds();
    this.registerGuildSettings();
    this.registerCombatTag();
    this.registerSpawnProtection();
    this.registerChatAnnouncer();
    this.registerDeathAnnouncer();
    this.registerAutoMessage();
    this.registerEnchantLimiter();
    this.registerAntiGrief();
    this.registerArmor();

    ItemUtil.removeRecipe(Material.PISTON_BASE);
    ItemUtil.removeRecipe(Material.PISTON_STICKY_BASE);
    ItemUtil.removeRecipe(Material.BEACON);
    ItemUtil.removeRecipe(Material.MINECART);
    ItemUtil.removeRecipe(Material.BOAT);
    ItemUtil.removeRecipe(Material.BOOK_AND_QUILL);
    ItemUtil.removeRecipe(Material.FISHING_ROD);

    this.recipeCache.add(new CrappleRecipe());

    Ziggurat ziggurat =
        new Ziggurat(
            plugin, new TabAdapter(this.playerCache, SectorPlugin.INSTANCE.getSectorCache()));
    ziggurat.setTicks(20);

    FrozenNametagHandler.init(CorePlugin.INSTANCE.getTaskDispatcher());
    FrozenNametagHandler.registerProvider(
        new GuildNameTagProvider(this.playerCache, CorePlugin.INSTANCE.getPlayerCache()));
  }

  public void disable() {
  }

  public void registerCombatTag() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketCombatTagSettingsUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(combatTagSettingsUpdater);
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimerAsync(
            new CombatTagUpdater.CombatTagUpdateTask(this.playerCache, this.combatTagUpdater),
            500L,
            TimeUnit.MILLISECONDS);
  }

  public void registerGuildSettings() {
    log.info("Registering guild settings");
    CorePlugin.INSTANCE.getPacketCache().add(PacketGuildSettingsUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(guildSettingsUpdater);
  }

  public void registerSpawnProtection() {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimerAsync(
            new SpawnProtectionUpdater.SpawnProtectionUpdaterTask(
                this.playerCache, this.spawnProtectionUpdater),
            1,
            TimeUnit.SECONDS);
  }

  public void registerChatAnnouncer() {
    CorePlugin.INSTANCE.setChatAnnouncer(chatAnnouncer);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.chatAnnouncer);
  }

  public void registerDeathAnnouncer() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketPlayerDeathAnnounce.class);
    CorePlugin.INSTANCE
        .getNetworkService()
        .subscribe(new PlayerDeathAnnouncer.PlayerDeathAnnounceListener(deathAnnouncer));
  }

  public void registerGuilds() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketGuildUpdate.class);
    packetCache.add(PacketGuildRemove.class);
    packetCache.add(PacketGuildInviteUpdate.class);
    packetCache.add(PacketGuildLeaveUpdate.class);
    packetCache.add(PacketGuildMessageAnnounce.class);
    packetCache.add(PacketGuildActionAnnounce.class);
    packetCache.add(PacketGuildWarActionAnnounce.class);
    packetCache.add(PacketGuildWarUpdate.class);
    packetCache.add(PacketGuildWarRemove.class);

    this.guildCache.addAll(this.guildFactory.loadAll());

    NetworkService networkService = CorePlugin.INSTANCE.getNetworkService();
    networkService.subscribe(
        new GuildUpdater.GuildUpdateListener(
            this.guildFactory, this.guildCache, CorePlugin.INSTANCE.getTaskDispatcher()));
    networkService.subscribe(
        new GuildMessageAnnouncer.GuildMessageAnnounceListener(guildMessageAnnouncer));
    networkService.subscribe(
        new GuildActionAnnouncer.GuildActionAnnounceListener(guildActionAnnouncer));
    networkService.subscribe(new InviteUpdater.InviteUpdateListener(guildCache, playerCache));
    networkService.subscribe(new LeaveUpdater.LeaveUpdateListener(guildCache, playerCache));
    networkService.subscribe(this.warUpdater);
    networkService.subscribe(this.warActionAnnouncer);
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimerAsync(
            new GuildRegionBarUpdater.GuildRegionUpdateTask(
                this.playerCache, this.guildRegionUpdater),
            100L,
            TimeUnit.MILLISECONDS);

    PluginUtil.registerListener(
        new RegionCacheUpdater(
            this.guildCache, this.playerCache, CorePlugin.INSTANCE.getTaskDispatcher()));
  }

  private void registerAutoMessage() {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimerAsync(new AutoMessageTask(this.playerCache), 1, TimeUnit.MINUTES);
  }

  private void registerEnchantLimiter() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketEnchantLimiterSettingsUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.enchantLimiterSettingsUpdater);
  }

  private void registerAntiGrief() {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runTimer(new AntiGriefTask(this.placedBlockCache), 30L, TimeUnit.SECONDS);
  }

  private void registerArmor() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketArmorSettingsUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.armorSettingsUpdater);

    ArmorSettings armorSettings = this.armorSettingsCache.get();

    for (Material material : armorSettings.getDisabledMaterials()) {
      this.armorSettingsUpdater.disable(material);
    }
  }
}
