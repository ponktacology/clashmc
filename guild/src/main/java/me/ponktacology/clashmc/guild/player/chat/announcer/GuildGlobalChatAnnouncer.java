package me.ponktacology.clashmc.guild.player.chat.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.core.chat.global.announcer.GlobalChatAnnouncer;
import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.chat.global.packet.PacketGlobalChatAnnounce;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class GuildGlobalChatAnnouncer implements GlobalChatAnnouncer {

  private final NetworkService networkService;
  private final CorePlayerCache corePlayerCache;
  private final GuildPlayerCache playerCache;
  private final TaskDispatcher taskDispatcher;

  @Override
  public void announce(GlobalChatEvent event) {
    this.taskDispatcher.runAsync(
        () -> {
          PluginUtil.callEvent(event);

          if (!event.isCancelled()) {
            this.networkService.publish(
                new PacketGlobalChatAnnounce(
                    event.getPlayer().getUniqueId(), event.getFormat(), event.getMessage()));
          }
        });
  }

  @Override
  @PacketHandler
  public void handle(PacketGlobalChatAnnounce packet) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(packet.getSender());

    if (!guildPlayerOptional.isPresent()) {
      log.info(
          "Received global chat message but guild player not found in database, player= "
              + packet.getSender().toString());
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();
    
    String message = packet.getMessage();

    Optional<CorePlayer> corePlayerOptional = this.corePlayerCache.get(packet.getSender());

    if (!corePlayerOptional.isPresent()) {
      log.info(
          "Received global chat message but core player not found in database, player= "
              + packet.getSender().toString());
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();
    boolean isStaff = corePlayer.isStaff();
    boolean hasGuild = guildPlayer.hasGuild();
    Guild guild = guildPlayer.getGuild().orElse(null);

    for (GuildPlayer otherGuildPlayer : this.playerCache.values()) {
      if (!otherGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.GLOBAL_CHAT_MESSAGES)
          && !isStaff) continue;

      String playerTag = "";

      if (hasGuild) {
        if (guild.hasMember(otherGuildPlayer)) {
          playerTag = "&8[&2" + guild.getTag() + "&8] ";
        } else if (guild.hasAlly(otherGuildPlayer)) {
          playerTag = "&8[&9" + guild.getTag() + "&8] ";
        } else playerTag = "&8[&c" + guild.getTag() + "&8] ";
      }

      String formatted =
          String.format(
              Text.colored(packet.getFormat().replace("{TAG}", playerTag)),
              guildPlayer.getName(),
              message);

      otherGuildPlayer.sendMessage(formatted);
    }
  }
}
