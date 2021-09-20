package me.ponktacology.clashmc.backup.player.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.menu.PlayerBackupManageMenu;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PlayerBackupButton extends Button {


  private final BackupPlayer backupPlayer;

  private final PlayerBackup backup;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.BOOK)
        .name("&e" + this.backup.getFormattedDate())
        .lore(
            "",
            "&eSektor: &f" + this.backup.getSector(),
            "&ePing: " + StyleUtil.colorPing(this.backup.getPing()),
            "&eTPS: &f"
                + Arrays.stream(this.backup.getTps())
                    .map(it -> MathUtil.roundOff(it, 1))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", ")),
            "&ePrzywrócono ekwipunek: "
                + StyleUtil.convertBooleanToText(this.backup.isInventoryRestored()),
            "&ePrzywrócono ender: " + StyleUtil.convertBooleanToText(this.backup.isEnderRestored()),
            "&ePrzywrócono ranking: "
                + StyleUtil.convertBooleanToText(this.backup.isRankRestored()))
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      new PlayerBackupManageMenu(this.backupPlayer, this.backup).openMenu(player);
    }
  }
}
