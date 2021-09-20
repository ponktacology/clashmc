package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;

@Getter
public class GuildWarActionWrapper extends Action {

  private final String guild;
  private final String enemy;

  public GuildWarActionWrapper(ActionType type, String guild, String enemy) {
    super(type);
    this.guild = guild;
    this.enemy = enemy;
  }
}
