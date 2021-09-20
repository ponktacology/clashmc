package me.ponktacology.clashmc.core.player.privatemessage.listener;

import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.privatemessage.event.PlayerPrivateMessageEvent;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;


public class PlayerPrivateMessageListener implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onPlayerPrivateMessageEvent( PlayerPrivateMessageEvent event) {
    CorePlayer sender = event.getSender();
    CorePlayer receiver = event.getReceiver();

    if (sender.isIgnored(receiver)) {
      sender.sendMessage(
          Text.colored("&cNie możesz wysłać wiadomości do gracza, którego ignorujesz."));
      event.setCancelled(true);
      return;
    }

    if (receiver.isIgnored(sender)) {
      sender.sendMessage(Text.colored("&cTen gracz ma wyłączone prywatne wiadomości."));
      event.setCancelled(true);
    }
  }
}
