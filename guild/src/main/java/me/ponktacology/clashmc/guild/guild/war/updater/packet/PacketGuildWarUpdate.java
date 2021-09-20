package me.ponktacology.clashmc.guild.guild.war.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;
import me.ponktacology.clashmc.guild.guild.war.wrapper.GuildWarWrapper;

@Data
@PacketManifest(channel = "packet-guild-war-update")
public class PacketGuildWarUpdate implements PacketUpdate<GuildWar> {
  private final GuildWarWrapper wrapper;
}
