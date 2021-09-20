package me.ponktacology.clashmc.sector.proxy.player.redirect;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.sector.api.redirect.PacketPlayerRedirectUpdate;
import me.ponktacology.clashmc.sector.proxy.player.listener.PlayerListener;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Slf4j
@RequiredArgsConstructor
public class RedirectUpdateListener implements PacketListener {

  private final PlayerListener listener;

  @PacketHandler
  public void onRedirect(PacketPlayerRedirectUpdate packet) {
    ProxiedPlayer player = ProxyServer.getInstance().getPlayer(packet.getUuid());

    if (player == null) {
      log.info(
          "Received redirect update but player not found on the network, player= "
              + packet.getUuid());
      return;
    }

    this.listener.redirectAndUpdate(player);
  }
}
