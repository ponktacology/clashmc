package me.ponktacology.clashmc.guild.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantType;
import me.ponktacology.clashmc.guild.enchantmentlimiter.cache.EnchantLimiterSettingsCache;
import me.ponktacology.clashmc.guild.enchantmentlimiter.menu.EnchantLimiterMenu;
import me.ponktacology.clashmc.guild.enchantmentlimiter.updater.EnchantLimiterSettingsUpdater;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.menu.ChatSettingsMenu;

@RequiredArgsConstructor
public class GuildMenuFactory {

  private final GuildPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  private final EnchantLimiterSettingsCache enchantLimiterSettingsCache;

  private final EnchantLimiterSettingsUpdater enchantLimiterSettingsUpdater;

  public ChatSettingsMenu getChatSettingsMenu() {
    return new ChatSettingsMenu(this.playerCache, this.taskDispatcher);
  }

  public EnchantLimiterMenu getEnchantLimiterMenu(EnchantType type) {
    return new EnchantLimiterMenu(
        type,
        this.enchantLimiterSettingsCache,
        this.enchantLimiterSettingsUpdater,
        this.taskDispatcher);
  }
}
