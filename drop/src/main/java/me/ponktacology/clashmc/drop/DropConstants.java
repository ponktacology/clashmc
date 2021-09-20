package me.ponktacology.clashmc.drop;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DropConstants {

  public static final int MAX_DROP_LEVEL = 10;
  public static final ItemStack COBBLEX_ITEM =
      new ItemBuilder(Material.MOSSY_COBBLESTONE).name("&dCobbleX").build();
  public static final ItemStack COBBLEX_PRICE = new ItemStack(Material.COBBLESTONE, 9 * 64);
}
