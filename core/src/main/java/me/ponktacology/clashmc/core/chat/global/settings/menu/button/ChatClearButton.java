package me.ponktacology.clashmc.core.chat.global.settings.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.chat.global.settings.updater.ChatClearUpdater;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class ChatClearButton extends Button {


  private final ChatClearUpdater chatClearUpdater;

  private final TaskDispatcher taskDispatcher;

  @Override
  public ItemStack getButtonItem(Player player) {
    return new ItemBuilder(Material.THIN_GLASS)
        .name("&eWyczyść czat")
        .lore( "&7Kliknij, aby wyczyścić czat.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      this.taskDispatcher.runAsync(this.chatClearUpdater::update);
      player.sendMessage(Text.colored("&aPomyślnie wyczyszczono czat."));
      return;
    }

    super.clicked(player, clickType);
  }
}
