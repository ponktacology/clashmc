package me.ponktacology.clashmc.guild.recipe.menu;

import com.google.common.collect.Maps;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class RecipeMenu extends PaginatedMenu {

  private static final int[] SLOTS = {0, 1, 2, 9, 10, 11, 18, 19, 20};

  public RecipeMenu() {
    super(36);
    setPlaceholder(true);
  }

  @Override
  public int getSize() {
    return 45;
  }

  
  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eCraftingi";
  }

  
  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    int index = 1;
    for (CustomRecipe recipe : GuildPlugin.INSTANCE.getRecipeCache().valuesSorted()) {
      String[] rows = recipe.getShape();

      for (int x = 0; x < 3; x++) {
        char[] ingredients = rows[x].toCharArray();

        for (int y = 0; y < 3; y++) {
          char ingredient = ingredients[y];
          int slot = SLOTS[y + (x * 3)];

          ItemStack item =
              recipe
                  .getIngredientMap()
                  .getOrDefault(ingredient, new ItemBuilder(Material.AIR).build());

          buttons.put(
              slot + index,
              new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                  return item == null ? new ItemBuilder(Material.AIR).build() : item;
                }
              });
        }
      }

      buttons.put(
          13 + index,
          new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
              return new ItemBuilder(recipe.getResult()).name("&e" + recipe.getName()).build();
            }
          });

      buttons.put(
          15 + index,
          new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
              return new ItemBuilder(Material.WORKBENCH)
                  .name("&eAutocrafting")
                  .lore(
                      "&7Kliknij lewym, aby stworzyć przedmiot.",
                      "&7Kliknij shift + lewy, aby stworzyć",
                      "&7maksymalną możliwą ilość przedmiotów.")
                  .build();
            }

            @Override
            public void clicked( Player player,  ClickType clickType) {
              if (clickType.isLeftClick()) {

                Map<Material, Integer> neededItems = Maps.newHashMap();

                for (ItemStack itemStack : recipe.getItems()) {
                  int amount = neededItems.computeIfAbsent(itemStack.getType(), i -> 0);

                  neededItems.put(itemStack.getType(), amount + 1);
                }

                if (!hasItems(player, neededItems)) {
                  player.sendMessage(Text.colored("&cNie posiadasz wymaganych przedmiotów."));
                  return;
                }

                do {
                  InventoryUtil.removeItems(player, Arrays.asList(recipe.getItems()));
                  InventoryUtil.addItem(player, recipe.getResult());
                } while (hasItems(player, neededItems) && clickType.isShiftClick());
              }
            }
          });

      index += 36;
    }

    return buttons;
  }

  private boolean hasItems( Player player,  Map<Material, Integer> items) {
    for (Map.Entry<Material, Integer> entry : items.entrySet()) {
      if (!InventoryUtil.hasItem(
          player, new ItemBuilder(entry.getKey()).amount(entry.getValue()).build())) {
        return false;
      }
    }

    return true;
  }
}
