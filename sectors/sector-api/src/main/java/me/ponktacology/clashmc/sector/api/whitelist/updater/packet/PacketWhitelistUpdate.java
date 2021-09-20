package me.ponktacology.clashmc.sector.api.whitelist.updater.packet;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.sector.api.whitelist.WhitelistSettings;

@RequiredArgsConstructor
@Getter
@PacketManifest(channel = "packet-whitelist-update")
public class PacketWhitelistUpdate implements PacketUpdate<WhitelistSettings> {}
