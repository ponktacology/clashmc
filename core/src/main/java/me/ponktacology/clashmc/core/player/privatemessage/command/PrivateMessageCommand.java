package me.ponktacology.clashmc.core.player.privatemessage.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.privatemessage.PrivateMessage;
import me.ponktacology.clashmc.core.player.privatemessage.announcer.PrivateMessageAnnouncer;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.Combined;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class PrivateMessageCommand {


  private final PrivateMessageAnnouncer announcer;

  private final CorePlayerCache playerCache;

  @Command(
      value = {"ignore", "ignoruj"},
      description = "Ignoruje gracza",
      async = true)
  public void ignore( @Sender Player sender,  @Name("player") CorePlayer corePlayer) {
    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    if (sender.getUniqueId().equals(corePlayer.getUuid())) {
      sender.sendMessage(Text.colored("&cNie możesz ignorować samego siebie."));
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

    if (senderCorePlayer.isIgnored(corePlayer)) {
      sender.sendMessage(Text.colored("&cJuż ignorujesz tego gracza."));
      return;
    }

    senderCorePlayer.ignore(corePlayer);
    senderCorePlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie zacząłeś ignorować tego gracza."));
  }

  @Command(
      value = {"unignore", "odignoruj"},
      description = "Przestaje ignorować gracza",
      async = true)
  public void unIgnore( @Sender Player sender,  @Name("player") CorePlayer corePlayer) {
    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

    if (!senderCorePlayer.isIgnored(corePlayer)) {
      sender.sendMessage(Text.colored("&cNie ignorujesz tego gracza."));
      return;
    }

    senderCorePlayer.unIgnore(corePlayer);
    senderCorePlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie przestałeś ignorować tego gracza."));
  }

  @Command(
      value = {"message", "msg"},
      description = "Wysyła prywatną wiadomość do gracza",
      async = true)
  public void message(
           @Sender Player sender,
           @Name("player") CorePlayer corePlayer,
          @Combined @Name("message") String message) {
    messageInternal(sender, corePlayer, message);
  }

  @Command(
      value = {"reply", "r"},
      description =
          "Wysyła prywatną wiadomość do gracza, któremu wcześniej już wysyłaliśmy komendę",
      async = true)
  public void reply( @Sender Player sender, @Combined @Name("message") String message) {
    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();
    Optional<UUID> replyPlayer = senderCorePlayer.getReplyPlayer();

    if (!replyPlayer.isPresent()) {
      sender.sendMessage(Text.colored("&cNie masz komu odpisać."));
      return;
    }

    Optional<CorePlayer> replyCorePlayerOptional = this.playerCache.get(replyPlayer.get());

    if (!replyCorePlayerOptional.isPresent()) {
      sender.sendMessage(Text.colored("&cNie masz komu odpisać."));
      return;
    }

    CorePlayer replyCorePlayer = replyCorePlayerOptional.get();

    messageInternal(sender, replyCorePlayer, message);
  }

  private void messageInternal( Player sender,  CorePlayer receiver, String message) {
    if (sender.getUniqueId().equals(receiver.getUuid())) {
      sender.sendMessage(Text.colored("&cNie możesz wysłać wiadomości do samego siebie."));
      return;
    }

    if (!this.playerCache.isOnline(receiver)) {
      sender.sendMessage(Text.colored("&cTen gracz nie jest online."));
      return;
    }

    Optional<CorePlayer> senderCorePlayerOptional = this.playerCache.get(sender);

    if (!senderCorePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer senderCorePlayer = senderCorePlayerOptional.get();

    senderCorePlayer.setReplyPlayer(receiver.getUuid());
    senderCorePlayer.save();

    this.announcer.announce(
        new PrivateMessage(
            senderCorePlayer, receiver, ChatColor.stripColor(Text.colored(message))));
  }
}
