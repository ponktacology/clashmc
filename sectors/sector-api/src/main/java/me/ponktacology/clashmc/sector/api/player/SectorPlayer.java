package me.ponktacology.clashmc.sector.api.player;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ponktacology.clashmc.api.player.PlayerWrapper;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.sector.api.player.info.PlayerInfo;
import me.ponktacology.clashmc.sector.api.player.inventory.update.InventoryUpdate;
import me.ponktacology.clashmc.sector.api.sector.Sector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@ToString
@Setter
@Entity(collection = "players", database = "sector")
public class SectorPlayer extends PlayerWrapper {

  private PlayerInfo info;
  private String sector;
  private boolean transfer;
  private boolean hasJoinedBefore;
  private final List<InventoryUpdate> pendingUpdates = new ArrayList<>();

  public SectorPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public void save(DataService dataService) {
    dataService.save(this);
  }

  public void addUpdate(InventoryUpdate update) {
    this.pendingUpdates.add(update);
  }

  public void clearUpdates() {
    this.pendingUpdates.clear();
  }

  public void setSector(String sector) {
    this.sector = sector;
  }

  public void setSector(Sector sector) {
    this.setSector(sector.getName());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    SectorPlayer that = (SectorPlayer) o;

    return this.getUuid().equals(that.getUuid());
  }
}
