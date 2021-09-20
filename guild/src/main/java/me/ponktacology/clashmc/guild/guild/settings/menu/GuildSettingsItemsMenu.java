package me.ponktacology.clashmc.guild.guild.settings.menu;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.button.DisplayButton;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GuildSettingsItemsMenu extends Menu {

  {
    setCanAddItems(true);
  }


  @Override
  public String getTitle(Player player) {
    return "&eItemy na gildię";
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    GuildSettings settings = GuildPlugin.INSTANCE.getGuildSettingsCache().get();

    for (ItemStack item : settings.getItems()) {
      buttons.put(buttons.size(), new DisplayButton(item, false));
    }

    return buttons;
  }

  @Override
  public void onClose( Player player) {
    GuildSettings settings = GuildPlugin.INSTANCE.getGuildSettingsCache().get();

    settings.setItems(
        Lists.newArrayList(
            Arrays.stream(player.getOpenInventory().getTopInventory().getContents())
                .filter(Objects::nonNull)
                .collect(Collectors.toList())));

    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .runAsync(() -> GuildPlugin.INSTANCE.getGuildSettingsUpdater().update());
  }
}
