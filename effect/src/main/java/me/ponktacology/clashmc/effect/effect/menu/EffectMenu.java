package me.ponktacology.clashmc.effect.effect.menu;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.effect.EffectPlugin;
import me.ponktacology.clashmc.effect.effect.Effect;
import me.ponktacology.clashmc.effect.effect.cache.EffectCache;
import me.ponktacology.clashmc.effect.effect.menu.button.EffectButton;
import org.bukkit.entity.Player;


import java.util.Map;

public class EffectMenu extends Menu {

  private final EffectCache effectCache = EffectPlugin.INSTANCE.getEffectCache();

  {
    setPlaceholder(true);
  }

  
  @Override
  public String getTitle(Player player) {
    return "&eEfekty";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newConcurrentMap();

    for (Effect effect : this.effectCache.values()) {
      buttons.put(effect.getIndex(), new EffectButton(effect));
    }

    return buttons;
  }
}
