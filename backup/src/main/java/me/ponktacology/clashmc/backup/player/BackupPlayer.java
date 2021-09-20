package me.ponktacology.clashmc.backup.player;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;


import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Getter
@Setter
@ToString
@Entity(collection = "players", database = "backup")
public class BackupPlayer extends BukkitPlayerWrapper {

  private final LinkedList<PlayerBackup> backups = Lists.newLinkedList();

  public BackupPlayer(UUID uuid,  String name) {
    super(uuid, name);
  }

  public void addBackup(PlayerBackup backup) {
    this.backups.addFirst(backup);
  }

  private void checkForEviction() {
    this.backups.removeIf(PlayerBackup::hasExpired);
  }


  public List<PlayerBackup> getBackups() {
    this.checkForEviction();

    return Collections.unmodifiableList(this.backups);
  }
}
