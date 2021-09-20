package me.ponktacology.clashmc.core.chat.global.broadcast.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.chat.global.broadcast.Broadcast;
import me.ponktacology.clashmc.core.chat.global.broadcast.type.BroadcastType;
import me.ponktacology.clashmc.core.chat.global.broadcast.announcer.BroadcastAnnouncer;
import me.ponktacology.clashmc.core.time.Time;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class BroadcastCommand {

  private final BroadcastAnnouncer broadcastAnnouncer;

  @Command(
      value = {"broadcast", "bc", "alert"},
      description = "Wysyła ogłoszenie",
      async = true)
  @Permission(CorePermissions.CHAT)
  public void broadcast(
          @Sender CommandSender sender,
           @Name("duration") Time duration,
          @Name("type") BroadcastType type,
          @Name("message") @Combined String message) {

    this.broadcastAnnouncer.announce(
        new Broadcast(message, type, 10, (int) (duration.getTimeStamp() / 50L), 10));
  }
}
