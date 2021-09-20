package me.ponktacology.clashmc.drop.turbo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.Expiring;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Turbo implements Expiring {

  private long startTimeStamp;
  private long duration;

  public void setDuration(long duration) {
    this.startTimeStamp = System.currentTimeMillis();
    this.duration = duration;
  }

  @Override
  public boolean hasExpired() {
    return this.startTimeStamp + this.duration < System.currentTimeMillis();
  }
}
