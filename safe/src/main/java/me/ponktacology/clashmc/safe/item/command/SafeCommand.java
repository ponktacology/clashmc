package me.ponktacology.clashmc.safe.item.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.safe.SafePermissions;
import me.ponktacology.clashmc.safe.item.SafeItem;
import me.ponktacology.clashmc.safe.item.cache.SafeItemCache;
import me.ponktacology.clashmc.safe.item.factory.SafeItemFactory;
import me.ponktacology.clashmc.safe.item.menu.factory.SafeMenuFactory;
import me.ponktacology.clashmc.safe.item.updater.SafeItemUpdater;
import me.ponktacology.clashmc.safe.player.SafePlayer;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
public class SafeCommand {


  private final SafeItemUpdater safeItemUpdater;

  private final SafeItemFactory safeItemFactory;

  private final SafeItemCache safeItemCache;

  private final SafeMenuFactory menuFactory;
  private final ItemStack price = new ItemBuilder(Material.GOLD_INGOT).amount(3).build();

  @Command(
      value = {"schowek", "depozyt"},
      description = "Otwiera schowek (koszt: 3 sztabki złota)")
  public void safe( @Sender Player sender) {
    if (!sender.hasPermission(SafePermissions.SAFE_PRICE_BYPASS)) {
      if (!InventoryUtil.hasItem(sender, this.price)) {
        sender.sendMessage(Text.colored("&cOtworzenie schowka kosztuje 3 sztabki złota."));
        return;
      }

      InventoryUtil.removeItem(sender, this.price);
    }

    this.menuFactory.getSafeMenu().openMenu(sender);
  }

  @Command(value = "safe add", description = "Dodaje przedmiot do schowka", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void add( @Sender Player sender, @Name("name") String name) {
    ItemStack item = sender.getItemInHand();

    if (item == null || item.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz mieć jakiś przedmiot w ręce."));
      return;
    }

    SafeItem safeItem = this.safeItemFactory.create(name, item);
    this.safeItemUpdater.update(safeItem);

    sender.sendMessage(Text.colored("&aPomyślnie dodano przedmiot do schowka."));
  }

  @Command(value = "safe remove", description = "Usuwa przedmiot ze schowka", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void remove( @Sender Player sender, @Name("item") SafeItem safeItem) {
    this.safeItemUpdater.remove(safeItem);
    sender.sendMessage(Text.colored("&aPomyślnie usunięto przedmiot ze schowka."));
  }

  @Command(value = "safe name", description = "Dodaje przedmiot do dropu", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void name(
           @Sender Player sender,  @Name("item") SafeItem safeItem, @Name("name") String name) {
    safeItem.setName(name);
    this.safeItemUpdater.update(safeItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono nazwę przedmiotu."));
  }

  @Command(value = "safe limit", description = "Ustawia limit przedmiotu", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void limit(
           @Sender Player sender,  @Name("item") SafeItem safeItem, @Name("limit") int limit) {
    safeItem.setLimit(limit);
    this.safeItemUpdater.update(safeItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono limit przedmiotu."));
  }

  @Command(value = "safe index", description = "Ustawia index przedmiotu", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void index(
           @Sender Player sender,  @Name("item") SafeItem safeItem, @Name("index") int index) {
    safeItem.setIndex(index);
    this.safeItemUpdater.update(safeItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono index przedmiotu."));
  }

  @Command(value = "safe reset", description = "Czysci schowek gracza", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void clearSafe( CommandSender sender, @Name("player") SafePlayer safePlayer) {

    sender.sendMessage(Text.colored("&aPomyślnie wyczyszczono schowek gracza."));
  }

  @Command(value = "safe check", description = "Sprawdza zawartość schowka gracza", async = true)
  @Permission(SafePermissions.SAFE_MANAGE)
  public void checkSafe( @Sender Player sender,  @Name("player") SafePlayer safePlayer) {
    new Menu() {

      @Override
      public String getTitle( Player player) {
        return "&eSchowek gracza &f" + player.getName();
      }


      @Override
      public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Map<UUID, Integer> safeItems = safePlayer.getItems();

        for (SafeItem safeItem : safeItemCache.values()) {
          if (!safeItems.containsKey(safeItem.getUuid())) continue;

          buttons.put(
              buttons.size() - 9,
              new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                  return new ItemBuilder(safeItem.getItem().getType())
                      .durability(safeItem.getItem().getDurability())
                      .lore("&eIlość: &f" + safeItems.get(safeItem.getUuid()))
                      .build();
                }
              });
        }

        return buttons;
      }
    }.openMenu(sender);
  }
}
