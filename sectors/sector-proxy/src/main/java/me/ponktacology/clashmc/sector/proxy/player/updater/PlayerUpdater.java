package me.ponktacology.clashmc.sector.proxy.player.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.updater.Updater;

import java.util.Locale;

@Slf4j
@RequiredArgsConstructor
public class PlayerUpdater implements Updater {

  private final NetworkService networkService;

  public void update(String name) {
    set(name, true);
    log.info("ADDED PLAYER " + name);
  }

  public void remove(String name) {
    set(name, false);
    log.info("REMOVED PLAYER " + name);
  }

  public void updateLastJoin(String name) {
    this.networkService.set(
        "last-join", name.toLowerCase(Locale.ROOT), String.valueOf(System.currentTimeMillis()));
  }

  public long getLastJoin(String name) {
    return Long.parseLong(
        this.networkService.get("last-join", name.toLowerCase(Locale.ROOT)).orElse("0"));
  }

  private void set(String name, boolean state) {
    this.networkService.set("player-online", name.toLowerCase(Locale.ROOT), String.valueOf(state));
  }

  public boolean get(String name) {
    return Boolean.parseBoolean(
        this.networkService.get("player-online", name.toLowerCase(Locale.ROOT)).orElse("false"));
  }
}
