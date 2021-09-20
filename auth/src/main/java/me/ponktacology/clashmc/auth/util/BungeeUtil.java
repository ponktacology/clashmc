package me.ponktacology.clashmc.auth.util;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import me.ponktacology.clashmc.auth.AuthPlugin;
import org.bukkit.entity.Player;


public class BungeeUtil {

  public static void sendToServer( Player player,  String server) {
    ByteArrayDataOutput out = ByteStreams.newDataOutput();
    out.writeUTF("Connect");
    out.writeUTF(server);
    player.sendPluginMessage(AuthPlugin.INSTANCE.getPlugin(), "BungeeCord", out.toByteArray());
  }
}
