package me.ponktacology.clashmc.core.chat.global.settings.command;

import me.ponktacology.clashmc.core.menu.CoreMenuFactory;
import me.ponktacology.clashmc.core.CorePermissions;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

public class ChatSettingsCommand {

  private final CoreMenuFactory menuFactory;

  public ChatSettingsCommand(CoreMenuFactory menuFactory) {
    this.menuFactory = menuFactory;
  }

  @Command(value = "chat", description = "Ustawienia czatu")
  @Permission(CorePermissions.CHAT)
  public void chat(@Sender Player sender) {
    menuFactory.getChatSettingsMenu().openMenu(sender);
  }
}
