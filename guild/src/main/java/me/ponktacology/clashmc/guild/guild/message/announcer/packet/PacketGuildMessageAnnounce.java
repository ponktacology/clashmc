package me.ponktacology.clashmc.guild.guild.message.announcer.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.announcer.packet.PacketAnnounce;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.guild.guild.message.GuildMessage;


import java.util.UUID;

@Data
@PacketManifest(channel = "packet-guild-message")
public class PacketGuildMessageAnnounce implements PacketAnnounce {

  private final UUID sender;

  private final String guild;

  private final String message;

  private final GuildMessage.MessageType messageType;
}
