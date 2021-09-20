package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import me.ponktacology.clashmc.core.CorePlugin;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

@UtilityClass
public class ActionBarUtil {

  private static final Map<Player, BukkitTask> PENDING_MESSAGES = new HashMap<>();

  /**
   * Sends a message to the player's action bar.
   *
   * <p>The message will appear above the player's hot bar for 2 seconds and then fade away over 1
   * second.
   *
   * @param bukkitPlayer the player to send the message to.
   * @param message the message to send.
   */
  public static void sendActionBarMessage(Player bukkitPlayer, String message) {
    sendRawActionBarMessage(bukkitPlayer, "{\"text\": \"" + message + "\"}");
  }

  /**
   * Sends a raw message (JSON format) to the player's action bar. Note: while the action bar
   * accepts raw messages it is currently only capable of displaying text.
   *
   * <p>The message will appear above the player's hot bar for 2 seconds and then fade away over 1
   * second.
   *
   * @param bukkitPlayer the player to send the message to.
   * @param rawMessage the json format message to send.
   */
  public static void sendRawActionBarMessage(Player bukkitPlayer, String rawMessage) {
    CraftPlayer player = (CraftPlayer) bukkitPlayer;
    IChatBaseComponent chatBaseComponent = ChatSerializer.a(rawMessage);
    PacketPlayOutChat packetPlayOutChat = new PacketPlayOutChat(chatBaseComponent, (byte) 2);
    player.getHandle().playerConnection.sendPacket(packetPlayOutChat);
  }

  public static void sendActionBarMessage(
      final Player bukkitPlayer, final String message, final int duration) {
    cancelPendingMessages(bukkitPlayer);
    final BukkitTask messageTask =
        new BukkitRunnable() {
          private int count = 0;

          @Override
          public void run() {
            if (count >= duration) {
              this.cancel();
              return;
            }
            sendActionBarMessage(bukkitPlayer, message);
            count++;
          }
        }.runTaskTimer(CorePlugin.INSTANCE.getPlugin(), 0L, 20L);
    PENDING_MESSAGES.put(bukkitPlayer, messageTask);
  }

  private static void cancelPendingMessages(Player bukkitPlayer) {
    if (PENDING_MESSAGES.containsKey(bukkitPlayer)) {
      PENDING_MESSAGES.get(bukkitPlayer).cancel();
    }
  }
}
