package me.ponktacology.clashmc.guild.recipe.armor.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.guild.recipe.armor.ArmorSettings;
import org.bukkit.Material;

@Data
@PacketManifest(channel = "packet-armor-settings-state")
public class PacketArmorSettingsState implements PacketUpdate<ArmorSettings> {

    private final Material material;
    private final boolean state;
}
