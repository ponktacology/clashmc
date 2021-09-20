package me.ponktacology.clashmc.core.punishment.anouncer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.api.util.Unknown;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.punishment.anouncer.packet.PacketPunishmentAnnounce;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


import java.util.Optional;
import java.util.UUID;

@Slf4j
public final class PunishmentAnnouncer implements Announcer<Punishment, PacketPunishmentAnnounce> {

  private final CorePlayerFactory playerFactory;
  private final CorePlayerCache playerCache;
  private final NetworkService networkService;
  private final TaskDispatcher dispatcher;

  public PunishmentAnnouncer(
      CorePlayerFactory playerFactory,
      CorePlayerCache playerCache,
      NetworkService networkService,
      TaskDispatcher dispatcher) {
    this.playerFactory = playerFactory;
    this.playerCache = playerCache;
    this.networkService = networkService;
    this.dispatcher = dispatcher;
  }

  @Override
  public void announce( Punishment punishment) {
    UUID issuer =
        punishment.isRemoved() && punishment.getType() != PunishmentType.KICK
            ? punishment.getRemovedBy()
            : punishment.getAddedBy();
    UUID receiver = punishment.getUuid();

    Optional<CorePlayer> receiverPlayerOptional = playerFactory.load(receiver);

    if (!receiverPlayerOptional.isPresent()) {
      log.info("Punishment was announced but receiver not found in database, aborting.");
      return;
    }

    CorePlayer receiverPlayer = receiverPlayerOptional.get();
    String formattedReceiverName = receiverPlayer.getFormattedName();

    String formattedIssuerName;
    if (issuer.equals(Console.UUID)) {
      formattedIssuerName = Console.FORMATTED_NAME;
    } else {
      Optional<CorePlayer> corePlayerOptional = playerFactory.load(issuer);

      formattedIssuerName =
          corePlayerOptional.map(CorePlayer::getFormattedName).orElse(Unknown.FORMATTED_NAME);
    }

    networkService.publish(
        new PacketPunishmentAnnounce(formattedIssuerName, formattedReceiverName, punishment));
  }

  @Override
  public void handle( PacketPunishmentAnnounce announce) {
    Punishment punishment = announce.getPunishment();
    PunishmentType type = punishment.getType();

    String formattedMessage =
        Text.colored(
            (punishment.isSilent() ? "&7[Silent] " : "")
                + announce.getFormattedReceiver()
                + " &azostał "
                + (punishment.isRemoved() ? type.getRemoveFormat() : type.getAddFormat())
                + " przez &f"
                + announce.getFormattedIssuer());

    if (punishment.isSilent()) {
      for (CorePlayer corePlayer : this.playerCache.staffMembers()) {
        corePlayer.sendMessage(formattedMessage);
      }
    } else {
      Bukkit.getServer().broadcastMessage(formattedMessage);
    }

    UUID receiver = punishment.getUuid();
    Player player = Bukkit.getPlayer(receiver);

    if (player == null) {
      return;
    }

    Optional<CorePlayer> playerOptional = playerCache.get(player);

    if (!playerOptional.isPresent()) {
      log.info("Player not found, aborting punishment");
      return;
    }

    CorePlayer corePlayer = playerOptional.get();

    if (punishment.isRemoved() && punishment.getType() != PunishmentType.KICK) {
      corePlayer.removePunishment(
          punishment.getType(), punishment.getRemoveReason(), punishment.getRemovedBy());
    } else {
      corePlayer.addPunishment(punishment);

      switch (type) {
        case KICK:
          dispatcher.run(
              () -> {
                player.kickPlayer(
                    Text.colored(
                        "&cZostałeś wyrzucony!" + "\n\n&cPowód: &f" + punishment.getReason()));
              });
          break;
        case BLACKLIST:
        case BAN:
          dispatcher.run(
              () ->
                  player.kickPlayer(
                      Text.colored(
                          "&cZostałeś "
                              + (punishment.getDuration() > 0 ? "" : "permanentnie ")
                              + type.getAddFormat()
                              + "!"
                              + "\n\n&cPowód: &f"
                              + punishment.getReason()
                              + (punishment.getDuration() > 0
                                  ? "\n&cWygasa: &f"
                                      + TimeUtil.formatTimeMillisToDate(
                                          punishment.getAddedOn() + punishment.getDuration())
                                  : "")
                              + "\n\n&c&oMożesz odwołać się od kary na Team Speaku: ts.clashmc.pl.")));
          break;
        case MUTE:
          player.sendMessage(
              Text.colored(
                  "&cZostałeś "
                      + (punishment.getDuration() > 0 ? "" : "permanentnie ")
                      + " wyciszony! \n&cPowód: &f"
                      + punishment.getReason()
                      + (punishment.getDuration() > 0
                          ? "\n&cWygasa: &f"
                              + TimeUtil.formatTimeMillisToDate(
                                  punishment.getAddedOn() + punishment.getDuration())
                          : "")
                      + "\n&c&oMożesz odwołać się od kary na Team Speaku: &ots.clashmc.pl."));
          break;
      }
    }

    corePlayer.save();
  }

  @RequiredArgsConstructor
  public static class PunishmentAnnounceListener implements PacketListener {


    private final PunishmentAnnouncer announcer;

    @PacketHandler
    public void onPunishmentAnnounce( PacketPunishmentAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}
