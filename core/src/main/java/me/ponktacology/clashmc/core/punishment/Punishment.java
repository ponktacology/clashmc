package me.ponktacology.clashmc.core.punishment;

import lombok.Getter;
import me.ponktacology.clashmc.core.managable.Manageable;

import java.util.UUID;

@Getter
public final class Punishment extends Manageable {

  private final UUID uuid;
  private final PunishmentType type;
  private final boolean silent;

  public Punishment(
      UUID uuid,
      UUID addedBy,
      PunishmentType type,
      String reason,
      long addedOn,
      long duration,
      boolean silent) {
    super(addedBy, reason, addedOn, duration);
    this.uuid = uuid;
    this.type = type;
    this.silent = silent;
  }
}
