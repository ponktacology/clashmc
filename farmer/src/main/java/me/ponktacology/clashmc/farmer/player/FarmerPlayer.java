package me.ponktacology.clashmc.farmer.player;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.farmer.farmer.filler.region.FarmerRegion;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "kit")
public class FarmerPlayer extends BukkitPlayerWrapper {

  private final Map<String, Long> lastKitUseTimeStamp = Maps.newHashMap();

  public FarmerPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  private Location firstCorner;
  private Location secondCorner;

  public void setFirstCorner(Location location) {
    this.firstCorner = location;
  }

  public void setSecondCorner(Location location) {
    this.secondCorner = location;
  }

  public boolean isFarmerRegionReady() {
    return this.firstCorner != null && this.secondCorner != null;
  }

  public FarmerRegion farmerRegion() {
    return FarmerRegion.fromCorners(this.firstCorner, this.secondCorner);
  }

  public int farmerRegionPrice() {
    return this.farmerRegion().price();
  }

  private void applyCorner(FarmerRegion region, Location corner) {
    region.setX(corner.getBlockX());
    region.setY(corner.getBlockY());
    region.setZ(corner.getBlockZ());
  }
}
