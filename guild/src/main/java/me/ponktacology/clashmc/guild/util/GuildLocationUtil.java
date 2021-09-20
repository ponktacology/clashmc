package me.ponktacology.clashmc.guild.util;

import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import net.jafama.FastMath;
import org.bukkit.Location;

public class GuildLocationUtil {

  public static double distanceToClosestGuild(Location location) {
    double closestDistance = Double.MAX_VALUE;
    Location location1 = location.clone();

    location1.setY(0);

    for (Guild guild : GuildPlugin.INSTANCE.getGuildCache().guildsOnThisSector()) {
      Location location2 = guild.getCenter().clone();
      location2.setY(0);
      double distance = location1.distanceSquared(location2);
      if (location1.distanceSquared(location2) < closestDistance) {
        closestDistance = distance;
      }
    }

    return FastMath.sqrtQuick(closestDistance);
  }
}
