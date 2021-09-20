package me.ponktacology.clashmc.sector.proxy.player.listener;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.util.MojangUtil;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;
import me.ponktacology.clashmc.sector.api.motd.cache.MotdSettingsCache;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import me.ponktacology.clashmc.sector.api.player.factory.SectorPlayerFactory;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;
import me.ponktacology.clashmc.sector.api.whitelist.cache.WhitelistSettingsCache;
import me.ponktacology.clashmc.sector.proxy.SectorProxy;
import me.ponktacology.clashmc.sector.proxy.SectorProxyConstants;
import me.ponktacology.clashmc.sector.proxy.configuration.SectorConfiguration;
import me.ponktacology.clashmc.sector.proxy.player.updater.PlayerUpdater;
import me.ponktacology.clashmc.sector.proxy.util.ServerUtil;
import me.ponktacology.clashmc.sector.proxy.util.Text;
import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
public class PlayerListener implements Listener {

  private final WhitelistSettingsCache whitelistSettingsCache;
  private final SectorConfiguration configuration;
  private final SectorCache sectorCache;
  private final PlayerUpdater playerUpdater;
  private final TaskDispatcher taskDispatcher;
  private final SectorPlayerFactory playerFactory;
  private final MotdSettingsCache motdSettingsCache;
  private final DataService dataService;
  private final Pattern NAME_REGEX = Pattern.compile("[a-zA-Z0-9_]{3,16}");

  @EventHandler
  public void onPreLoginEvent(PreLoginEvent event) {
    PendingConnection pendingConnection = event.getConnection();
    String name = pendingConnection.getName();

    event.registerIntent(SectorProxy.INSTANCE.getPlugin());

    this.taskDispatcher.runAsync(
        () -> {
          try {
            boolean isAlreadyOnline = this.playerUpdater.get(name);

            log.info("Player " + isAlreadyOnline);
            if (isAlreadyOnline) {
              event.setCancelReason(Text.colored("&cGracz o takim nicku jest już online."));
              event.setCancelled(true);
              return;
            }

            boolean isPremium = MojangUtil.getUuid(name, true).isPresent();

            if (!isPremium) {
              if (!NAME_REGEX.matcher(name).matches()) {
                event.setCancelReason(Text.colored("&cTwój nick zawiera niedozwolone znaki."));
                event.setCancelled(true);
                event.completeIntent(SectorProxy.INSTANCE.getPlugin());
                return;
              }
            }

            pendingConnection.setOnlineMode(isPremium);

            log.info("Player= " + name + ", premium= " + isPremium);
          } finally {
            event.completeIntent(SectorProxy.INSTANCE.getPlugin());
          }
        });
  }

  @EventHandler
  public void onLoginEvent(LoginEvent event) {
    WhitelistSettings whitelist = whitelistSettingsCache.get();

    event.registerIntent(SectorProxy.INSTANCE.getPlugin());

    this.taskDispatcher.runAsync(
        () -> {
          try {
            PendingConnection connection = event.getConnection();

            final UUID uuid = connection.getUniqueId();
            final String name = connection.getName();
            if (whitelist.isEnabled() && !whitelist.hasPlayer(uuid)) {
              event.setCancelReason(Text.colored(whitelist.getMessage()));
              event.setCancelled(true);
              return;
            }

            long lastJoin = System.currentTimeMillis() - this.playerUpdater.getLastJoin(name);

            if (lastJoin < SectorProxyConstants.JOIN_THROTTLE) {
              int lastJoinSeconds = (int) (lastJoin / 1000L);
              int throttleSeconds = (int) (SectorProxyConstants.JOIN_THROTTLE / 1000L);
              event.setCancelReason(
                  new TextComponent(
                      Text.colored(
                          "&cOdczekaj "
                              + (throttleSeconds - lastJoinSeconds)
                              + "s zanim znowu dołączysz na serwer.")));
              event.setCancelled(true);
              return;
            }

            this.playerUpdater.updateLastJoin(name);

            if (!connection.isOnlineMode()) {
              Optional<SectorPlayer> sectorPlayerOptional =
                  this.playerFactory.load(connection.getName());

              if (sectorPlayerOptional.isPresent()) {
                SectorPlayer sectorPlayer = sectorPlayerOptional.get();

                if (!sectorPlayer.getName().equals(connection.getName())) {
                  event.setCancelReason(
                      Text.colored("&cDołącz na nicku &f" + sectorPlayer.getName() + "&c."));
                  event.setCancelled(true);
                }
              }
            }
          } finally {
            log.info("COMPLETED INTENT 1");
            event.completeIntent(SectorProxy.INSTANCE.getPlugin());
          }
        });
  }

