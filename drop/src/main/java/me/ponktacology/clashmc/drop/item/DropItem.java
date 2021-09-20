package me.ponktacology.clashmc.drop.item;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Setter
@Entity(database = "drop", collection = "items")
public class DropItem implements Serializable {


  @SerializedName("_id")
  private final String name;

  private final List<Material> allowedPickaxes = new ArrayList<>();

  private String displayName;
  private ItemStack item;
  private boolean fortune;
  private double chance;
  private int dropBelowY;
  private int index;

  private DropType type = DropType.STONE;

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public double getChance( DropPlayer dropPlayer) {
    double multiplier = dropPlayer.getDropMultiplier();
    double bonus = dropPlayer.getDropData(this).getBonus();
    double chance = (this.chance + bonus);

    return chance + (chance * multiplier);
  }

  public void addPickaxe(Material material) {
    this.allowedPickaxes.add(material);
  }

  public void removePickaxe(Material material) {
    this.allowedPickaxes.remove(material);
  }

  public boolean canMine(Material material) {
    return this.allowedPickaxes.contains(material);
  }

  public ItemStack getItem() {
    return this.item.clone();
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    DropItem dropItem = (DropItem) o;

    return name != null ? name.equals(dropItem.name) : dropItem.name == null;
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
