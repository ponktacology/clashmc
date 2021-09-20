package me.ponktacology.clashmc.sector.player.transfer.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.event.BaseCancellableEvent;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import org.bukkit.Location;
import org.bukkit.entity.Player;


@Getter
@RequiredArgsConstructor
public class AsyncPlayerSectorTransferEvent extends BaseCancellableEvent {
  private final Player player;
  private final Sector sector;
  private final Location location;
}
