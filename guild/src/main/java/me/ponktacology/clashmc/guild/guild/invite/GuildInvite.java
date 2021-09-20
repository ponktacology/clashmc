package me.ponktacology.clashmc.guild.guild.invite;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;

@Data
@RequiredArgsConstructor
public class GuildInvite {

  private final GuildPlayer player;
  private final Guild guild;
  private final boolean accepted;

  public GuildInvite(GuildPlayer player, Guild guild) {
    this.player = player;
    this.guild = guild;
    this.accepted = false;
  }
}
