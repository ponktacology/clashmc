package me.ponktacology.loader.plugin.bootstrap;

import lombok.RequiredArgsConstructor;
import me.ponktacology.loader.LoaderPlugin;
import org.bukkit.plugin.java.JavaPlugin;


@RequiredArgsConstructor
public class LoaderBootstrap extends JavaPlugin {

  
  private final LoaderPlugin plugin;

  @Override
  public void onLoad() {
    this.plugin.enable(this);
  }

  @Override
  public void onDisable() {
    this.plugin.disable();
  }
}
