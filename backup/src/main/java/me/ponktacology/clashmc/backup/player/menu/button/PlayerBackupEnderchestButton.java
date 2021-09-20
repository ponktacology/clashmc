package me.ponktacology.clashmc.backup.player.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.menu.PlayerBackupEnderchestMenu;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class PlayerBackupEnderchestButton extends Button {


  private final BackupPlayer backupPlayer;

  private final PlayerBackup backup;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.ENDER_CHEST)
        .name("&eEnderchest")
        .lore( "&7Kliknij, aby wyświetlić enderchest gracza.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      new PlayerBackupEnderchestMenu(this.backupPlayer, this.backup).openMenu(player);
    }
  }
}
