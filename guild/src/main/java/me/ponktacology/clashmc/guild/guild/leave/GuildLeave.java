package me.ponktacology.clashmc.guild.guild.leave;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;

@Data
@RequiredArgsConstructor
public class GuildLeave {

  private final GuildPlayer player;
  private final Guild guild;
  private final boolean kick;

  public GuildLeave(GuildPlayer player, Guild guild) {
    this.player = player;
    this.guild = guild;
    this.kick = false;
  }
}
