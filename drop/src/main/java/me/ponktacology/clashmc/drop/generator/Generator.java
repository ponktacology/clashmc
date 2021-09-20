package me.ponktacology.clashmc.drop.generator;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;



import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
@Setter
@Entity(collection = "generators", database = "drop")
public class Generator implements Serializable {

  
  public static final Generator BASE_GENERATOR = new Generator(null);


  @SerializedName("_id")
  private final Location location;

  private int level;
  private int usage;

  public void regen( Block block) {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runLater(
            () -> {
              this.usage++;

              block.setType(Material.STONE);
            },
            2000L - (300L * this.level),
            TimeUnit.MILLISECONDS);
  }

  public boolean isMaxLevel() {
    return this.level >= 4;
  }

  public int getUpgradePrice() {
    return ((this.level + 1) * 500);
  }

  public void increaseLevel() {
    this.level++;
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public ItemStack getItem() {
    return new ItemBuilder(Material.STONE)
        .enchantment(Enchantment.DURABILITY, 10)
        .name("&7Generator")
        .lore(
            "",
            "&ePoziom: &f" + this.level,
            "&7Kliknij prawym, aby ulepszyć.",
            "&eGenerator mozesz zniszczyć złotym kilofem.")
        .build();
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Generator generator = (Generator) o;

    return location != null ? location.equals(generator.location) : generator.location == null;
  }

  @Override
  public int hashCode() {
    return location != null ? location.hashCode() : 0;
  }
}
