package me.ponktacology.clashmc.effect.effect.updater.packet;

import lombok.Data;
import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.effect.effect.Effect;


@Data
@PacketManifest(channel = "packet-effect-remove")
public class PacketEffectRemove implements PacketUpdate<Effect> {

    private final String name;
}
