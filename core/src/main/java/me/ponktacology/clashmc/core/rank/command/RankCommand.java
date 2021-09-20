package me.ponktacology.clashmc.core.rank.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.menu.CoreMenuFactory;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.cache.RankCache;
import me.ponktacology.clashmc.core.rank.factory.RankFactory;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.rank.updater.RankUpdater;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.CommandUtil;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


import java.util.Optional;

@RequiredArgsConstructor
public class RankCommand {

  
  private final RankCache rankCache;
  
  private final RankFactory rankFactory;
  
  private final RankUpdater rankUpdater;
  
  private final CoreMenuFactory menuFactory;

  @Command(value = "rank create", description = "Tworzy nową rangę", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void create( @Sender CommandSender sender,  @Name("name") String name) {
    Optional<Rank> rankOptional = this.rankCache.get(name);

    if (rankOptional.isPresent()) {
      sender.sendMessage(Text.colored("&cTaka ranga już istnieje."));
      return;
    }

    Rank rank = this.rankFactory.create(name);
    rank.save();
    rankUpdater.update(rank);
    sender.sendMessage(Text.colored("&aPomyślnie utworzono rangę."));
  }

  @Command(value = "rank remove", description = "Usuwa rangę", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void remove( @Sender CommandSender sender, @Name("rank") Rank rank) {
    rankUpdater.remove(rank);
    sender.sendMessage(Text.colored("&aPomyślnie usunięto rangę."));
  }

  @Command(value = "rank info", description = "Pokazuje informacje dotyczące rangi")
  @Permission(CorePermissions.RANK_MANAGE)
  public void info( @Sender CommandSender sender,  @Name("rank") Rank rank) {
    sender.sendMessage(rank.toString());
  }

  @Command(value = "rank prefix", description = "Zmienia prefix rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void prefix(
           @Sender CommandSender sender,  @Name("rank") Rank rank, @Name("prefix") String prefix) {
    rank.setPrefix(prefix);
    rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono prefix rangi."));
  }

  @Command(value = "rank color", description = "Zmienia color rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void color(
           @Sender CommandSender sender,  @Name("rank") Rank rank, @Name("color") String color) {
    rank.setColor(color);
    rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono color rangi."));
  }

  @Command(value = "rank messagecolor", description = "Zmienia messageColor rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void messageColor(
           @Sender CommandSender sender,  @Name("rank") Rank rank, @Name("color") String color) {
    rank.setMessageColor(color);
    rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono color rangi."));
  }

  @Command(value = "rank staff", description = "Zmienia staff rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void messageColor(
           @Sender CommandSender sender,  @Name("rank") Rank rank, @Name("staff") boolean staff) {
    rank.setStaff(staff);
    rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono staff rangi."));
  }

  @Command(value = "rank power", description = "Zmienia power rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void messageColor(
           @Sender CommandSender sender,  @Name("rank") Rank rank, @Name("power") int power) {
    rank.setPower(power);
    rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie ustawiono staff rangi."));
  }

  @Command(
      value = "rank permissions list",
      description = "Pokazuję liste permisji rangi",
      async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void permissionsRemove( @Sender CommandSender sender,  @Name("rank") Rank rank) {
    sender.sendMessage(String.join("\n", rank.getPermissions()));
  }

  @Command(value = "rank permissions add", description = "Dodaję permisję do rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void permissionsAdd(
           @Sender CommandSender sender,
           @Name("rank") Rank rank,
          @Name("permission") String permission) {
    rank.getPermissions().add(permission);
    this.rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie dodano permisję do rangi."));
  }

  @Command(value = "rank permissions remove", description = "Usuwa permisję z rangi", async = true)
  @Permission(CorePermissions.RANK_MANAGE)
  public void permissionsRemove(
           @Sender CommandSender sender,
           @Name("rank") Rank rank,
          @Name("permission") String permission) {
    rank.getPermissions().remove(permission);
    this.rankUpdater.update(rank);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto permisję z rangi."));
  }

  @Command(
      value = {"rank player check", "grants"},
      description = "Pokazuje wszystkie rangi gracza",
      async = true)
  @Permission(CorePermissions.RANK_PLAYER_MANAGE)
  public void playerCheck(
           @Sender Player sender,
          @me.vaperion.blade.command.annotation.Optional("self") @Name("player")
          CorePlayer corePlayer) {
    menuFactory.getGrantsMenu(corePlayer).openMenu(sender);
  }

  @Command(
      value = {"rank player add", "grant", "setrank"},
      description = "Dodaje rangę graczowi",
      async = true)
  @Permission(CorePermissions.RANK_PLAYER_MANAGE)
  public void playerAdd(
           @Sender CommandSender sender,
          @Name("player") CorePlayer corePlayer,
           @Name("rank") Rank rank,
           @Name("time") Time time,
          @me.vaperion.blade.command.annotation.Optional("Awans") @Name("reason") String reason) {
    this.rankUpdater.addRankToPlayer(
        corePlayer,
        new Grant(
            rank.getName(),
            CommandUtil.getUuid(sender),
            reason,
            System.currentTimeMillis(),
            time.getTimeStamp()));
    sender.sendMessage(Text.colored("&aPomyślnie dodano rangę graczowi."));
  }

  @Command(
      value = {"rank player remove", "ungrant", "revokegrant", "unsetrank"},
      description = "Dodaje rangę graczowi",
      async = true)
  @Permission(CorePermissions.RANK_PLAYER_MANAGE)
  public void playerRemove(
           @Sender CommandSender sender,
           @Name("player") CorePlayer corePlayer,
           @Name("rank") Rank rank,
          @me.vaperion.blade.command.annotation.Optional("Degrad") @Name("reason") String reason) {
    Optional<Grant> grantOptional = corePlayer.getActiveGrant(rank);

    if (!grantOptional.isPresent()) {
      sender.sendMessage(Text.colored("&cTen gracz nie posiada tej rangi."));
      return;
    }

    if (rank.getName().equals(rankCache.defaultRank().getName())) {
      sender.sendMessage(Text.colored("&cNie możesz usunąć tej rangi graczowi."));
      return;
    }

    Grant tempGrant = grantOptional.get();
    Grant grant =
        new Grant(
            tempGrant.getRankName(),
            tempGrant.getAddedBy(),
            tempGrant.getReason(),
            tempGrant.getAddedOn(),
            tempGrant.getDuration());

    grant.setRemoved(true);
    grant.setRemovedOn(System.currentTimeMillis());
    grant.setRemovedBy(CommandUtil.getUuid(sender));
    grant.setRemoveReason(reason);

    rankUpdater.removeRankFromPlayer(corePlayer, grant);
    sender.sendMessage(Text.colored("&aPomyślnie usunięto rangę graczowi."));
  }
}
