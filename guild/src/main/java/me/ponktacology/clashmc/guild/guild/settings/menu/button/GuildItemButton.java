package me.ponktacology.clashmc.guild.guild.settings.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.util.GuildItemsUtil;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class GuildItemButton extends Button {


  private final ItemStack item;

  @Override
  public ItemStack getButtonItem( Player player) {
    CorePlayer corePlayer = CorePlugin.INSTANCE.getPlayerCache().getOrKick(player);

    double multiplier = GuildItemsUtil.guildItemsMultiplier(corePlayer);

    return new ItemBuilder(item.clone())
        .lore(
            "",
            "&eWymagana ilość: &f" + (int) (multiplier * item.getAmount()),
            "&ePotrzebujesz jeszcze: &f" + GuildItemsUtil.getRemainingItems(corePlayer, item))
        .build();
  }
}
