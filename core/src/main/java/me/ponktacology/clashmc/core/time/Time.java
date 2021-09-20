package me.ponktacology.clashmc.core.time;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ponktacology.clashmc.api.util.TimeUtil;


@AllArgsConstructor
@Getter
public class Time {


  public static Time PERMANENT = new Time(-1L);

  private final long timeStamp;

  public Time( String timeStamp) {
    if ("perm".equalsIgnoreCase(timeStamp)) {
      this.timeStamp = -1L;
    } else {
      this.timeStamp = TimeUtil.parseTime(timeStamp);

      if (this.timeStamp == -1) {
        throw new IllegalArgumentException("Invalid time exception");
      }
    }
  }
}
