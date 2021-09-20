package me.ponktacology.clashmc.auth;

import lombok.Getter;
import me.ponktacology.clashmc.auth.player.AuthPlayer;
import me.ponktacology.clashmc.auth.player.adapter.AuthPlayerParameterAdapter;
import me.ponktacology.clashmc.auth.player.cache.AuthPlayerCache;
import me.ponktacology.clashmc.auth.player.command.AuthCommand;
import me.ponktacology.clashmc.auth.player.factory.AuthPlayerFactory;
import me.ponktacology.clashmc.auth.player.listener.PlayerChatListener;
import me.ponktacology.clashmc.auth.player.listener.PlayerJoinListener;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum AuthPlugin implements BukkitPlugin {
  INSTANCE;

  private JavaPlugin plugin;
  private final AuthPlayerFactory playerFactory =
      new AuthPlayerFactory();
  private final AuthPlayerCache playerCache =
      new AuthPlayerCache(playerFactory);

  @Override
  public void enable(JavaPlugin plugin) {
    this.plugin = plugin;
    Bukkit.getMessenger().registerOutgoingPluginChannel(this.plugin, "BungeeCord");
    PluginUtil.registerListener(
        new PlayerJoinListener(
            this.playerFactory, this.playerCache, CorePlugin.INSTANCE.getTaskDispatcher()));
    PluginUtil.registerListener(new PlayerChatListener(CorePlugin.INSTANCE.getPlayerCache()));
    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("auth")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter()).helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(AuthPlayer.class, new AuthPlayerParameterAdapter(this.playerCache))
        .build()
        .register(new AuthCommand(this.playerCache));
  }

  @Override
  public void disable() {}
}
