package me.ponktacology.clashmc.core.util;

import me.ponktacology.clashmc.core.CorePlugin;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;

public class PluginUtil {

  public static void registerListener(Listener listener) {
    Bukkit.getServer().getPluginManager().registerEvents(listener, CorePlugin.INSTANCE.getPlugin());
  }

  public static void callEvent(Event event) {
    Bukkit.getServer().getPluginManager().callEvent(event);
  }
}
