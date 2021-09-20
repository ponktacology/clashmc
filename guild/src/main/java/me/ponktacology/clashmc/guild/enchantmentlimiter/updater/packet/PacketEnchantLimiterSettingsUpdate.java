package me.ponktacology.clashmc.guild.enchantmentlimiter.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.enchantmentlimiter.EnchantLimiterSettings;

@PacketManifest(channel = "packet-enchant-limiter-settings-update")
public class PacketEnchantLimiterSettingsUpdate implements PacketUpdate<EnchantLimiterSettings> {
}
