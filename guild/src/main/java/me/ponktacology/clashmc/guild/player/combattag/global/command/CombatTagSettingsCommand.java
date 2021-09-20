package me.ponktacology.clashmc.guild.player.combattag.global.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPermissions;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import me.ponktacology.clashmc.guild.player.combattag.global.updater.CombatTagSettingsUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;


@RequiredArgsConstructor
public class CombatTagSettingsCommand {

  
  private final CombatTagSettingsCache combatTagSettingsCache;
  
  private final CombatTagSettingsUpdater combatTagSettingsUpdater;

  @Command(
      value = "combattagsettings state",
      description = "Włącza/Wyłącza antylogout",
      async = true)
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsState( @Sender CommandSender sender, @Name("state") boolean state) {
    this.combatTagSettingsCache.get().setEnabled(state);
    this.combatTagSettingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &aantylogout."));
  }

  @Command(value = "combattagsettings enable", description = "Włącza antylogout", async = true)
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsEnable( @Sender CommandSender sender) {
    this.combatTagSettingsState(sender, true);
  }

  @Command(value = "combattagsettings disable", description = "Wyłącza antylogout", async = true)
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsDisable( @Sender CommandSender sender) {
    this.combatTagSettingsState(sender, false);
  }

  @Command(
      value = "combattagsettings blocked add",
      description = "Dodaję komende do zablokowanych podczas walki",
      async = true)
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsBlockedAdd(
           @Sender CommandSender sender, @Name("command") String command) {
    this.combatTagSettingsCache.get().addBlockedCommand(command);
    this.combatTagSettingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie dodano komendę jako zablokowaną podczas walki."));
  }

  @Command(
      value = "combattagsettings blocked remove",
      description = "Dodaję komende do zablokowanych podczas walki",
      async = true)
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsBlockedRemove(
           @Sender CommandSender sender, @Name("command") String command) {
    this.combatTagSettingsCache.get().removeBlockedCommand(command);
    this.combatTagSettingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie usunięto komendę jako zablokowaną podczas walki."));
  }

  @Command(
      value = "combattagsettings blocked list",
      description = "Pokazuje komendy zablokowane podczas walki")
  @Permission(GuildPermissions.COMBAT_TAG_MANAGE)
  public void combatTagSettingsBlockedList( @Sender CommandSender sender) {
    sender.sendMessage(String.join(", ", this.combatTagSettingsCache.get().getBlockedCommands()));
  }
}
