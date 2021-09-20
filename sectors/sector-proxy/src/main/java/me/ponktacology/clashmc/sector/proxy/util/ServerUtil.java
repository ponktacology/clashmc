package me.ponktacology.clashmc.sector.proxy.util;

import me.ponktacology.clashmc.api.Callback;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.proxy.SectorProxy;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;


import java.util.Optional;

public class ServerUtil {

  public static Optional<ServerInfo> from( Sector sector) {
    ServerInfo info = SectorProxy.INSTANCE.getProxy().getServerInfo(sector.getName());

    return Optional.ofNullable(info);
  }

  public static void connect(
           ProxiedPlayer player,  ServerInfo serverInfo,  Callback<Boolean> callback) {
    Server playerServer = player.getServer();

    if (playerServer != null) {
      if (playerServer.getInfo().getName().equals(serverInfo.getName())) return;
    }

    long start = System.currentTimeMillis();
    player.connect(
        serverInfo,
        (success, t) -> {
          if (success && t == null) {
            callback.accept(true);
            player.sendMessage(
                new TextComponent(
                    Text.colored(
                        "&aPomyślnie przeniesiono w "
                            + (System.currentTimeMillis() - start)
                            + " ms.")));
          } else {
            callback.accept(false);
            if (t != null) {
              t.printStackTrace();
            }
          }
        });
  }
}
