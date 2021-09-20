package me.ponktacology.clashmc.sector.sector.time;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketListener;
import me.ponktacology.clashmc.sector.api.sector.time.packet.PacketTimeUpdate;
import org.bukkit.Bukkit;
import org.bukkit.World;


@RequiredArgsConstructor
public class TimeUpdateListener implements PacketListener {


  private final TaskDispatcher taskDispatcher;

  @PacketHandler
  public void onTimeUpdate( PacketTimeUpdate packet) {
    taskDispatcher.run(
        () -> {
          for (World world : Bukkit.getServer().getWorlds()) {
            world.setTime(packet.getTime());
          }
        });
  }
}
