package me.ponktacology.clashmc.safe.item;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;


import java.util.UUID;

@ToString
@Getter
@Setter
@Entity(collection = "items", database = "safe")
public class SafeItem {

  // random id
  @SerializedName("_id")
  private final UUID uuid = UUID.randomUUID();

  private final ItemStack item;
  private String name;
  private int limit;
  private int index;

  public SafeItem(String name, ItemStack item) {
    this.name = name;
    this.item = item;
    this.name = name;
  }

  public ItemStack getItem() {
    return this.item;
  }

  public String getRawName() {
    return ChatColor.stripColor(Text.colored(this.name));
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  @Override
  public boolean equals( Object other) {
    if (other == null) return false;
    if (!(other instanceof SafeItem)) return false;
    SafeItem safeItem = (SafeItem) other;

    return safeItem.getName().equals(this.getName());
  }
}
