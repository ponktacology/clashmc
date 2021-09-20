package me.ponktacology.clashmc.effect.effect.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.effect.effect.Effect;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class EffectButton extends Button {

  
  private final Effect effect;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(this.effect.getIcon())
        .name(this.effect.getDisplayName())
        .lore(
            "",
            "&eKoszt: &f" + this.effect.getPrice() + " &abloków szmaragdu",
            "&eCzas: &f" + TimeUtil.formatTimeMillis(this.effect.getEffect().getDuration() * 50L),
            "&7Kliknij, aby zakupić.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (!clickType.isLeftClick()) {
      return;
    }

    if (!InventoryUtil.hasItem(player, Material.EMERALD_BLOCK, this.effect.getPrice())) {
      player.sendMessage(Text.colored("&cNie posiadasz wymaganych przedmiotów."));
      return;
    }

    InventoryUtil.removeItem(player, Material.EMERALD_BLOCK, this.effect.getPrice());
    this.effect.apply(player);

    player.sendMessage(Text.colored("&aPomyślnie zakupiłeś efekt."));
  }
}
