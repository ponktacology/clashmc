package me.ponktacology.clashmc.guild.guild.settings.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.guild.settings.GuildSettings;

@PacketManifest(channel = "packet-guild-settings-update")
public class PacketGuildSettingsUpdate implements PacketUpdate<GuildSettings> {
}
