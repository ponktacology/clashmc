package me.ponktacology.clashmc.core.chat.staff.request.announcer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.chat.staff.request.StaffRequest;
import me.ponktacology.clashmc.core.chat.staff.request.announcer.packet.PacketStaffRequestAnnounce;
import me.ponktacology.clashmc.core.configuration.CoreConfiguration;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;


@RequiredArgsConstructor
public class StaffRequestAnnouncer implements Announcer<StaffRequest, PacketStaffRequestAnnounce> {


  private final NetworkService networkService;

  private final CorePlayerCache playerCache;

  private final CoreConfiguration configuration;

  @Override
  public void announce( StaffRequest request) {
    CorePlayer corePlayer = request.getSender();
    String formattedName = corePlayer.getFormattedName();
    String formatted =
        "&7(&bRequest&7) &7(&f"
            + this.configuration.getServerName()
            + "&7) "
            + formattedName
            + "&8: &f"
            + request.getMessage();

    this.networkService.publish(new PacketStaffRequestAnnounce(corePlayer.getUuid(), formatted));
  }

  @Override
  public void handle( PacketStaffRequestAnnounce packet) {
    String message = Text.colored(packet.getMessage());
    for (CorePlayer player : this.playerCache.staffMembers()) {
      player.sendMessage(message);
    }
  }

  @RequiredArgsConstructor
  public static class RequestAnnounceListener implements PacketListener {


    private final StaffRequestAnnouncer announcer;

    @PacketHandler
    public void onRequestAnnounce( PacketStaffRequestAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}
