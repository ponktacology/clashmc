package me.ponktacology.clashmc.guild.recipe.armor;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.recipe.armor.cache.ArmorSettingsCache;
import me.ponktacology.clashmc.guild.recipe.armor.updater.ArmorSettingsUpdater;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class ArmorButton extends Button {

  private final Material material;
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();
  private final ArmorSettingsCache armorSettingsCache =
      GuildPlugin.INSTANCE.getArmorSettingsCache();
  private final ArmorSettingsUpdater armorSettingsUpdater =
      GuildPlugin.INSTANCE.getArmorSettingsUpdater();

  @Override
  public ItemStack getButtonItem(Player player) {
    ArmorSettings armorSettings = this.armorSettingsCache.get();

    return new ItemBuilder(material)
        .lore(
            "&eStatus: " + StyleUtil.convertBooleanToText(armorSettings.enabled(material)),
            "&7Kliknij, aby zmienić status.")
        .build();
  }

  @Override
  public boolean shouldUpdate(Player player, ClickType clickType) {
    if (clickType.isLeftClick()) {
      armorSettingsUpdater.toggle(material);
      this.taskDispatcher.runAsync(() -> armorSettingsUpdater.update(material));
      player.sendMessage(Text.colored("&aPomyślnie ustawiono."));

      return true;
    }

    return super.shouldUpdate(player, clickType);
  }
}
