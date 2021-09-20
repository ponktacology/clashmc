package me.ponktacology.clashmc.guild.guild.leave.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.leave.GuildLeave;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-guild-leave-update")
public class PacketGuildLeaveUpdate implements PacketUpdate<GuildLeave> {

  private final UUID player;

  private final String guild;
  private final boolean kick;
}
