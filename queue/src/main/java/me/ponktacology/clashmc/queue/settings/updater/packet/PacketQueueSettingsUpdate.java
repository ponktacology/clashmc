package me.ponktacology.clashmc.queue.settings.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.queue.settings.QueueSettings;

@PacketManifest(channel = "packet-queue-settings-update")
public class PacketQueueSettingsUpdate implements PacketUpdate<QueueSettings> {
}
