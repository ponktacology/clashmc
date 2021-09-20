package me.ponktacology.loader.plugin.bootstrap;

import lombok.RequiredArgsConstructor;
import me.ponktacology.loader.plugin.BukkitPlugin;
import org.bukkit.plugin.java.JavaPlugin;


@RequiredArgsConstructor
public class PluginBootstrap extends JavaPlugin {


  private final BukkitPlugin plugin;

  @Override
  public void onEnable() {
    this.plugin.enable(this);
  }

  @Override
  public void onDisable() {
    this.plugin.disable();
  }
}
