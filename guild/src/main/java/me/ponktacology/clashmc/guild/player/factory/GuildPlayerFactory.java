package me.ponktacology.clashmc.guild.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.guild.player.GuildPlayer;

public class GuildPlayerFactory extends BukkitPlayerFactory<GuildPlayer> {
  public GuildPlayerFactory() {
    super(GuildPlayer.class, GuildPlayer::new);
  }
}
