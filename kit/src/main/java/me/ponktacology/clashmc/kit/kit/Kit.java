package me.ponktacology.clashmc.kit.kit;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.rank.Rank;
import org.bukkit.inventory.ItemStack;


import java.util.*;
import java.util.stream.Collectors;

@Data
@Entity(database = "kit", collection = "kits")
public class Kit {


  @SerializedName("_id")
  private final String name;

  private final Set<String> allowedRanks = new HashSet<>();
  private ItemStack icon;
  private ItemStack[] items;
  private long delay;
  private int index;
  private boolean enabled = true;

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public void addRank( Rank rank) {
    this.allowedRanks.add(rank.getName());
  }

  public void removeRank( Rank rank) {
    this.allowedRanks.remove(rank.getName());
  }

  public boolean isRankAllowed( Rank rank) {
    return this.allowedRanks.contains(rank.getName());
  }


  public List<ItemStack> getItems() {
    return Arrays.asList(this.items);
  }

  public Set<Rank> getAllowedRanks() {
    return this.allowedRanks.stream()
        .map(it -> CorePlugin.INSTANCE.getRankCache().get(it))
        .map(Optional::get)
        .filter(Objects::nonNull)
        .collect(Collectors.toSet());
  }
}
