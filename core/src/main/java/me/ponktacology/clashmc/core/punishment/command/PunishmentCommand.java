package me.ponktacology.clashmc.core.punishment.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.menu.CoreMenuFactory;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.punishment.anouncer.PunishmentAnnouncer;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.CommandUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

@RequiredArgsConstructor
public class PunishmentCommand {

  private final PunishmentAnnouncer announcer;

  private final CorePlayerCache playerCache;

  private final CoreMenuFactory menuFactory;

  @Command(value = "alts", description = "Sprawdza alty gracza", async = true)
  @Permission(CorePermissions.ALTS)
  public void alts(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    this.menuFactory.getAltsMenu(corePlayer).openMenu(sender);
  }

  @Command(value = "history", description = "Sprawdza alty gracza", async = true)
  @Permission(CorePermissions.HISTORY)
  public void history(@Sender Player sender, @Name("player") CorePlayer corePlayer) {
    this.menuFactory.getHistoryMenu(corePlayer).openMenu(sender);
  }

  @Command(value = "ban", description = "Nadaje graczowi bana", async = true)
  @Permission(CorePermissions.BAN)
  public void ban(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("duration") Time duration,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.BAN, reason, duration, silent, false);
  }

  @Command(value = "unban", description = "Anuluje bana graczowi", async = true)
  @Permission(CorePermissions.UNBAN)
  public void unBan(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.BAN, reason, null, silent, true);
  }

  @Command(value = "blacklist", description = "Nadaje graczowi blacklistę", async = true)
  @Permission(CorePermissions.BLACKLIST)
  public void blacklist(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.BLACKLIST, reason, Time.PERMANENT, silent, false);
  }

  @Command(value = "unblacklist", description = "Anuluje blacklistę graczowi", async = true)
  @Permission(CorePermissions.UNBLACKLIST)
  public void unBlacklist(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.BLACKLIST, reason, null, silent, true);
  }

  @Command(value = "mute", description = "Nadaje graczowi mute", async = true)
  @Permission(CorePermissions.MUTE)
  public void mute(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("duration") Time duration,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.MUTE, reason, duration, silent, false);
  }

  @Command(value = "unmute", description = "Anuluje mute graczowi", async = true)
  @Permission(CorePermissions.UNMUTE)
  public void unMute(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("reason") @Combined String reason) {
    execute(sender, corePlayer, PunishmentType.MUTE, reason, null, silent, true);
  }

  @Command(value = "kick", description = "Wyrzuca gracza", async = true)
  @Permission(CorePermissions.KICK)
  public void kick(
      @Sender CommandSender sender,
      @Flag(value = 's') boolean silent,
      @Name("player") CorePlayer corePlayer,
      @Name("reason") @Combined String reason) {
    Punishment punishment =
        new Punishment(
            corePlayer.getUuid(),
            CommandUtil.getUuid(sender),
            PunishmentType.KICK,
            reason,
            System.currentTimeMillis(),
            -1L,
            silent);

    punishment.setRemoved(true);
    punishment.setRemovedBy(Console.UUID);
    punishment.setRemoveReason("Kick");
    punishment.setRemovedOn(System.currentTimeMillis());

    announcer.announce(punishment);

    if (!playerCache.isOnline(corePlayer)) {
      corePlayer.addPunishment(punishment);
      corePlayer.save();
    }
  }

  private void execute(
      CommandSender sender,
      CorePlayer corePlayer,
      PunishmentType type,
      String reason,
      Time duration,
      boolean silent,
      boolean undo) {

    Optional<Punishment> punishmentOptional = corePlayer.getActivePunishment(type);

    if (undo) {
      if (!punishmentOptional.isPresent()) {
        sender.sendMessage(Text.colored("&cTen gracz nie jest " + type.getAddFormat() + "."));
        return;
      }

      Punishment tempPunishment = punishmentOptional.get();
      Punishment punishment =
          new Punishment(
              tempPunishment.getUuid(),
              tempPunishment.getAddedBy(),
              tempPunishment.getType(),
              tempPunishment.getReason(),
              tempPunishment.getAddedOn(),
              tempPunishment.getDuration(),
              silent);

      punishment.setRemoved(true);
      punishment.setRemovedOn(System.currentTimeMillis());
      punishment.setRemovedBy(CommandUtil.getUuid(sender));
      punishment.setRemoveReason(reason);

      announcer.announce(punishment);

      if (!playerCache.isOnline(corePlayer)) {
        corePlayer.removePunishment(
            punishment.getType(), punishment.getRemoveReason(), punishment.getRemovedBy());
        corePlayer.save();
      }
    } else {
      if (punishmentOptional.isPresent()) {
        sender.sendMessage(Text.colored("&cTen gracz jest już " + type.getAddFormat() + "."));
        return;
      }

      Punishment punishment =
          new Punishment(
              corePlayer.getUuid(),
              CommandUtil.getUuid(sender),
              type,
              reason,
              System.currentTimeMillis(),
              duration.getTimeStamp(),
              silent);

      announcer.announce(punishment);

      if (!playerCache.isOnline(corePlayer)) {
        corePlayer.addPunishment(punishment);
        corePlayer.save();
      }
    }
  }
}
