package me.ponktacology.clashmc.sector.sector.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.sector.menu.button.SectorButton;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class SectorMenu extends Menu {


  private final SectorCache sectorCache;


  @Override
  public String getTitle(Player player) {
    return "&eLista sektorów";
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    buttons.put(
        0,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.BOOK)
                .name("&eStatystyki")
                .lore(
                    "&eOnline: &f"
                        + sectorCache.getNormalPlayersCount()
                        + "&e/&f"
                        + sectorCache.getGlobalMaxPlayers(),
                    "&eŚrednie TPS: &f" + sectorCache.getAverageGlobalTPS(),
                    "&eŚrednie użycie CPU: &f" + sectorCache.getAverageGlobalCPUUsage() + "%")
                .build();
          }

          @Override
          public boolean shouldUpdate(Player player, ClickType clickType) {
            return true;
          }
        });

    for (Sector sector : this.sectorCache.sortedValues()) {
      buttons.put(buttons.size(), new SectorButton(sector));
    }

    return buttons;
  }
}
