package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.menus.ConfirmMenu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildPlayerRoleButton extends Button {

  private final Guild guild;
  private final GuildRole role;
  private final GuildPlayer otherPlayer;
  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();
  private final GuildUpdater guildUpdater = GuildPlugin.INSTANCE.getGuildUpdater();
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(role.getIcon())
        .name("&e" + role.getFormattedName())
        .lore("&7Kliknij, aby nadać tę rangę graczowi.")
        .build();
  }

  @Override
  public void clicked(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {
      if (role == GuildRole.LEADER) {
        GuildPlayer guildPlayer = playerCache.getOrKick(player);

        new ConfirmMenu(
            "&ePotwiedź przekazanie lidera",
            (response) -> {
              guild.setRole(guildPlayer, GuildRole.MODERATOR);
              guild.setRole(otherPlayer, role);
              taskDispatcher.runAsync(() -> guildUpdater.update(guild));
              player.sendMessage(Text.colored("&aPomyślnie przekazano lidera."));
            },
            true);
      }

      guild.setRole(otherPlayer, role);
      taskDispatcher.runAsync(() -> guildUpdater.update(guild));
      player.sendMessage(Text.colored("&aPomyślnie nadano rangę."));
      player.getOpenInventory().close();
    }
  }
}
