package me.ponktacology.clashmc.guild.util;

import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


public class GuildItemsUtil {

  public static boolean hasEnoughItems( GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();

    if (player == null) return false;

    CorePlayer corePlayer = CorePlugin.INSTANCE.getPlayerCache().getOrKick(player);

    double multiplier = guildItemsMultiplier(corePlayer);

    GuildSettings guildSettings = GuildPlugin.INSTANCE.getGuildSettingsCache().get();

    return InventoryUtil.hasItems(player, guildSettings.getItems(multiplier));
  }

  public static int getRemainingItems( CorePlayer corePlayer,  ItemStack item) {
    Player player = corePlayer.getPlayer();

    if (player == null) return -1;

    double multiplier = guildItemsMultiplier(corePlayer);

    return Math.max(
        Math.max((int) (item.getAmount() * multiplier), 1) - InventoryUtil.countItems(player, item),
        0);
  }

  public static double guildItemsMultiplier( CorePlayer corePlayer) {
    double multiplier = 1.0;

    if (corePlayer.hasPermission("svip")) {
      multiplier = 0.5;
    } else if (corePlayer.hasPermission("vip")) {
      multiplier = 0.75;
    }

    return multiplier;
  }
}
