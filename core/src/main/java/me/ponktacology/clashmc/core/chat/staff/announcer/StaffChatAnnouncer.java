package me.ponktacology.clashmc.core.chat.staff.announcer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.core.chat.staff.StaffMessage;
import me.ponktacology.clashmc.core.chat.staff.packet.PacketStaffChatAnnounce;
import me.ponktacology.clashmc.core.configuration.CoreConfiguration;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;


@RequiredArgsConstructor
public class StaffChatAnnouncer implements Announcer<StaffMessage, PacketStaffChatAnnounce> {


  private final CorePlayerCache playerCache;

  private final CoreConfiguration configuration;

  private final NetworkService networkService;

  @Override
  public void announce( StaffMessage event) {
    CorePlayer corePlayer = event.getSender();
    String formattedName = corePlayer.getFormattedName();
    String formatted =
        "&7(&dStaffChat&7) &7(&f"
            + this.configuration.getServerName()
            + "&7) "
            + formattedName
            + "&8: &f"
            + event.getMessage();

    this.networkService.publish(new PacketStaffChatAnnounce(corePlayer.getUuid(), formatted));
  }

  @Override
  public void handle( PacketStaffChatAnnounce packet) {
    String message = Text.colored(packet.getMessage());

    for (CorePlayer corePlayer : this.playerCache.staffMembers()) {
      corePlayer.sendMessage(message);
    }
  }

  @RequiredArgsConstructor
  public static class StaffChatAnnounceListener implements PacketListener {


    private final StaffChatAnnouncer announcer;

    @PacketHandler
    public void onStaffChatAnnounce( PacketStaffChatAnnounce packet) {
      this.announcer.handle(packet);
    }
  }
}