  @EventHandler
  public void onProxyPingEvent(ProxyPingEvent event) {
    ServerPing serverPing = event.getResponse();
    MotdSettings motdSettings = this.motdSettingsCache.get();

    serverPing.setDescriptionComponent(
        new TextComponent(Text.colored(motdSettings.getFormattedMotd())));

    int playerCount = this.sectorCache.getNormalPlayersCount();

    serverPing.setVersion(
        new ServerPing.Protocol(
            Text.colored(
                "                                                                &4BlazingPack 1.8.8"),
            47));

    List<String> lines =
        Arrays.asList(
            "&8       &c&m-----&8&m[--&c&l Clash&f&lMC.pl &8&m--]&c&m-----",
            "",
            "&cLicznik nie pokaże więcej niż &f2000 &cgraczy.",
            "",
            "&cStrona: &fwww.clashmc.pl",
            "&cFacebook: &ffb.clashmc.pl",
            "&CTeamSpeak: &fts.clashmc.pl",
            "&cDiscord: &fdc.clashmc.pl",
            "",
            "&cProxy: &f" + this.configuration.getProxyName(),
            "");

    ServerPing.PlayerInfo[] serverPings = new ServerPing.PlayerInfo[lines.size()];

    for (int i = 0; i < serverPings.length; i++) {
      serverPings[i] = new ServerPing.PlayerInfo(Text.colored(lines.get(i)), String.valueOf(i));
    }

    serverPing.setPlayers(
        new ServerPing.Players(2000, this.sectorCache.getMaskedPlayersCount(), serverPings));
  }

  @SneakyThrows
  @EventHandler
  public void onServerConnectEvent(ServerConnectEvent event) {
    ProxiedPlayer player = event.getPlayer();

    if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
      PendingConnection connection = player.getPendingConnection();

      if (connection.isOnlineMode() || this.configuration.getTestMode()) {
        ServerInfo lobbyServer = SectorProxy.INSTANCE.getProxy().getServerInfo("lobby");

        if (lobbyServer == null) {
          log.info("Couldn't find lobby server, redirecting...");
          this.taskDispatcher.runAsync(() -> redirectAndUpdate(player));
          return;
        }

        if (this.configuration.getTestMode()) {
          redirectAndUpdate(player);
        } else event.setTarget(lobbyServer);
      } else {
        ServerInfo authServer = SectorProxy.INSTANCE.getProxy().getServerInfo("auth");

        if (authServer == null) {
          log.info("Couldn't find auth server, disconnecting...");
          player.disconnect(
              new TextComponent(
                  Text.colored(
                      "&cNie udało się połączyć z serwerem autoryzacyjnym.\nSpróbuj ponownie później lub skontaktuj się z administracją.")));
          return;
        }

        event.setTarget(authServer);
      }
    }
  }

  @EventHandler
  public void onPlayerKickEvent(ServerKickEvent event) {
    ProxiedPlayer player = event.getPlayer();

    if (event.getKickReason().contains("Aktualnie trwa restart sektora")
        && !event.getKickedFrom().getName().equals("lobby")) {

      ServerInfo lobbyServer = SectorProxy.INSTANCE.getProxy().getServerInfo("lobby");

      player.sendMessage(Text.colored("&eSektor na którym byłeś jest aktualnie restartowany."));

      event.setCancelled(true);
      event.setCancelServer(lobbyServer);
    }
  }

  @EventHandler
  public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
    ProxiedPlayer player = event.getPlayer();

    this.taskDispatcher.runAsync(() -> this.playerUpdater.remove(player.getName()));
  }

  private Optional<ServerInfo> getRandomDefaultSector() {

    Optional<Sector> sectorOptional = this.sectorCache.getRandom(SectorType.DEFAULT);

    if (!sectorOptional.isPresent()) {
      return Optional.empty();
    }
    log.info("Sending to random " + sectorOptional.get().getName());
    return ServerUtil.from(sectorOptional.get());
  }

  private Optional<ServerInfo> getSpawnServer() {
    Optional<Sector> sectorOptional = this.sectorCache.getLeastCrowded(SectorType.SPAWN);

    if (!sectorOptional.isPresent()) return Optional.empty();

    return ServerUtil.from(sectorOptional.get());
  }

  public void redirectAndUpdate(ProxiedPlayer player) {
    SectorPlayer sectorPlayer =
        this.playerFactory.loadOrCreate(player.getUniqueId(), player.getName());

    Optional<ServerInfo> serverInfoOptional = this.redirect(sectorPlayer);

    if (!serverInfoOptional.isPresent()) {
      player.sendMessage(Text.colored("&cTwój sektor docelowy jest aktualnie offline."));
      return;
    }

    ServerInfo serverInfo = serverInfoOptional.get();

    ServerUtil.connect(
        player,
        serverInfo,
        (success) -> {
          if (success) {
            sectorPlayer.setSector(serverInfo.getName());
            sectorPlayer.save(this.dataService);

            this.playerUpdater.update(player.getName());
          }
        });
  }

  public Optional<ServerInfo> redirect(SectorPlayer sectorPlayer) {
    String sectorName = sectorPlayer.getSector();

    if (sectorName == null) {
      return getRandomDefaultSector();
    } else {
      if (sectorName.contains("spawn")) {
        return getSpawnServer();
      } else {
        Optional<Sector> sectorOptional = this.sectorCache.get(sectorName);

        if (!sectorOptional.isPresent()) {
          return Optional.empty();
        }

        Sector sector = sectorOptional.get();

        return ServerUtil.from(sector);
      }
    }
  }
}
