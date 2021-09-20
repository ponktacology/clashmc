package me.ponktacology.clashmc.farmer;

import lombok.Getter;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.farmer.farmer.cache.FarmerCache;
import me.ponktacology.clashmc.farmer.farmer.command.FarmerCommand;
import me.ponktacology.clashmc.farmer.farmer.listener.FarmerPlaceListener;
import me.ponktacology.clashmc.farmer.farmer.listener.FarmerRegionWandListener;
import me.ponktacology.clashmc.farmer.farmer.listener.ForceCompleteFarmerListener;
import me.ponktacology.clashmc.farmer.farmer.listener.PlayerDataListener;
import me.ponktacology.clashmc.farmer.farmer.recipe.ObsidianFarmerRecipe;
import me.ponktacology.clashmc.farmer.farmer.recipe.SandFarmerRecipe;
import me.ponktacology.clashmc.farmer.player.cache.FarmerPlayerCache;
import me.ponktacology.clashmc.farmer.player.factory.FarmerPlayerFactory;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum FarmerPlugin implements BukkitPlugin {
  INSTANCE;

  private JavaPlugin plugin;
  private final FarmerCache farmerCache = new FarmerCache();
  private final FarmerPlayerFactory playerFactory = new FarmerPlayerFactory();
  private final FarmerPlayerCache playerCache = new FarmerPlayerCache(this.playerFactory);

  @Override
  public void enable(JavaPlugin plugin) {
    this.plugin = plugin;

    this.registerFarmers();

    PluginUtil.registerListener(new PlayerDataListener(this.playerCache, this.playerFactory));
    PluginUtil.registerListener(
        new FarmerPlaceListener(
            GuildPlugin.INSTANCE.getPlayerCache(),
            GuildPlugin.INSTANCE.getRecipeCache(),
            CorePlugin.INSTANCE.getTaskDispatcher()));
    PluginUtil.registerListener(
        new FarmerRegionWandListener(this.playerCache, GuildPlugin.INSTANCE.getPlayerCache()));
    PluginUtil.registerListener(new ForceCompleteFarmerListener(this.farmerCache));

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("farmers")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .build()
        .register(
            new FarmerCommand(
                GuildPlugin.INSTANCE.getPlayerCache(), this.playerCache, this.farmerCache));
  }

  private void registerFarmers() {
    GuildPlugin.INSTANCE.getRecipeCache().add(new SandFarmerRecipe());
    GuildPlugin.INSTANCE.getRecipeCache().add(new ObsidianFarmerRecipe());
  }
}
