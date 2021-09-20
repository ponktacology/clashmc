package me.ponktacology.clashmc.guild.guild.invite.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.invite.GuildInvite;


import java.util.UUID;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-guild-invite-update")
public class PacketGuildInviteUpdate implements PacketUpdate<GuildInvite> {

  private final UUID player;

  private final String guild;
  private final boolean accepted;
}
