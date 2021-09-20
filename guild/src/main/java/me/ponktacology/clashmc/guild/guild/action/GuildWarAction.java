package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;
import me.ponktacology.clashmc.guild.guild.Guild;

@Getter
public class GuildWarAction extends Action {

  private final Guild guild;
  private final Guild enemy;

  public GuildWarAction(ActionType type, Guild guild, Guild enemy) {
    super(type);
    this.guild = guild;
    this.enemy = enemy;
  }
}
