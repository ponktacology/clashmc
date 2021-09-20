package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
public class TitleUtil {

  public static void sendTitleAndSubtitle(
      String title, String subtitle, int fadeIn, int duration, int fadeOut) {
    sendTitleAndSubtitle(
        Bukkit.getServer().getOnlinePlayers().stream()
            .filter(Objects::nonNull)
            .collect(Collectors.toList()),
        Text.colored(title),
        Text.colored(subtitle),
        fadeIn,
        duration,
        fadeOut);
  }

  public static void sendTitle( Player player, String title, int fadeIn, int duration, int fadeOut) {
    sendTitleAndSubtitle(player, title, "", fadeIn, duration, fadeOut);
  }

  public static void sendSubtitle(
           Player player, String subtitle, int fadeIn, int duration, int fadeOut) {
    sendTitleAndSubtitle(player, "", subtitle, fadeIn, duration, fadeOut);
  }

  public static void sendTitleAndSubtitle(
           List<Player> players, String title, String subtitle, int fadeIn, int duration, int fadeOut) {
    for (Player player : players) {
      sendTitleAndSubtitle(player, title, subtitle, fadeIn, duration, fadeOut);
    }
  }

  public static void sendTitleAndSubtitle(
           Player player, String title, String subtitle, int fadeIn, int duration, int fadeOut) {
    player.sendTitle(new Title(Text.colored(title), Text.colored(subtitle), fadeIn, duration, fadeOut));
  }
}
