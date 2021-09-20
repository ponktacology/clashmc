package me.ponktacology.clashmc.sector.player.teleport;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import me.ponktacology.clashmc.core.player.CorePlayer;

@Data
@AllArgsConstructor
@ToString
public class TeleportRequest {
  
  private final CorePlayer sender;
  private final long timeStamp = System.currentTimeMillis();

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    TeleportRequest that = (TeleportRequest) o;

    if (timeStamp != that.timeStamp) return false;
    return sender != null ? sender.equals(that.sender) : that.sender == null;
  }

  @Override
  public int hashCode() {
    int result = sender != null ? sender.hashCode() : 0;
    result = 31 * result + (int) (timeStamp ^ (timeStamp >>> 32));
    return result;
  }
}
