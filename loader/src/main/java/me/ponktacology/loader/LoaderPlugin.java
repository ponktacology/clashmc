package me.ponktacology.loader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.ponktacology.clashmc.api.serializer.gson.GsonSerializer;
import me.ponktacology.loader.configuration.LoaderConfiguration;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.ponktacology.loader.plugin.SecurePlugin;
import me.ponktacology.loader.plugin.manager.SecurePluginManager;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public enum LoaderPlugin implements BukkitPlugin {

  INSTANCE;

  private final Gson configurationSerializerGson =
      new GsonBuilder().serializeNulls().setPrettyPrinting().disableHtmlEscaping().create();
  private final GsonSerializer configurationSerializer =
      new GsonSerializer(configurationSerializerGson);
  private final LoaderConfiguration configuration =
      new LoaderConfiguration(this.configurationSerializer);
  private final SecurePluginManager pluginManager = new SecurePluginManager();

  @Override
  public void enable(JavaPlugin plugin) {
    for(String name : this.configuration.getPlugins()) {
      this.pluginManager.addPlugin(new SecurePlugin(name, this.configuration, plugin));
    }
  }

  @Override
  public void disable() {
    this.pluginManager.getPlugins().forEach(SecurePlugin::disable);
  }
}
