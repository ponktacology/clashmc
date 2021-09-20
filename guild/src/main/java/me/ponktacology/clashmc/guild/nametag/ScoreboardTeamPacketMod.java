package me.ponktacology.clashmc.guild.nametag;

import net.minecraft.server.v1_8_R3.PacketPlayOutScoreboardTeam;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;



import java.lang.reflect.Field;
import java.util.Collection;

public final class ScoreboardTeamPacketMod {
    private final PacketPlayOutScoreboardTeam packet=new PacketPlayOutScoreboardTeam();
    private static Field aField;
    private static Field bField;
    private static Field cField;
    private static Field dField;
    private static Field eField;
    private static Field fField;
    private static Field gField;

    public ScoreboardTeamPacketMod(String name, String prefix, String suffix, Collection players, int paramInt) {
        try {
            aField.set(this.packet, name);
            fField.set(this.packet, paramInt);
            if (paramInt == 0 || paramInt == 2) {
                bField.set(this.packet, name);
                cField.set(this.packet, prefix);
                dField.set(this.packet, suffix);
                gField.set(this.packet, 3);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (paramInt == 0) {
            this.addAll(players);
        }
    }

    public ScoreboardTeamPacketMod(String name, Collection players, int paramInt) {
        try {
            gField.set(this.packet, 3);
            aField.set(this.packet, name);
            fField.set(this.packet, paramInt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.addAll(players);
    }

    public void sendToPlayer( Player bukkitPlayer) {
        ((CraftPlayer) bukkitPlayer).getHandle().playerConnection.sendPacket(this.packet);
    }

    private void addAll( Collection col) {
        if (col == null) {
            return;
        }
        try {
            ((Collection) eField.get(this.packet)).addAll(col);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            aField=PacketPlayOutScoreboardTeam.class.getDeclaredField("a");
            bField=PacketPlayOutScoreboardTeam.class.getDeclaredField("b");
            cField=PacketPlayOutScoreboardTeam.class.getDeclaredField("c");
            dField=PacketPlayOutScoreboardTeam.class.getDeclaredField("d");
            eField=PacketPlayOutScoreboardTeam.class.getDeclaredField("g");
            fField=PacketPlayOutScoreboardTeam.class.getDeclaredField("h");
            gField=PacketPlayOutScoreboardTeam.class.getDeclaredField("i");
            aField.setAccessible(true);
            bField.setAccessible(true);
            cField.setAccessible(true);
            dField.setAccessible(true);
            eField.setAccessible(true);
            fField.setAccessible(true);
            gField.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

