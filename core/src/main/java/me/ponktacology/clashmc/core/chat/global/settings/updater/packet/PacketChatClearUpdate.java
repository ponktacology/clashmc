package me.ponktacology.clashmc.core.chat.global.settings.updater.packet;

import me.ponktacology.clashmc.api.service.network.packet.PacketManifest;
import me.ponktacology.clashmc.api.updater.packet.PacketUpdate;
import me.ponktacology.clashmc.core.chat.global.settings.ChatSettings;

@PacketManifest(channel = "packet-chat-clear-update")
public class PacketChatClearUpdate implements PacketUpdate<ChatSettings> {
}
