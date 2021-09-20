package me.ponktacology.clashmc.guild.enchantmentlimiter;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum EnchantType {

  DIAMOND("Diamentowe", Material.DIAMOND_SWORD),
  IRON("Żelazne", Material.IRON_SWORD),
  OTHER("Inne", Material.WOOD_SWORD);

  private final String displayName;
  private final Material icon;

  public static EnchantType from(ItemStack itemStack) {
    return itemStack == null
        ? EnchantType.OTHER
        : Arrays.stream(EnchantType.values())
            .filter(it -> itemStack.getType().toString().contains(it.toString()))
            .findFirst()
            .orElse(EnchantType.OTHER);
  }
}
