package me.ponktacology.clashmc.core.chat.staff.request.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Timer;
import me.ponktacology.clashmc.core.CoreConstants;
import me.ponktacology.clashmc.core.chat.staff.request.StaffRequest;
import me.ponktacology.clashmc.core.chat.staff.request.announcer.StaffRequestAnnouncer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Combined;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class StaffRequestCommand {


  private final CorePlayerCache playerCache;

  private final StaffRequestAnnouncer announcer;

  @Command(
      value = {"request", "helpop", "report"},
      description = "Wysyła wiadomość do wszystkich administratorów online",
      async = true)
  public void request( @Sender Player sender, @Name("message") @Combined String message) {
    this.playerCache
        .get(sender)
        .ifPresent(
            it -> {
              Timer requestTimer = it.getRequestTimer();
              if (!requestTimer.hasPassed(CoreConstants.REQUEST_DELAY_TIME)) {
                sender.sendMessage(
                    Text.colored(
                        "&cMusisz odczekać chwilę, zanim znowu wyślesz wiadomość do administracji."));
                return;
              }

              requestTimer.reset();

              this.announcer.announce(new StaffRequest(it, message));

              sender.sendMessage(
                  Text.colored(
                      "&aPomyślnie wysłano wiadomość do wszystkich administratorów online."));
            });
  }
}
