package me.ponktacology.clashmc.backup.util;

import lombok.experimental.UtilityClass;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.player.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


@UtilityClass
public class BackupUtil {


  public static PlayerBackup wrap( Player player, int rankChange) {
    Inventory inventory = PlayerUtil.wrapInventory(player);

    return new PlayerBackup(
        inventory,
        rankChange,
        player.spigot().getPing(),
        Bukkit.getServer().spigot().getTPS(),
        SectorPlugin.INSTANCE.getLocalSector());
  }


  public static PlayerBackup wrap( Player player) {
    return wrap(player, 0);
  }
}
