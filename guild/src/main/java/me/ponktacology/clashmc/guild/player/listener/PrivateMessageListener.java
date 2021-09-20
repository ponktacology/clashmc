package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.privatemessage.event.PlayerPrivateMessageEvent;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


import java.util.Optional;

@RequiredArgsConstructor
public class PrivateMessageListener implements Listener {

  private final GuildPlayerCache playerCache;

  @EventHandler(ignoreCancelled = true)
  public void onPrivateMessageEvent( PlayerPrivateMessageEvent event) {
    CorePlayer corePlayer = event.getSender();
    Player player = corePlayer.getPlayer();

    if (player == null) return;

    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(player);

    if (!guildPlayerOptional.isPresent()) return;

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    if (!guildPlayer.isChatSettingEnabled(ChatSettings.Settings.PRIVATE_MESSAGES)) {
      player.sendMessage(
          Text.colored("&cWyłączyłeś możliwość wysyłania i odbierania prywatnych wiadomości."));
      event.setCancelled(true);
      return;
    }

    Optional<GuildPlayer> receiverGuildPlayerOptional =
        this.playerCache.get(event.getReceiver().getUuid());

    if (!receiverGuildPlayerOptional.isPresent()) {
      return;
    }

    GuildPlayer receiverGuildPlayer = receiverGuildPlayerOptional.get();

    if (!receiverGuildPlayer.isChatSettingEnabled(ChatSettings.Settings.PRIVATE_MESSAGES)) {
      player.sendMessage(
          Text.colored("&cTen gracz ma wyłączoną możliwość odbierania prywatnych wiadomości."));
      event.setCancelled(true);
    }
  }
}
