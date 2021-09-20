package me.ponktacology.clashmc.core.log.chat.menu.button;

import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.log.chat.ChatLog;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ChatLogButton extends Button {

  private final ChatLog chatLog;

  public ChatLogButton(ChatLog chatLog) {
    this.chatLog = chatLog;
  }

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.PAPER)
        .name("&f" + chatLog.getCommand())
        .lore("&eData: &f" + TimeUtil.formatTimeMillisToDate(chatLog.getTimeStamp()))
        .build();
  }
}
