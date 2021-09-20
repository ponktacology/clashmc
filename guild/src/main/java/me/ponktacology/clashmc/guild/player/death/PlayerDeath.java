package me.ponktacology.clashmc.guild.player.death;

import lombok.Data;
import me.ponktacology.clashmc.guild.player.GuildPlayer;


@Data
public class PlayerDeath {

  private final GuildPlayer player;

  private final GuildPlayer killer;
  private final int rank;
}
