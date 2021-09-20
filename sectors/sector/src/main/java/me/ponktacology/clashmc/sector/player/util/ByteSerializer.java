package me.ponktacology.clashmc.sector.player.util;

import lombok.experimental.UtilityClass;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@UtilityClass
public final class ByteSerializer {


  public static byte[] serializeItems(ItemStack... itemStacks) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      try (BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(stream)) {
        objectStream.writeObject(itemStacks);
      }
      return stream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Could not serialize itemstacks", e);
    }
  }


  public static ItemStack[] deserializeItems( byte[] itemStacks) {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(itemStacks)) {
      try (BukkitObjectInputStream objectStream = new BukkitObjectInputStream(stream)) {
        return (ItemStack[]) objectStream.readObject();
      }
    } catch ( IOException | ClassNotFoundException e) {
      throw new RuntimeException("Could not deserialize itemstacks", e);
    }
  }


  public static byte[] serializeItem(ItemStack itemStack) {
    try (ByteArrayOutputStream stream = new ByteArrayOutputStream()) {
      try (BukkitObjectOutputStream objectStream = new BukkitObjectOutputStream(stream)) {
        objectStream.writeObject(itemStack);
      }
      return stream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException("Could not serialize itemstack", e);
    }
  }


  public static ItemStack deserializeItem( byte[] itemStack) {
    try (ByteArrayInputStream stream = new ByteArrayInputStream(itemStack)) {
      try (BukkitObjectInputStream objectStream = new BukkitObjectInputStream(stream)) {
        return (ItemStack) objectStream.readObject();
      }
    } catch ( IOException | ClassNotFoundException e) {
      throw new RuntimeException("Could not deserialize itemstack", e);
    }
  }
}
