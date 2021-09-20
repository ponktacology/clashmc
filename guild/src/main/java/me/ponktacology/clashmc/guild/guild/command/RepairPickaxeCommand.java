package me.ponktacology.clashmc.guild.guild.command;

import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class RepairPickaxeCommand {

  private static final int EXP_TO_LEVEL_30 = getExpToLevelUp(30);

  @Command(value = "naprawkilof", description = "Naprawia kilof za 30 lvl")
  public void execute(@Sender Player sender) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand == null || itemInHand.getType() != Material.DIAMOND_PICKAXE) {
      sender.sendMessage(Text.colored("&cMożesz naprawić tylko diamentowy kilof."));
      return;
    }

    if (!InventoryUtil.hasItem(sender, Material.GOLD_INGOT, 8)) {
      sender.sendMessage(Text.colored("&cNaprawa kilofa kosztuję 8 sztabek złota."));
      return;
    }

    InventoryUtil.removeItem(sender, Material.GOLD_INGOT, 8);
    itemInHand.setDurability((short) 0);
    sender.sendMessage(Text.colored("&aPomyślnie naprawiono kilof."));
  }

  public static int getExpToLevelUp(int level) {
    if (level <= 15) {
      return 2 * level + 7;
    } else if (level <= 30) {
      return 5 * level - 38;
    } else {
      return 9 * level - 158;
    }
  }
}
