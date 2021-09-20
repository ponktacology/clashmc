package me.ponktacology.clashmc.backup.backup;

import lombok.Getter;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.api.util.TimeUtil;


@Getter
public abstract class Backup implements Expiring {

  private final long timeStamp = System.currentTimeMillis();


  public String getFormattedDate() {
    return TimeUtil.formatTimeMillisToDate(this.timeStamp);
  }
}
