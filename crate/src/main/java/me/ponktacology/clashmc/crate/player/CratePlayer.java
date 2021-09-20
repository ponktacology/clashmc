package me.ponktacology.clashmc.crate.player;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.crate.player.statistics.CratePlayerStatistics;



import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "crate")
public class CratePlayer extends BukkitPlayerWrapper {

  private final CratePlayerStatistics statistics = new CratePlayerStatistics();

  public CratePlayer(UUID uuid,  String name) {
    super(uuid, name);
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    CratePlayer that = (CratePlayer) o;

    return this.getUuid().equals(that.getUuid());
  }
}
