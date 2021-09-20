package me.ponktacology.loader.plugin;

import com.google.common.collect.Sets;
import me.ponktacology.clashmc.api.Plugin;
import me.ponktacology.clashmc.api.module.Module;
import me.ponktacology.clashmc.api.service.Service;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;

public interface BukkitPlugin extends Plugin {

  Set<Service> services = Sets.newConcurrentHashSet();
  Set<Module> modules = Sets.newConcurrentHashSet();

  void enable(JavaPlugin plugin);

  default void disable() {}

  default void register(Service service) {
    services.add(service);
  }

  default void register(Module module) {
    modules.add(module);
  }
}
