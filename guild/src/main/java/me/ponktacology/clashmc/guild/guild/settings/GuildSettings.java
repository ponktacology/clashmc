package me.ponktacology.clashmc.guild.guild.settings;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.inventory.ItemStack;


import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity(collection = "settings", database = "guild")
@SettingsKey(key = "guild-settings")
public class GuildSettings extends Settings {

  
  private List<ItemStack> items = Lists.newArrayList();
  private boolean enabled = true;
  private boolean enabledItems = true;
  private boolean enabledAllies;
  private boolean enableWars = true;
  private boolean enabledTnt;
  private int maxMembers = 25;
  private int maxAllies = 3;
  private int maxWars = 5;

  public List<ItemStack> getItems(double multiplier) {
    return this.items.stream()
        .map(
            it ->
                new ItemBuilder(it.clone())
                    .amount(Math.max(1, (int) (it.getAmount() * multiplier)))
                    .build())
        .collect(Collectors.toList());
  }
}
