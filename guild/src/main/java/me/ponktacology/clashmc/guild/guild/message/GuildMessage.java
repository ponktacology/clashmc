package me.ponktacology.clashmc.guild.guild.message;

import lombok.Data;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;


@Data
public class GuildMessage {

  private final GuildPlayer sender;

  private final Guild guild;

  private final String message;

  public enum MessageType {
    GUILD,
    ALLY
  }
}
