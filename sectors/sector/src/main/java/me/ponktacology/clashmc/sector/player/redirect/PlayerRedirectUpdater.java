package me.ponktacology.clashmc.sector.player.redirect;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.sector.api.redirect.PacketPlayerRedirectUpdate;


import java.util.UUID;

@RequiredArgsConstructor
public class PlayerRedirectUpdater implements Updater {


  private final NetworkService networkService;

  
  public void update(UUID uuid) {
    this.networkService.publish(new PacketPlayerRedirectUpdate(uuid));
  }
}
