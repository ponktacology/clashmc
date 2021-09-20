package me.ponktacology.clashmc.guild.player.chat.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class ChatSettingButton extends Button {

  private final ChatSettings.Settings setting;
  private final GuildPlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem( Player player) {
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    boolean enabled = guildPlayer.isChatSettingEnabled(setting);
    return new ItemBuilder(Material.INK_SACK)
        .durability(enabled ? 10 : 8)
        .name("&a" + setting.getFormattedName())
        .lore("&eWłączone: " + StyleUtil.convertBooleanToText(enabled), "", "&7Kliknij, aby zmienić.")
        .build();
  }

  @Override
  public boolean shouldUpdate( Player player,  ClickType clickType) {
    if (!clickType.isLeftClick()) return super.shouldUpdate(player, clickType);

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    guildPlayer.getChatSettings().toggle(setting);

    this.taskDispatcher.runAsync(guildPlayer::save);

    return true;
  }
}
