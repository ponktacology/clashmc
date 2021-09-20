package me.ponktacology.loader.plugin;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.loader.configuration.LoaderConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@Getter
@Slf4j
public final class SecurePlugin {

  private static final String URL = "http://144.76.91.143:2137/product/";

  private final String name;
  private final LoaderConfiguration configuration;
  private final JavaPlugin plugin;

  public SecurePlugin(String name, LoaderConfiguration configuration, JavaPlugin plugin) {
    this.name = name;
    this.configuration = configuration;
    this.plugin = plugin;

    try {
      init();
    } catch (Exception e) {
      log.info("Invalid API key");
      // Bukkit.getServer().shutdown();
      e.printStackTrace();
    }
  }

  public void init() throws Exception {
    File file = download();

    PluginManager pluginManager = this.plugin.getServer().getPluginManager();
    Plugin plugin = pluginManager.loadPlugin(file);
    pluginManager.enablePlugin(plugin);
  }

  public void enable() {
    this.plugin.getServer().getPluginManager().enablePlugin(plugin);
  }

  public void disable() {
    this.plugin.getServer().getPluginManager().isPluginEnabled(plugin);
  }

  public File download() throws IOException {
    return new SecureDownloader(URL + this.name + "/" + this.configuration.getKey()).download();
  }

  public boolean isEnabled() {
    return plugin != null && plugin.isEnabled();
  }
}
