package me.ponktacology.clashmc.guild.recipe.armor;

import com.google.common.collect.Sets;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;
import org.bukkit.Material;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(collection = "settings", database = "guild")
@SettingsKey(key = "armor-settings")
public class ArmorSettings extends Settings {

  private final Set<Material> disabledMaterials = Sets.newHashSet();

  public void enable(Material material) {
    this.disabledMaterials.remove(material);
  }

  public void disable(Material material) {
    this.disabledMaterials.add(material);
  }

  public boolean enabled(Material material) {
    return !this.disabledMaterials.contains(material);
  }
}
