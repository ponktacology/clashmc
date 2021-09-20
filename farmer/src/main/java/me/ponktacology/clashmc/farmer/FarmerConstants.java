package me.ponktacology.clashmc.farmer;

import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FarmerConstants {

  public static final int PRICE_PER_PILLAR_FARMER_REGION = 3; // in emerald blocks
  public static final ItemStack FARMER_REGION_WAND =
      new ItemBuilder(Material.GOLD_AXE)
          .name("&eRódżka kopacza fosy")
          .lore(
              "",
              "&7Kliknij prawym jeden róg regionu.",
              "&7Kliknij lewym drugi róg regionu.",
              "&7Aby zatwierdzić użyj komendy&e",
              "&e/kopaczfosy start&7.")
          .build();
}
