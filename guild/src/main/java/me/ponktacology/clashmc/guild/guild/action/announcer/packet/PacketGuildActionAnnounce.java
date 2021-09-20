package me.ponktacology.clashmc.guild.guild.action.announcer.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.guild.guild.action.ActionType;


import java.util.UUID;

@Getter
@RequiredArgsConstructor
@PacketManifest(channel = "packet-guild-action-announce")
public class PacketGuildActionAnnounce implements PacketAnnounce {


  private final ActionType type;

  private final String guild;

  private final UUID player;
}
