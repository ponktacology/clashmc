package me.ponktacology.clashmc.core.log.command.menu.button;

import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.log.command.CommandLog;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class CommandLogButton extends Button {

  private final CommandLog commandLog;

  public CommandLogButton(CommandLog chatLog) {
    this.commandLog = chatLog;
  }

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.PAPER)
        .name("&f" + commandLog.getCommand())
        .lore("&eData: &f" + TimeUtil.formatTimeMillisToDate(commandLog.getTimeStamp()))
        .build();
  }
}
