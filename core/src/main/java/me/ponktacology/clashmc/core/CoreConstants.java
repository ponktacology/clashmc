package me.ponktacology.clashmc.core;

import java.util.concurrent.TimeUnit;

public class CoreConstants {
  public static final String DEFAULT_RANK_NAME = "Gracz";
  public static final int MAX_LOG_COUNT = 1000;
  public static final long REQUEST_DELAY_TIME = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES);
  public static final long TAB_COMPLETE_DELAY_TIME =
      TimeUnit.MILLISECONDS.convert(1, TimeUnit.SECONDS);

  public static final String[] BLOCKED_COMMANDS =
      new String[] {
        "/minecraft:me",
        "/bukkit:?",
        "/?",
        "/icanhasbukkit",
        "/bukkit:me",
        "/plugins",
        "/pl",
        "/about",
        "/bukkit:pl",
        "/bukkit:plugins",
        "/ver",
        "/version",
        "/bukkit:version",
        "/bukkit:ver",
        "/me",
        "/bukkit:me",
        "/spigot:me",
        "/bukkit:help",
        "//calc",
        "//solve",
        "/worldedit:/calc",
        "/worldedit:/solve"
      };
}
