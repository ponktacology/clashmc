package me.ponktacology.clashmc.guild.player.combattag.global.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.player.combattag.global.CombatTagSettings;

@PacketManifest(channel = "packet-combat-tag-settings-update")
public class PacketCombatTagSettingsUpdate implements PacketUpdate<CombatTagSettings> {}
