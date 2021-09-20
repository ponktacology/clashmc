package me.ponktacology.clashmc.core.managable;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.api.util.TimeUtil;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public abstract class Manageable implements Expiring {


  private final UUID addedBy;

  private final String reason;
  private final long addedOn;
  private final long duration;

  private String removeReason;
  private UUID removedBy;
  private boolean removed;
  private long removedOn;

  @Override
  public boolean hasExpired() {
    return this.removed || (this.duration > 0 && System.currentTimeMillis() - this.addedOn > this.duration);
  }

  public long getExpireDate() {
    return this.getDuration() > 0 ? this.getAddedOn() + this.getDuration() : this.getDuration();
  }


  public String getFormattedExpireDate() {
    return TimeUtil.formatTimeMillisToDate(this.getExpireDate());
  }


  public String getFormattedDuration() {
    return TimeUtil.formatTimeMillis(this.getDuration());
  }
}
