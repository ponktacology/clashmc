package me.ponktacology.clashmc.drop;

import lombok.Getter;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.data.mongo.MongoDataService;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.crate.CratePlugin;
import me.ponktacology.clashmc.drop.generator.cache.GeneratorCache;
import me.ponktacology.clashmc.drop.generator.factory.GeneratorFactory;
import me.ponktacology.clashmc.drop.generator.listener.GeneratorListener;
import me.ponktacology.clashmc.drop.generator.listener.GeneratorMenuListener;
import me.ponktacology.clashmc.drop.generator.recipe.GeneratorRecipe;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.adapter.DropItemParameterAdapter;
import me.ponktacology.clashmc.drop.item.adapter.DropTypeParameterAdapter;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.command.DropCommand;
import me.ponktacology.clashmc.drop.item.factory.DropItemFactory;
import me.ponktacology.clashmc.drop.item.listener.CobbleXListener;
import me.ponktacology.clashmc.drop.item.listener.ItemDropListener;
import me.ponktacology.clashmc.drop.item.updater.DropItemUpdater;
import me.ponktacology.clashmc.drop.item.updater.packet.PacketDropItemRemove;
import me.ponktacology.clashmc.drop.item.updater.packet.PacketDropItemUpdate;
import me.ponktacology.clashmc.drop.menu.DropMenuFactory;
import me.ponktacology.clashmc.drop.player.DebugCommand;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.adapter.DropPlayerParameterAdapter;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.player.factory.DropPlayerFactory;
import me.ponktacology.clashmc.drop.player.listener.PlayerDataListener;
import me.ponktacology.clashmc.drop.turbo.command.TurboCommand;
import me.ponktacology.clashmc.drop.turbo.global.cache.GlobalTurboCache;
import me.ponktacology.clashmc.drop.turbo.global.factory.GlobalTurboFactory;
import me.ponktacology.clashmc.drop.turbo.global.updater.GlobalTurboUpdater;
import me.ponktacology.clashmc.drop.turbo.global.updater.packet.PacketGlobalTurboUpdate;
import me.ponktacology.clashmc.drop.turbo.player.updater.PlayerTurboUpdater;
import me.ponktacology.clashmc.drop.turbo.player.updater.packet.PacketPlayerTurboUpdate;
import me.ponktacology.clashmc.drop.turbo.task.TurboTask;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.recipe.cache.RecipeCache;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.clashmc.safe.SafePlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Getter
public enum DropPlugin implements BukkitPlugin {
  INSTANCE;

  private final MongoDataService dataService = CorePlugin.INSTANCE.getDataService();
  private final NetworkService networkService = CorePlugin.INSTANCE.getNetworkService();
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
  private final GeneratorCache generatorCache =
      new GeneratorCache();
  private final GeneratorFactory generatorFactory = new GeneratorFactory(this.dataService);
  private final DropItemCache dropItemCache = new DropItemCache();
  private final DropItemFactory dropItemFactory = new DropItemFactory(this.dataService);
  private final DropItemUpdater dropItemUpdater =
      new DropItemUpdater(this.networkService, this.dropItemFactory, this.dropItemCache);
  private final DropPlayerFactory playerFactory = new DropPlayerFactory();
  private final DropPlayerCache playerCache =
      new DropPlayerCache(this.playerFactory);
  private final DropMenuFactory menuFactory =
      new DropMenuFactory(
          CratePlugin.INSTANCE.getCrateCache(),
          this.dropItemCache,
          this.playerCache,
          this.taskDispatcher);
  private final GlobalTurboFactory globalTurboFactory = new GlobalTurboFactory(this.dataService);
  private final GlobalTurboCache globalTurboCache = new GlobalTurboCache(this.globalTurboFactory);
  private final GlobalTurboUpdater globalTurboUpdater =
      new GlobalTurboUpdater(
          this.dataService, this.networkService, this.globalTurboCache, this.globalTurboFactory);
  private final PlayerTurboUpdater playerTurboUpdater =
      new PlayerTurboUpdater(
          this.networkService, this.playerCache, CorePlugin.INSTANCE.getPlayerCache());

  @Override
  public void enable(JavaPlugin plugin) {
    this.registerGenerators();
    this.registerItems();
    this.registerPlayer();
    this.registerTurbo();

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("drop")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(DropItem.class, new DropItemParameterAdapter(this.dropItemCache))
        .bind(DropType.class, new DropTypeParameterAdapter())
        .bind(DropPlayer.class, new DropPlayerParameterAdapter(this.playerCache))
        .build()
        .register(
            new DebugCommand(
                GuildPlugin.INSTANCE.getPlayerCache(),
                CorePlugin.INSTANCE.getPlayerCache(),
                DropPlugin.INSTANCE.getPlayerCache(),
                CratePlugin.INSTANCE.getPlayerCache(),
                SafePlugin.INSTANCE.getPlayerCache()))
        .register(
            new DropCommand(
                this.dropItemFactory, this.dropItemUpdater, this.dropItemCache, this.menuFactory))
        .register(
            new TurboCommand(
                this.globalTurboCache, this.globalTurboUpdater, this.playerTurboUpdater));

    RecipeCache recipeCache = GuildPlugin.INSTANCE.getRecipeCache();
    recipeCache.add(new GeneratorRecipe());
  }

  @Override
  public void disable() {}

  private void registerGenerators() {
    PluginUtil.registerListener(
        new GeneratorListener(this.generatorCache, this.generatorFactory, taskDispatcher));
    PluginUtil.registerListener(
        new GeneratorMenuListener(this.generatorCache, this.playerCache, this.menuFactory));

    this.generatorCache.addAll(this.generatorFactory.loadAll());
  }

  private void registerItems() {
    PluginUtil.registerListener(new ItemDropListener(this.playerCache, this.dropItemCache));
    PluginUtil.registerListener(new CobbleXListener(this.dropItemCache, this.playerCache));

    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketDropItemUpdate.class);
    packetCache.add(PacketDropItemRemove.class);

    this.dropItemCache.addAll(this.dropItemFactory.loadAll());

    CorePlugin.INSTANCE.getNetworkService().subscribe(this.dropItemUpdater);
  }

  private void registerPlayer() {
    PluginUtil.registerListener(
        new PlayerDataListener(this.taskDispatcher, this.playerCache, this.playerFactory));
    CorePlugin.INSTANCE.getPacketCache().add(PacketPlayerTurboUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.playerTurboUpdater);
  }

  private void registerTurbo() {
    CorePlugin.INSTANCE.getPacketCache().add(PacketGlobalTurboUpdate.class);
    CorePlugin.INSTANCE.getNetworkService().subscribe(this.globalTurboUpdater);

    this.taskDispatcher.runTimerAsync(new TurboTask(this.playerCache), 1L, TimeUnit.SECONDS);
  }
}
