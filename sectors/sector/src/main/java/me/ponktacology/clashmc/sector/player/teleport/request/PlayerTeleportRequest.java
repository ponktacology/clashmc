package me.ponktacology.clashmc.sector.player.teleport.request;

import lombok.Getter;
import lombok.ToString;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.sector.player.teleport.TeleportRequest;

@Getter
@ToString
public class PlayerTeleportRequest extends TeleportRequest {

  private final CorePlayer receiver;

  public PlayerTeleportRequest(CorePlayer sender, CorePlayer receiver) {
    super(sender);
    this.receiver = receiver;
  }


}
