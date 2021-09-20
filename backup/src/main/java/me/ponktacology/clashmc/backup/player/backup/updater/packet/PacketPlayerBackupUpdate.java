package me.ponktacology.clashmc.backup.player.backup.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackup;
import me.ponktacology.clashmc.backup.player.backup.PlayerBackupUpdate;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-player-backup-update")
public class PacketPlayerBackupUpdate implements PacketUpdate<PlayerBackupUpdate> {

  private final UUID player;

  private final PlayerBackup backup;

  private final boolean restoreRank;
}
