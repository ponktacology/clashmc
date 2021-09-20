package me.ponktacology.clashmc.guild.guild.war.action.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.guild.guild.action.ActionType;

@Getter
@RequiredArgsConstructor
@PacketManifest(channel = "packet-guild-war-action-announce")
public class PacketGuildWarActionAnnounce implements PacketAnnounce {

  private final ActionType type;
  private final String guild;
  private final String enemy;
}
