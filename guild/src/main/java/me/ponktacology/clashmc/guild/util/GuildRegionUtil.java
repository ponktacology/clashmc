package me.ponktacology.clashmc.guild.util;

import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.sector.SectorConstants;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class GuildRegionUtil {

  public static boolean availableForGuildCreation(
          Player sender, Location location) {
    return availableForGuildCreation(sender, location, true);

  }
  public static boolean availableForGuildCreation(
      Player sender, Location location, boolean inform) {
    if (Math.abs(location.getBlockX()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD
        && Math.abs(location.getBlockZ()) <= SectorConstants.MIN_DISTANCE_FROM_SPAWN_ALLOW_BUILD) {
      if (inform) sender.sendMessage(Text.colored("&cJesteś za blisko spawna."));
      return false;
    }

    if (RegionUtil.distanceToBorder(SectorPlugin.INSTANCE.getLocalSector(), location)
        < GuildConstants.MIN_DISTANCE_GUILD_CREATE) {
      if (inform) sender.sendMessage(Text.colored("&cJesteś za blisko granicy sektora."));
      return false;
    }

    if (GuildLocationUtil.distanceToClosestGuild(location)
        < (GuildConstants.MAX_GUILD_REGION_SIZE * 2) + 10) {
      if (inform) sender.sendMessage(Text.colored("&cJesteś za blisko innej gildii."));
      return false;
    }

    if (!RegionUtil.isIn(location, SectorPlugin.INSTANCE.getLocalSector())) {
      if (inform)
        sender.sendMessage(Text.colored("&cNie możesz założyć gildii poza granicami sektora."));
      return false;
    }

    return true;
  }

  public static Location getRandomLocationInSector(Sector sector) {
    boolean found = false;
    Location location;
    do {
      location = RegionUtil.getRandomLocationInSector(sector);

      if (!GuildPlugin.INSTANCE.getGuildCache().getByLocation(location).isPresent()) {
        found = true;
      }
    } while (!found);

    return location;
  }
}
