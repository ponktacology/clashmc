package me.ponktacology.clashmc.queue.entry;

import lombok.Data;
import me.ponktacology.clashmc.core.player.CorePlayer;



@Data
public class QueueEntry {


  private final CorePlayer corePlayer;
  private final long timeStamp = System.currentTimeMillis();

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    QueueEntry entry = (QueueEntry) o;

    return corePlayer != null ? corePlayer.equals(entry.corePlayer) : entry.corePlayer == null;
  }

  @Override
  public int hashCode() {
    return corePlayer != null ? corePlayer.hashCode() : 0;
  }
}
