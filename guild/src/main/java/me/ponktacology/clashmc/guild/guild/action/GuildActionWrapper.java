package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;

import java.util.UUID;

@Getter
public class GuildActionWrapper extends Action {

  private final UUID player;
  private final String guild;

  public GuildActionWrapper(ActionType type, UUID player, String guild) {
    super(type);
    this.player = player;
    this.guild = guild;
  }
}
