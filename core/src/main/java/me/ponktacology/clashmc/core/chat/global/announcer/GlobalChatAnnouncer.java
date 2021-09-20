package me.ponktacology.clashmc.core.chat.global.announcer;

import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.core.chat.global.event.GlobalChatEvent;
import me.ponktacology.clashmc.core.chat.global.packet.PacketGlobalChatAnnounce;

public interface GlobalChatAnnouncer extends Announcer<GlobalChatEvent, PacketGlobalChatAnnounce> {}
