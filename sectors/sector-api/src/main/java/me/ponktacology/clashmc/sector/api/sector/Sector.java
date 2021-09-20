package me.ponktacology.clashmc.sector.api.sector;

import com.google.gson.annotations.SerializedName;
import lombok.*;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.sector.api.sector.data.SectorData;



import java.util.Objects;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity(collection = "sectors", database = "sector")
public class Sector {


  @SerializedName("_id")
  private final String name;

  private final SectorType type;
  private final SectorRegion region;
  private SectorData data;

  public boolean isSpawn() {
    return this.type == SectorType.SPAWN;
  }

  public boolean isSpecial() {
    return this.type.isSpecial();
  }

  public boolean isAvailable() {
    return this.data.isAvailable();
  }

  public void save( DataService dataService) {
    dataService.save(this);
  }

  public int getMaxPlayers() {
    if (this.data == null) return 0;

    return this.data.getMaxPlayers();
  }

  public int getOnlinePlayers() {
    if (this.data == null) return 0;

    return this.data.getPlayers();
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Sector sector = (Sector) o;

    return Objects.equals(name, sector.name);
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }
}
