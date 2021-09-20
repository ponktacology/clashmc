package me.ponktacology.clashmc.sector.api.sector.time;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.sector.api.sector.time.packet.PacketTimeUpdate;


@RequiredArgsConstructor
public class TimeUpdater implements Updater {


  private final NetworkService networkService;


  public void update(Long time) {
    networkService.publish(new PacketTimeUpdate(time));
  }
}
