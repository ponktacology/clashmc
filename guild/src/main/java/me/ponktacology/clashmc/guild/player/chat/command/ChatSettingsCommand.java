package me.ponktacology.clashmc.guild.player.chat.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.menu.GuildMenuFactory;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class ChatSettingsCommand {


  private final GuildMenuFactory menuFactory;

  @Command(
      value = {"ustawieniaczatu", "cc"},
      description = "Pokazuje menu ustawień czatu")
  public void chatSettings( @Sender Player player) {
    this.menuFactory.getChatSettingsMenu().openMenu(player);
  }
}
