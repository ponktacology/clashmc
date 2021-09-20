package me.ponktacology.clashmc.effect.effect;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;


@Data
@Entity(database = "effect", collection = "effects")
public class Effect {

  @SerializedName("_id")
  private final String name;

  private String displayName;
  private ItemStack icon;
  private PotionEffect effect;
  private int price;
  private int index;

  public ItemStack getIcon() {
    return this.icon.clone();
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public void apply( Player player) {
    player.addPotionEffect(effect, true);
  }
}
