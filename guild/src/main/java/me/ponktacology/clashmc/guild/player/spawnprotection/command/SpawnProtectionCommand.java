package me.ponktacology.clashmc.guild.player.spawnprotection.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


import java.util.Optional;

@RequiredArgsConstructor
public class SpawnProtectionCommand {


  private final GuildPlayerCache playerCache;

  @Command(value = "ochrona", description = "Informacje o ochronie")
  public void spawnProtection( @Sender Player sender) {
    sender.sendMessage(
        Text.colored("&eOchrona startowa 5 minut. /ochrona off "));
  }

  @Command(
      value = {"ochrona off", "ochrona wylacz"},
      description = "Wyłącza ochronę")
  public void spawnProtectionOff( @Sender Player sender) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(sender);

    if (!guildPlayerOptional.isPresent()) {
      return;
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    if (!guildPlayer.hasSpawnProtection()) {
      sender.sendMessage(Text.colored("&cNie posiadasz już ochrony startowej."));
      return;
    }

    guildPlayer.disableSpawnProtection();
    sender.sendMessage(Text.colored("&aPomyślnie wyłączono ochronę startową."));
  }
}
