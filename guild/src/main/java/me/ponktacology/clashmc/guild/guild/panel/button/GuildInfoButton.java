package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildInfoButton extends Button {

  private final Guild guild;
  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();

  @Override
  public ItemStack getButtonItem(Player player) {
    ItemBuilder itemBuilder =
        new ItemBuilder(Material.BEACON).name("&eInformacje o członkach gildii");

    for (GuildPlayer guildPlayer : guild.members()) {
      itemBuilder.lore(
          (playerCache.isOnlineNotInAuthOrLobby(guildPlayer) ? "&a" : "&c")
              + guildPlayer.getName()
              + " &7(&f"
              + guild.getRole(guildPlayer).getFormattedName()
              + "&7)");
    }

    return itemBuilder.build();
  }
}
