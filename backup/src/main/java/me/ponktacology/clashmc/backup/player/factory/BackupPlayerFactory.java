package me.ponktacology.clashmc.backup.player.factory;

import me.ponktacology.clashmc.backup.player.BackupPlayer;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;

public class BackupPlayerFactory extends BukkitPlayerFactory<BackupPlayer> {
  public BackupPlayerFactory() {
    super(BackupPlayer.class, BackupPlayer::new);
  }
}
