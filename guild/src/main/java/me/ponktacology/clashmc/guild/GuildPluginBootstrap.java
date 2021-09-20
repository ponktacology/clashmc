package me.ponktacology.clashmc.guild;

import org.bukkit.plugin.java.JavaPlugin;

public final class GuildPluginBootstrap extends JavaPlugin {

  @Override
  public void onEnable() {
    GuildPlugin.INSTANCE.enable(this);
  }

  @Override
  public void onDisable() {
    GuildPlugin.INSTANCE.disable();
  }
}
