package me.ponktacology.clashmc.guild.guild.settings.menu;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;
import me.ponktacology.clashmc.guild.guild.settings.menu.button.GuildItemButton;
import me.ponktacology.clashmc.guild.util.GuildItemsUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Map;

public class GuildItemsMenu extends Menu {


  @Override
  public String getTitle(Player player) {
    return "&ePrzedmioty na gildię";
  }


  @Override
  public Map<Integer, Button> getButtons( Player player) {
    Map<Integer, Button> buttons = Maps.newHashMap();

    GuildSettings settings = GuildPlugin.INSTANCE.getGuildSettingsCache().get();
    CorePlayer corePlayer = CorePlugin.INSTANCE.getPlayerCache().getOrKick(player);

    int index = 0;
    for (ItemStack item : settings.getItems()) {
      buttons.put(index, new GuildItemButton(item));
      buttons.put(
          index + 9,
          new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
              return new ItemBuilder(Material.STAINED_GLASS_PANE)
                  .name("&8, ")
                  .durability(GuildItemsUtil.getRemainingItems(corePlayer, item) == 0 ? 5 : 14)
                  .build();
            }
          });

      index++;
    }

    return buttons;
  }
}
