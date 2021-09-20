package me.ponktacology.clashmc.guild.guild.war.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;

@Data
@PacketManifest(channel = "packet-guild-war-remove")
public class PacketGuildWarRemove implements PacketUpdate<GuildWar> {
  private final String winner;
  private final String loser;
}
