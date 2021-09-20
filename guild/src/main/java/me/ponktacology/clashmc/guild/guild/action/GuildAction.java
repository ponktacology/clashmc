package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;

@Getter
public class GuildAction extends Action {

  private final GuildPlayer player;
  private final Guild guild;

  public GuildAction(ActionType type, GuildPlayer player, Guild guild) {
    super(type);
    this.player = player;
    this.guild = guild;
  }
}
