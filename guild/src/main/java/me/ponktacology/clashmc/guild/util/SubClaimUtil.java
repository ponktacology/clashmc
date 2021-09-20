package me.ponktacology.clashmc.guild.util;

import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;


import java.util.Optional;

public class SubClaimUtil implements Listener {

  private static final BlockFace[] SIDES =
      new BlockFace[] {
        BlockFace.UP, BlockFace.NORTH, BlockFace.SOUTH, BlockFace.WEST, BlockFace.EAST
      };

  public static boolean canAccess(
           Block block,  Player player, GuildPlayer guildPlayer,  Guild guild) {
    if (block == null) return true;
    switch (block.getType()) {
      case FURNACE:
      case HOPPER:
      case TRAPPED_CHEST:
      case CHEST:
        Sign sign = null;

        for (BlockFace side : SIDES) {
          Block b = block.getRelative(side);
          if (b.getType() == Material.WALL_SIGN) {
            sign = (Sign) b.getState();
            break;
          }
        }

        if (sign == null) {
          return true;
        }

        if (sign.getLines().length < 2) {
          return true;
        }

        if (!ChatColor.stripColor(sign.getLine(0)).equals("[Ochrona]")) {
          return true;
        }

        boolean hasAcces = false;

        Optional<CorePlayer> corePlayerOptional =
            CorePlugin.INSTANCE.getPlayerCache().get(player);

        if (!corePlayerOptional.isPresent()) {
          return false;
        }

        CorePlayer corePlayer = corePlayerOptional.get();

        for (int i = 1; i < sign.getLines().length; i++) {
          if (sign.getLine(i).equalsIgnoreCase(corePlayer.getName())) {
            hasAcces = true;
            break;
          }
        }

        if (!hasAcces) {
          return guild.hasPermission(guildPlayer, GuildPermission.Permissions.PRIVATE_CHEST_ACCESS);
        }

        return true;
      default:
        return true;
    }
  }

  public static boolean checkForPrivate(
           Block block,  Player player, GuildPlayer guildPlayer,  Guild guild) {
    for (BlockFace face : SIDES) {
      Block b = block.getRelative(face);

      if (!canAccess(b, player, guildPlayer, guild)) {
        return true;
      }
    }

    return false;
  }
}
