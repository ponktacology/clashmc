package me.ponktacology.clashmc.sector.api.player.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.player.factory.PlayerFactory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.sector.api.player.SectorPlayer;
import org.bson.Document;


import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class SectorPlayerFactory implements PlayerFactory<SectorPlayer> {


  private final DataService dataService;

  @Override
  public Optional<SectorPlayer> load(String name) {
    return this.dataService.load(
        new Document("name", new Document("$regex", name).append("$options", "i")), SectorPlayer.class);
  }

  @Override
  public Optional<SectorPlayer> load( UUID uuid) {
    return this.dataService.load(uuid.toString(), SectorPlayer.class);
  }

  public SectorPlayer loadOrCreate( UUID uuid, String name) {
    return load(uuid)
        .orElseGet(
            () -> {
              SectorPlayer sectorPlayer = create(uuid, name);

              sectorPlayer.save(dataService);
              return sectorPlayer;
            });
  }


  @Override
  public SectorPlayer create(UUID uuid, String name) {
    return new SectorPlayer(uuid, name);
  }
}
