package me.ponktacology.clashmc.drop.generator.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.drop.generator.Generator;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GeneratorMenu extends Menu {


  private final Generator generator;

  private final DropPlayer dropPlayer;

  private final TaskDispatcher taskDispatcher;


  @Override
  public String getTitle(Player player) {
    return "&eGenerator";
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    buttons.put(
        0,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.PAPER)
                .name("&eStatystyki")
                .lore(
                    "",
                    "&7Wygenerowane bloki: &f" + generator.getUsage(),
                    "&7Poziom: &f" + generator.getLevel())
                .build();
          }
        });

    buttons.put(
        1,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            ItemBuilder builder =
                new ItemBuilder(Material.STAINED_CLAY)
                    .durability(4)
                    .name("&aUlepszenie")
                    .lore(
                        "",
                        generator.isMaxLevel()
                            ? "&aOsiągnięto maksymalny poziom ulepszenia."
                            : "&7Poziom ulepszenia: &f" + generator.getLevel());

            if (!generator.isMaxLevel()) {
              builder.lore(
                  "&7Koszt ulepszenia: &f" + generator.getUpgradePrice() + " &7punktów kopania",
                  "&7Kliknij, aby ulepszyć.");
            }

            return builder.build();
          }

          @Override
          public boolean shouldUpdate( Player player,  ClickType clickType) {
            if (clickType.isLeftClick()) {
              if (generator.isMaxLevel()) {
                player.sendMessage(Text.colored("&cOsiągnięto maksymalny poziom ulepszenia."));
                return super.shouldUpdate(player, clickType);
              }

              if (dropPlayer.getPoints() < generator.getUpgradePrice()) {
                player.sendMessage(Text.colored("&cNie masz wystarczającej ilości punktów."));
                return super.shouldUpdate(player, clickType);
              }

              dropPlayer.decreasePoints(generator.getUpgradePrice());
              generator.increaseLevel();

              taskDispatcher.runAsync(
                  () -> {
                    generator.save();
                    dropPlayer.save();
                  });
              return true;
            }

            return super.shouldUpdate(player, clickType);
          }
        });

    buttons.put(
        8,
        new Button() {
          @Override
          public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(Material.GOLD_NUGGET)
                .name("&6Punkty")
                .lore("&7Punkty kopania: &f" + dropPlayer.getPoints())
                .build();
          }
        });

    return buttons;
  }
}
