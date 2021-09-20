package me.ponktacology.clashmc.core.chat.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.Timer;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.chat.global.announcer.GlobalChatAnnouncer;
import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;
import me.ponktacology.clashmc.core.chat.global.settings.cache.ChatSettingsCache;
import me.ponktacology.clashmc.core.log.chat.ChatLog;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.util.Patterns;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PlayerAsyncChatListener implements Listener {


  private final TaskDispatcher dispatcher;

  private final CorePlayerCache playerCache;

  private final ChatSettingsCache chatSettingsCache;

  @EventHandler(priority = EventPriority.LOW, ignoreCancelled = true)
  public void onAsyncPlayerChatEventLowest( AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    Optional<CorePlayer> playerOptional = playerCache.get(player);

    if (!playerOptional.isPresent()) {
      log.info("Player sent chat message but not in cache, player= " + player.getName());
      event.setCancelled(true);
      return;
    }

    CorePlayer corePlayer = playerOptional.get();

    if (corePlayer.isStaff()) {
      return;
    }

    ChatSettings chatSettings = chatSettingsCache.get();

    if (chatSettings.getState() == ChatSettings.ChatState.DELAYED) {
      Timer chatTimer = corePlayer.getChatTimer();
      if (!chatTimer.hasPassed(chatSettings.getDelay())) {
        player.sendMessage(
            Text.colored("&cMusisz odczekać chwilę, zanim wyślesz kolejną wiadomość."));
        event.setCancelled(true);
        return;
      }

      chatTimer.reset();
    }

    Optional<Punishment> punishmentOptional = corePlayer.getActivePunishment(PunishmentType.MUTE);

    if (punishmentOptional.isPresent()) {
      Punishment punishment = punishmentOptional.get();
      player.sendMessage(
          Text.colored(
                  "&cJesteś "
                      + (punishment.getDuration() > 0 ? "" : "permanentnie ")
                      + " wyciszony! \n&cPowód: &f"
                      + punishment.getReason()
                      + "\n&cWygasa: &f"
                      + punishment.getFormattedExpireDate()
              + "\n&c&oMożesz odwołać się od kary na Team Speaku: &ots.clashmc.pl."));
      event.setCancelled(true);
    }

    String message = event.getMessage();

    if (Patterns.DISALLOWED_URL.matcher(message).matches()) {
      player.sendMessage(Text.colored("&cTwoja wiadomość zawiera niedozwolone linki."));
      event.setCancelled(true);
      return;
    }

    if (Patterns.IP.matcher(message).matches()) {
      player.sendMessage(Text.colored("&cTwoja wiadomość zawiera adres ip."));
      event.setCancelled(true);
      return;
    }

    if (chatSettings.getState() == ChatSettings.ChatState.POWER) {
      if (corePlayer.getMainRank().getPower() < chatSettings.getMinPower()) {
        player.sendMessage(Text.colored("&cCzat jest aktualnie wyłączony"));
        event.setCancelled(true);
      }
    } else if (chatSettings.getState() == ChatSettings.ChatState.STAFF) {
      player.sendMessage(Text.colored("&cCzat jest aktualnie wyłączony"));
      event.setCancelled(true);
    }
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onAsyncPlayerChatEvent( AsyncPlayerChatEvent event) {
    Player player = event.getPlayer();
    Optional<CorePlayer> playerOptional = playerCache.get(player);

    if (!playerOptional.isPresent()) {
      log.info("Player sent chat message but not in cache, player= " + player.getName());
      event.setCancelled(true);
      return;
    }

    CorePlayer corePlayer = playerOptional.get();

    corePlayer.addChatLog(new ChatLog(event.getMessage()));

    GlobalChatAnnouncer chatAnnouncer = CorePlugin.INSTANCE.getChatAnnouncer();

    if (chatAnnouncer != null) {
      chatAnnouncer.announce(new GlobalChatEvent(player, event.getMessage(), event.getFormat()));
    }

    this.dispatcher.runAsync(corePlayer::save);

    event.setCancelled(true);
  }

  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onGlobalChatEvent( GlobalChatEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = playerCache.getOrKick(player);

    Rank rank = corePlayer.getMainRank();
    String prefix = rank.getPrefix().isEmpty() ? "" : rank.getPrefix() + " ";
    String suffix = rank.getSuffix().isEmpty() ? "" : " " + rank.getSuffix();

    event.setFormat(
        Text.colored(
            prefix
                + "&f"
                + rank.getColor()
                + "%1$s"
                + suffix
                + "&8:&f "
                + rank.getMessageColor()
                + "%2$s"));
  }
}
