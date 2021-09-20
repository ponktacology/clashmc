package me.ponktacology.clashmc.core.chat.staff.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.chat.staff.StaffMessage;
import me.ponktacology.clashmc.core.chat.staff.announcer.StaffChatAnnouncer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class StaffChatCommand {


  private final StaffChatAnnouncer announcer;

  private final CorePlayerCache playerCache;

  @Command(
      value = {"staffchat", "sc"},
      description = "Wysyła wiadomość do administracji",
      async = true)
  @Permission(CorePermissions.STAFFCHAT)
  public void execute( @Sender Player sender, @Name("message") @Combined String message) {
    this.playerCache
        .get(sender)
        .ifPresent(it -> this.announcer.announce(new StaffMessage(it, message)));
  }
}
