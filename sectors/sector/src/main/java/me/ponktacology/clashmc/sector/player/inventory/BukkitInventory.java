package me.ponktacology.clashmc.sector.player.inventory;

import me.ponktacology.clashmc.sector.api.player.inventory.Inventory;
import me.ponktacology.clashmc.sector.player.util.ByteSerializer;
import org.bukkit.inventory.ItemStack;


public class BukkitInventory extends Inventory {

  public BukkitInventory( Inventory inventory) {
    this.setInventoryContents(inventory.getInventoryContents());
    this.setArmorContents(inventory.getArmorContents());
    this.setEnderContents(inventory.getEnderContents());
  }

  public BukkitInventory(ItemStack[] enderContents) {
    this.setEnderContents(ByteSerializer.serializeItems(enderContents));
  }

  public BukkitInventory(ItemStack[] armorContents, ItemStack[] inventoryContents) {
    this.setArmorContents(ByteSerializer.serializeItems(armorContents));
    this.setInventoryContents(ByteSerializer.serializeItems(inventoryContents));
  }


  public ItemStack[] getInventoryContentsItems() {
    return ByteSerializer.deserializeItems(super.getInventoryContents());
  }


  public ItemStack[] getArmorContentsItems() {
    return ByteSerializer.deserializeItems(super.getArmorContents());
  }


  public ItemStack[] getEnderContentsItems() {
    return ByteSerializer.deserializeItems(super.getEnderContents());
  }
}
