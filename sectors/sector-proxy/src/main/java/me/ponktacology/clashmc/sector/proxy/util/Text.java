package me.ponktacology.clashmc.sector.proxy.util;

import net.md_5.bungee.api.ChatColor;


public class Text {


  public static String colored( String message) {
    return ChatColor.translateAlternateColorCodes('&', message);
  }
}
