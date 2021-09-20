package me.ponktacology.clashmc.drop.turbo.global.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.drop.turbo.global.GlobalTurbo;

@PacketManifest(channel = "packet-global-turbo-update")
public class PacketGlobalTurboUpdate implements PacketUpdate<GlobalTurbo> {}
