package me.ponktacology.clashmc.guild.recipe.armor.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;

@PacketManifest(channel = "packet-armor-settings-update")
public class PacketArmorSettingsUpdate implements PacketUpdate<ArmorSettings> {
}
