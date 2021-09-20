package me.ponktacology.clashmc.sector.player.teleport.request;

import lombok.Getter;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.sector.player.teleport.TeleportRequest;
import org.bukkit.Location;

@Getter
public class LocationTeleportRequest extends TeleportRequest {

  private final Location location;

  public LocationTeleportRequest(CorePlayer sender, Location location) {
    super(sender);
    this.location = location;
  }
}
