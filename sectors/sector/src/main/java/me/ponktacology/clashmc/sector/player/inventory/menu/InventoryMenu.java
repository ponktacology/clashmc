package me.ponktacology.clashmc.sector.player.inventory.menu;

import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.button.DisplayButton;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.BukkitInventory;
import me.ponktacology.clashmc.sector.player.inventory.PlayerInventoryUpdate;
import me.ponktacology.clashmc.sector.player.inventory.cache.InventoryCache;
import me.ponktacology.clashmc.sector.player.inventory.updater.InventoryUpdater;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class InventoryMenu extends Menu {

  private final InventoryUpdater inventoryUpdater = SectorPlugin.INSTANCE.getInventoryUpdater();
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
  private final InventoryCache inventoryCache = SectorPlugin.INSTANCE.getInventoryCache();


  private final BukkitPlayerWrapper player;

  private final BukkitInventory inventory;
  private final boolean ender;

  {
    setCanAddItems(true);
  }


  @Override
  public String getTitle(Player player) {
    return "&e" + (this.ender ? "Enderchest" : "Ekwipunek") + " gracza &f" + this.player.getName();
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();
    if (this.ender) {
      for (ItemStack itemStack : this.inventory.getEnderContentsItems()) {
        buttons.put(buttons.size(), new DisplayButton(itemStack, false));
      }
    } else {
      for (ItemStack itemStack : this.inventory.getInventoryContentsItems()) {
        buttons.put(buttons.size(), new DisplayButton(itemStack, false));
      }

      int index = 36;
      for (ItemStack itemStack : this.inventory.getArmorContentsItems()) {
        buttons.put(index++, new DisplayButton(itemStack, false));
      }
    }

    return buttons;
  }

  @Override
  public void onClose( Player player) {
    BukkitInventory inventory;
    if (this.ender) {
      inventory = new BukkitInventory(player.getOpenInventory().getTopInventory().getContents());
    } else {
      ItemStack[] menuContents = player.getOpenInventory().getTopInventory().getContents();
      ItemStack[] inventoryContents = new ItemStack[36];
      ItemStack[] armorContents = new ItemStack[4];

      System.arraycopy(menuContents, 0, inventoryContents, 0, 36);
      System.arraycopy(menuContents, 36, armorContents, 0, 4);

      inventory = new BukkitInventory(armorContents, inventoryContents);
    }

    InventoryUpdate inventoryUpdate = new InventoryUpdate(inventory);

    if (this.ender) {
      inventoryUpdate.setUpdateEnderchest(true);
    } else {
      inventoryUpdate.setUpdateInventory(true);
    }

    PlayerInventoryUpdate playerInventoryUpdate =
        new PlayerInventoryUpdate(this.player, inventoryUpdate);

    this.taskDispatcher.runAsync(
        () -> {
          Optional<Inventory> cachedInventoryOptional = this.inventoryCache.get(this.player);

          if (cachedInventoryOptional.isPresent()) {
            Inventory cachedInventory = cachedInventoryOptional.get();

            if (this.ender) {
              cachedInventory.setInventoryContents(cachedInventory.getInventoryContents());
              cachedInventory.setArmorContents(cachedInventory.getArmorContents());
            } else {
              cachedInventory.setArmorContents(cachedInventory.getEnderContents());
            }

            this.inventoryCache.add(this.player, cachedInventory);
          }

          this.inventoryUpdater.update(playerInventoryUpdate);
        });
  }

  @Override
  public int getSize() {
    return this.ender ? 27 : 45;
  }
}
