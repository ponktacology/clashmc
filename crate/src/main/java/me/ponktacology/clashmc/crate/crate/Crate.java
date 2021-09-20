package me.ponktacology.clashmc.crate.crate;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;



import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
@Setter
@Entity(collection = "crates", database = "crate")
public class Crate {


  @SerializedName("_id")
  private final String name;

  private final Set<CrateItem> items = new HashSet<>();

  private boolean enabled;

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public void addItem(CrateItem item) {
    this.items.add(item);
  }

  public void removeItem(CrateItem item) {
    this.items.remove(item);
  }

  public ItemStack getCrateItem() {
    return new ItemBuilder(Material.CHEST)
        .name("&fSkrzynka &6" + this.name)
        .lore( "&6Specjalna skrzynka &f" + this.name + "&6.", "&7Kliknij, aby otworzyć.")
        .build();
  }

  public ItemStack getCrateItem(int amount) {
    return new ItemBuilder(Material.CHEST)
            .amount(amount)
            .name("&fSkrzynka &6" + this.name)
            .lore( "&6Specjalna skrzynka &f" + this.name + "&6.", "&7Kliknij, aby otworzyć.")
            .build();
  }

  public CrateItem getItem(String name) {
    return this.items.stream()
        .filter(it -> it.getName().equalsIgnoreCase(name))
        .findFirst()
        .orElse(null);
  }

  
  public CrateItem getRandomItem() {
    double sum = this.items.stream().mapToDouble(CrateItem::getChance).sum();
    double rand = Math.random() * sum;

    CrateItem randomItem = null;

    for (CrateItem item : this.items) {
      rand -= item.getChance();

      if (rand <= 0) {
        randomItem = item;
        break;
      }
    }

    return randomItem;
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Crate that = (Crate) o;

    return this.getName().equals(that.getName());
  }
}
