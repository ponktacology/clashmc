package me.ponktacology.clashmc.sector.api.sector;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum SectorType {
  DEFAULT,
  SPAWN,
  NETHER,
  END,
  EVENT,
  LOBBY,
  AUTH;

  public boolean isSpecial() {
    return this == LOBBY || this == AUTH || this == EVENT;
  }
}
