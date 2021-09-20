package me.ponktacology.clashmc.sector.proxy.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.sector.api.player.packet.PacketPlayerTransferRequest;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.proxy.util.ServerUtil;
import me.ponktacology.clashmc.sector.proxy.util.Text;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class PlayerTransferUpdateListener implements PacketListener {

  private final ProxyServer proxyServer;
  private final SectorCache sectorCache;

  @PacketHandler
  public void onPlayerTransferRequest( PacketPlayerTransferRequest packet) {
    ProxiedPlayer player = this.proxyServer.getPlayer(packet.getUuid());

    if (player == null) {
      log.info("Player " + packet.getUuid() + " has not been found.");
      return;
    }

    String sectorName = packet.getTo();

    Optional<Sector> sectorOptional = this.sectorCache.get(sectorName);

    if (!sectorOptional.isPresent()) {
      log.info("Sector " + packet.getTo() + " has not been found.");
      return;
    }

    Optional<ServerInfo> serverInfoOptional = ServerUtil.from(sectorOptional.get());

    if (!serverInfoOptional.isPresent()) {
      log.info("Server " + packet.getTo() + " has not been found.");
      return;
    }

    ServerUtil.connect(
        player,
        serverInfoOptional.get(),
        (success) -> {
          if (!success) {
            log.info(
                "Couldn't properly transfer player= "
                    + player.getName()
                    + ", to= "
                    + packet.getTo());
            player.disconnect(
                new TextComponent(
                    Text.colored(
                        "&cWystąpił błąd w trakcie przenoszenia na inny sektor.\nSpróbuj ponownie później lub skontaktuj się z administracją.")));
          }
        });
  }
}
