package me.ponktacology.clashmc.guild.player.automessage;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;


@RequiredArgsConstructor
public class AutoMessageTask implements Runnable {


  private final GuildPlayerCache playerCache;
  private int currentMessage = 0;

  @Override
  public void run() {
    if (++this.currentMessage >= GuildConstants.AUTO_MESSAGES.length) {
      this.currentMessage = 0;
    }

    String message = Text.colored(GuildConstants.AUTO_MESSAGES[this.currentMessage]);

    for (GuildPlayer guildPlayer : this.playerCache.values()) {
      if (!guildPlayer.isChatSettingEnabled(ChatSettings.Settings.AUTO_MESSAGES)) continue;

      guildPlayer.sendMessage(message);
    }
  }
}
