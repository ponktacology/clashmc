package me.ponktacology.clashmc.guild.guild.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.Guild;


@Data
@PacketManifest(channel = "packet-guild-remove")
public class PacketGuildRemove implements PacketUpdate<Guild> {

    
    private final String guild;
}
