package me.ponktacology.clashmc.sector.whitelist;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.sector.SectorPermissions;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;
import me.ponktacology.clashmc.sector.api.whitelist.cache.WhitelistSettingsCache;
import me.ponktacology.clashmc.sector.api.whitelist.updater.WhitelistUpdater;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;

@RequiredArgsConstructor
public class WhitelistCommand {

  private final WhitelistSettingsCache whitelistSettingsCache;

  private final WhitelistUpdater whitelistUpdater;

  @Command(value = "whitelist state", description = "Włącza/wyłącza whitelistę", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void state(@Sender CommandSender sender, @Name("enabled") boolean enabled) {
    WhitelistSettings whitelistSettings = this.whitelistSettingsCache.get();
    whitelistSettings.setEnabled(enabled);
    this.whitelistUpdater.update(whitelistSettings);

    sender.sendMessage(Text.colored("&aPomyślnie " + StyleUtil.state(enabled) + " &awhitelistę."));
  }

  @Command(value = "whitelist enable", description = "Włącza whitelistę", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void enable(@Sender CommandSender sender) {
    this.state(sender, true);
  }

  @Command(value = "whitelist disable", description = "Wyłącza whitelistę", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void disable(@Sender CommandSender sender) {
    this.state(sender, false);
  }

  @Command(value = "whitelist add", description = "Dodaję gracza na whitelistę", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void add(@Sender CommandSender sender, @Name("player") CorePlayer corePlayer) {
    WhitelistSettings whitelistSettings = this.whitelistSettingsCache.get();

    if (whitelistSettings.hasPlayer(corePlayer.getUuid())) {
      sender.sendMessage(Text.colored("&cTen gracz jest już na whiteliście."));
      return;
    }

    whitelistSettings.addPlayer(corePlayer.getUuid());
    this.whitelistUpdater.update(whitelistSettings);
    sender.sendMessage(Text.colored("&aPomyślnie dodano gracza na whitelistę."));
  }

  @Command(value = "whitelist remove", description = "Usuwa gracza z whitelisty", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void remove(@Sender CommandSender sender, @Name("player") CorePlayer corePlayer) {
    WhitelistSettings whitelistSettings = this.whitelistSettingsCache.get();

    if (!whitelistSettings.hasPlayer(corePlayer.getUuid())) {
      sender.sendMessage(Text.colored("&cTen gracz nie jest na whiteliście."));
      return;
    }

    whitelistSettings.removePlayer(corePlayer.getUuid());
    this.whitelistUpdater.update(whitelistSettings);
    sender.sendMessage(Text.colored("&aPomyślnie usunięto gracza z whitelisty."));
  }

  @Command(value = "whitelist message", description = "Ustawia wiadomość whitelisty", async = true)
  @Permission(SectorPermissions.WHITELIST_MANAGE)
  public void message(@Sender CommandSender sender, @Combined @Name("message") String message) {
    WhitelistSettings whitelistSettings = this.whitelistSettingsCache.get();

    whitelistSettings.setMessage(message);
    this.whitelistUpdater.update(whitelistSettings);
    sender.sendMessage(Text.colored("&aPomyślnie ustawiono wiadomość whitelisty."));
  }
}
