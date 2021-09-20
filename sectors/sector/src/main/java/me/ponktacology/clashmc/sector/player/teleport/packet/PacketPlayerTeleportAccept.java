package me.ponktacology.clashmc.sector.player.teleport.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.player.teleport.TeleportRequest;
import org.bukkit.Location;

import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-teleport-accept")
public class PacketPlayerTeleportAccept implements PacketUpdate<TeleportRequest> {

  private final UUID sender;
  private final UUID receiver;
  private final String sector;
  private final Location location;
}
