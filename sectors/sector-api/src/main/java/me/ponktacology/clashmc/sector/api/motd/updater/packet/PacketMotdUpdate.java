package me.ponktacology.clashmc.sector.api.motd.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.api.motd.MotdSettings;

@PacketManifest(channel = "packet-motd-update")
public class PacketMotdUpdate implements PacketUpdate<MotdSettings> {}
