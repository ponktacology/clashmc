package me.ponktacology.clashmc.core.player.factory;

import me.ponktacology.clashmc.core.player.CorePlayer;


import java.util.Optional;
import java.util.UUID;

public final class CorePlayerFactory extends BukkitPlayerFactory<CorePlayer> {

  public CorePlayerFactory() {
    super(CorePlayer.class, CorePlayer::new);
  }

  public CorePlayer loadOrCreate( UUID uuid, String name, String ip) {
    Optional<CorePlayer> playerOptional = this.load(uuid);

    return playerOptional.orElseGet(
        () -> {
          CorePlayer player = this.create(uuid, name, ip);
          player.save();
          return player;
        });
  }

  public CorePlayer create(UUID uuid, String name, String ip) {
    CorePlayer corePlayer = super.create(uuid, name);

    corePlayer.addIp(ip);

    return corePlayer;
  }
}
