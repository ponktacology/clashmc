package me.ponktacology.clashmc.core.player.command;

import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;


public class RepairCommand {

  @Command(value = "repair", description = "Naprawia przedmiot aktualnie trzymany w ręce")
  @Permission(CorePermissions.REPAIR)
  public void execute( @Sender Player sender) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand.getType().getMaxDurability() > 0) {
      itemInHand.setDurability((short) 0);
      sender.sendMessage(Text.colored("&aNaprawiono przedmiot."));
    } else sender.sendMessage(Text.colored("&cNie możesz naprawic tego przedmiotu."));
  }

  @Command(
      value = {"repairall", "repair all"},
      description = "Naprawia wszystkie przedmioty w ekwipunku")
  @Permission(CorePermissions.REPAIR_ALL)
  public void repairAll( @Sender Player sender) {
    PlayerInventory inventory = sender.getInventory();
    for (ItemStack content : inventory.getContents()) {
      if (content != null
          && content.getType() != null
          && content.getEnchantmentLevel(Enchantment.DIG_SPEED) < 6
          && content.getType().getMaxDurability() > 0) {
        content.setDurability((short) 0);
      }
    }

    for (ItemStack content : inventory.getArmorContents()) {
      if (content != null && content.getType().getMaxDurability() > 0) {
        content.setDurability((short) 0);
      }
    }

    sender.sendMessage(Text.colored("&aNaprawiono przedmioty."));
  }
}
