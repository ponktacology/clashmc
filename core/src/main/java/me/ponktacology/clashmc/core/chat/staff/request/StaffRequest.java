package me.ponktacology.clashmc.core.chat.staff.request;

import lombok.Data;
import me.ponktacology.clashmc.core.player.CorePlayer;


@Data
public class StaffRequest {

  private final CorePlayer sender;

  private final String message;
}
