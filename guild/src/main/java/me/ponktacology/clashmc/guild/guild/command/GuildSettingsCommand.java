package me.ponktacology.clashmc.guild.guild.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPermissions;
import me.ponktacology.clashmc.guild.guild.settings.cache.GuildSettingsCache;
import me.ponktacology.clashmc.guild.guild.settings.menu.GuildSettingsItemsMenu;
import me.ponktacology.clashmc.guild.guild.settings.updater.GuildSettingsUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GuildSettingsCommand {

  private final GuildSettingsCache settingsCache;
  private final GuildSettingsUpdater settingsUpdater;

  @Command(value = "guildsettings state", description = "Włącza/Wyłącza gildie", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void state(@Sender CommandSender sender, @Name("state") boolean state) {
    this.settingsCache.get().setEnabled(state);
    this.settingsUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &agildie."));
  }

  @Command(value = "guildsettings enable", description = "Włącza gildie", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void enable(@Sender CommandSender sender) {
    this.state(sender, true);
  }

  @Command(value = "guildsettings disable", description = "Wyłącza gildie", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void disable(@Sender CommandSender sender) {
    this.state(sender, false);
  }

  @Command(value = "guildsettings allies", description = "Włącza/Wyłącza sojusze", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void setAllies(@Sender CommandSender sender, @Name("state") boolean state) {
    this.settingsCache.get().setEnabledAllies(state);
    this.settingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &asojusze gildyjne."));
  }

  @Command(
      value = "guildsettings maxmembers",
      description = "Ustawia maksymalną ilość członków gildii",
      async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void setMaxMembers(@Sender CommandSender sender, @Name("max") int max) {
    this.settingsCache.get().setMaxMembers(max);
    this.settingsUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono maksymalną liczbę członków gildii."));
  }

  @Command(
      value = "guildsettings maxallies",
      description = "Ustawia maksymalną ilość sojuszów gildyjnych",
      async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void setMaxAllies(@Sender CommandSender sender, @Name("max") int max) {
    this.settingsCache.get().setMaxAllies(max);
    this.settingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie ustawiono maksymalną liczbę sojuszów gildyjnych."));
  }

  @Command(
      value = "guildsettings items state",
      description = "Włącza/Wyłącza dostęp do podglądu itemów na gildię",
      async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void items(@Sender CommandSender sender, @Name("state") boolean state) {
    this.settingsCache.get().setEnabledItems(state);
    this.settingsUpdater.update();

    sender.sendMessage(
        Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &apodgląd itemów na gildię."));
  }

  @Command(value = "guildsettings tnt state", description = "Włącza/Wyłącza TnT", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void tnt(@Sender CommandSender sender, @Name("state") boolean state) {
    this.settingsCache.get().setEnabledTnt(state);
    this.settingsUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &aTnT."));
  }

  @Command(
      value = "guildsettings wars state",
      description = "Włącza/Wyłącza wojny gildii",
      async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void wars(@Sender CommandSender sender, @Name("state") boolean state) {
    this.settingsCache.get().setEnableWars(state);
    this.settingsUpdater.update();

    sender.sendMessage(Text.colored("&aPomyślnie " + StyleUtil.state(state) + " &awojny gildii."));
  }

  @Command(value = "guildsettings items", description = "Ustawia itemy na gildie", async = true)
  @Permission(GuildPermissions.GUILD_MANAGE)
  public void setItems(@Sender Player sender) {
    new GuildSettingsItemsMenu().openMenu(sender);
  }
}
