package me.ponktacology.clashmc.guild.guild.panel.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.GuildRole;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class GuildPermissionButton extends Button {

  private final Guild guild;
  private final GuildRole role;
  private final GuildPermission.Permissions permission;
  private final GuildUpdater guildUpdater = GuildPlugin.INSTANCE.getGuildUpdater();
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.PAPER)
        .name("&e" + permission.getFormattedName())
        .lore(
            "&ePosiada: "
                + StyleUtil.convertBooleanToText(
                    guild.getPermission(role).hasPermission(permission)),
            "",
            "&7Kliknij, aby zmienić.")
        .build();
  }

  @Override
  public boolean shouldUpdate(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {
      guild.getPermission(role).togglePermission(permission);
      taskDispatcher.runAsync(() -> guildUpdater.update(guild));
      player.sendMessage(Text.colored("&aPomyślnie zmieniono status posiadania permisji."));
      return true;
    }

    return super.shouldUpdate(player, clickType);
  }
}
