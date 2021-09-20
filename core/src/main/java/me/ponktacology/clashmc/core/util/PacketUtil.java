package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import net.minecraft.server.v1_8_R3.Packet;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;


import java.util.Set;

@UtilityClass
public class PacketUtil {

    public static void sendPacketExcept(Player except,  Packet<?>... packets) {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            if (player.equals(except)) continue;
            for (Packet<?> packet : packets) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public static void sendPacket( Set<Player> players,  Packet<?>... packets) {
        for (Player player : players) {
            for (Packet<?> packet : packets) {
                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        }
    }

    public static void sendPacket( Player player,  Packet<?>... packets) {
        for (Packet<?> packet : packets) {
            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
