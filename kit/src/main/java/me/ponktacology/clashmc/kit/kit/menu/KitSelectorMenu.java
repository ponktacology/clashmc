package me.ponktacology.clashmc.kit.kit.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.menu.button.KitButton;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class KitSelectorMenu extends Menu {

  
  private final KitCache kitCache;
  
  private final KitPlayerCache kitPlayerCache;

  {
    setPlaceholder(true);
  }

  
  @Override
  public String getTitle(Player player) {
    return "&eKity";
  }

  
  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    for (Kit kit : this.kitCache.values()) {
      buttons.put(kit.getIndex(), new KitButton(kit, this.kitPlayerCache, this.kitCache));
    }

    buttons.put(26, this.getPlaceholderButton());

    return buttons;
  }
}
