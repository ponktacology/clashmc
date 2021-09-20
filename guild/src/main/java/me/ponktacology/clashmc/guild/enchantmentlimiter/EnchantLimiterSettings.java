package me.ponktacology.clashmc.guild.enchantmentlimiter;

import com.google.common.collect.Maps;
import lombok.Data;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;
import org.bukkit.enchantments.Enchantment;

import java.util.Map;

@Data
@Entity(collection = "settings", database = "guild")
@SettingsKey(key = "enchantlimiter-settings")
public class EnchantLimiterSettings extends Settings {

  private final Map<EnchantType, Map<String, Integer>> limits = Maps.newHashMap();

  public void set(EnchantType type, Enchantment enchantment, int limit) {
    Map<String, Integer> map = this.limits.computeIfAbsent(type, t -> Maps.newHashMap());

    map.put(enchantment.getName(), limit);
  }

  public void reset(EnchantType type, Enchantment enchantment) {
    Map<String, Integer> map = this.limits.getOrDefault(type, Maps.newHashMap());

    map.remove(enchantment.getName());
  }

  public int get(EnchantType type, Enchantment enchantment) {
    Map<String, Integer> map = this.limits.getOrDefault(type, Maps.newHashMap());

    return map.getOrDefault(enchantment.getName(), enchantment.getMaxLevel());
  }
}
