package me.ponktacology.clashmc.auth.player.factory;

import me.ponktacology.clashmc.auth.player.AuthPlayer;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;

public class AuthPlayerFactory extends BukkitPlayerFactory<AuthPlayer> {

  public AuthPlayerFactory() {
    super(AuthPlayer.class, AuthPlayer::new);
  }
}
