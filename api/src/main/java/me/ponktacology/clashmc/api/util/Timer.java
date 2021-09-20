package me.ponktacology.clashmc.api.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
public class Timer {

  private long start = System.currentTimeMillis();
  private long delay;

  public Timer(long delay) {
    this.delay = delay;
  }

  public boolean hasPassed() {
    return System.currentTimeMillis() - start > this.delay;
  }

  public boolean hasPassed(long delay) {
    return System.currentTimeMillis() - start > delay;
  }

  public void reset() {
    start = System.currentTimeMillis();
  }
}
