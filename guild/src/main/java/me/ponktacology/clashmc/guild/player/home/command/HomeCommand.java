package me.ponktacology.clashmc.guild.player.home.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPermissions;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.home.PlayerHome;
import me.ponktacology.clashmc.sector.player.teleport.request.LocationTeleportRequest;
import me.ponktacology.clashmc.sector.player.teleport.updater.PlayerTeleportUpdater;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HomeCommand {

  private final CorePlayerCache corePlayerCache;

  private final GuildPlayerCache playerCache;

  private final PlayerTeleportUpdater teleportUpdater;

  @Command(
      value = {"sethome", "ustawdom"},
      description = "Ustawia lokalizację jako dom",
      async = true)
  @Permission(GuildPermissions.HOME)
  public void setHome(@Sender Player sender, @Name("name") String name) {
    CorePlayer corePlayer = this.corePlayerCache.getOrKick(sender);

    int maxHomeCount = getMaxHomeCount(corePlayer);

    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    if (guildPlayer.homesSize() >= maxHomeCount) {
      sender.sendMessage(
          Text.colored(
              "&cOsiągnałeś maksymalną ilość domów, aby dodać kolejny usuń poprzedni komendą /usundom."));
      return;
    }

    if (guildPlayer.getHome(name).isPresent()) {
      sender.sendMessage(Text.colored("&cPosiadasz już dom o takiej nazwie."));
      return;
    }

    guildPlayer.addHome(new PlayerHome(name, sender.getLocation()));
    guildPlayer.save();
    sender.sendMessage(Text.colored("&aPomyślnie ustawiono nowy dom."));
  }

  @Command(
      value = {"removehome", "usundom"},
      description = "Usuwa lokalizację z listy domów",
      async = true)
  @Permission(GuildPermissions.HOME)
  public void removeHome(@Sender Player sender, @Name("name") String name) {
    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    if (guildPlayer.homesSize() == 0) {
      sender.sendMessage(Text.colored("&cNie posiadasz żadnych domów."));
      return;
    }

    if (!guildPlayer.removeHome(name).isPresent()) {
      sender.sendMessage(Text.colored("&cNie posiadasz domu o takiej nazwie."));
      return;
    }

    sender.sendMessage(Text.colored("&aPomyślnie usunięto dom."));
  }

  @Command(
      value = {"home", "dom"},
      description = "Teleportuje do domu",
      async = true)
  @Permission(GuildPermissions.HOME)
  public void home(@Sender Player sender, @Name("name") String name) {
    GuildPlayer guildPlayer = this.playerCache.getOrKick(sender);

    Optional<PlayerHome> homeOptional = guildPlayer.getHome(name);

    if (!homeOptional.isPresent()) {
      sender.sendMessage(Text.colored("&cDom o takiej nazwie nie istnieje."));
      sender.sendMessage(
          Text.colored(
              "&cDostępne: &e"
                  + guildPlayer.getHomes().stream()
                      .map(PlayerHome::getName)
                      .collect(Collectors.joining("&f, &e"))));
      return;
    }

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(sender);
    PlayerHome home = homeOptional.get();

    this.teleportUpdater.update(new LocationTeleportRequest(corePlayer, home.getLocation()));
    sender.sendMessage(Text.colored("&aPomyślnie przeteleportowano do domu."));
  }

  private static int getMaxHomeCount(CorePlayer corePlayer) {
    if (corePlayer.hasPermission("svip")) return GuildConstants.SVIP_HOME_COUNT;
    if (corePlayer.hasPermission("vip")) return GuildConstants.VIP_HOME_COUNT;

    return 1;
  }
}
