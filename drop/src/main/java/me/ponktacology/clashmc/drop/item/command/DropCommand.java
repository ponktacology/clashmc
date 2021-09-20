package me.ponktacology.clashmc.drop.item.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.drop.DropConstants;
import me.ponktacology.clashmc.drop.DropPermissions;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.factory.DropItemFactory;
import me.ponktacology.clashmc.drop.item.updater.DropItemUpdater;
import me.ponktacology.clashmc.drop.menu.DropMenuFactory;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


@RequiredArgsConstructor
public class DropCommand {


  private final DropItemFactory dropItemFactory;

  private final DropItemUpdater dropItemUpdater;

  private final DropItemCache dropItemCache;

  private final DropMenuFactory menuFactory;

  @Command(
      value = {"cobblex", "cx"},
      description = "Tworzy CobbleX")
  public void cobblex( @Sender Player sender) {
    if (!InventoryUtil.hasItem(sender, DropConstants.COBBLEX_PRICE)) {
      sender.sendMessage(Text.colored("&cPotrzebujesz 9x64 cobblestone do stworzenia CobbleX."));
      return;
    }

    InventoryUtil.removeItem(sender, DropConstants.COBBLEX_PRICE);
    InventoryUtil.addItem(sender, DropConstants.COBBLEX_ITEM.clone());
    sender.sendMessage(Text.colored("&aPomyślnie stworzono CobbleX."));
  }

  @Command(
      value = {"drop", "stone", "kamien"},
      description = "Otwiera menu dropów")
  public void drop( @Sender Player sender) {
    this.menuFactory.getDropMenu().openMenu(sender);
  }

  @Command(value = "drop add", description = "Dodaje przedmiot do dropu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void add( @Sender Player sender,  @Name("name") String name) {
    ItemStack item = sender.getItemInHand();

    if (item == null || item.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz mieć jakiś przedmiot w ręce."));
      return;
    }

    if (this.dropItemCache.get(name).isPresent()) {
      sender.sendMessage(Text.colored("&cIstnieje już przedmiot o takiej nazwie."));
      return;
    }

    DropItem dropItem = this.dropItemFactory.create(name);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie dodano przedmiot do dropu."));
  }

  @Command(value = "drop item", description = "Zmienia przedmiot dropu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void item( @Sender Player sender,  @Name("item") DropItem dropItem) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand == null) {
      sender.sendMessage(Text.colored("&cMusisz trzymać jakiś przedmiot w ręce."));
      return;
    }

    dropItem.setItem(itemInHand);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono nazwę przedmiotu."));
  }

  @Command(value = "drop remove", description = "Usuwa przedmiot z dropu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void remove( @Sender Player sender, @Name("item") DropItem dropItem) {
    this.dropItemUpdater.remove(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto przedmiot z dropu."));
  }

  @Command(value = "drop displayName", description = "Dodaje przedmiot do dropu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void name(
           @Sender Player sender,
           @Name("item") DropItem dropItem,
          @Name("displayName") @Combined String displayName) {
    dropItem.setDisplayName(displayName);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono wygląd nazwy przedmiotu."));
  }

  @Command(value = "drop type", description = "Ustawia typ dropu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void name(
           @Sender Player sender,  @Name("item") DropItem dropItem, @Name("type") DropType type) {
    dropItem.setType(type);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono typ dropu przedmiotu."));
  }

  @Command(value = "drop fortune", description = "Ustawia fortune przedmiotu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void fortune(
           @Sender Player sender,  @Name("item") DropItem dropItem, @Name("fortune") boolean fortune) {
    dropItem.setFortune(fortune);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono fortune przedmiotu."));
  }

  @Command(
      value = "drop chance",
      description = "Ustawia szanse na wykopanie przedmiotu",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void chance(
           @Sender Player sender,  @Name("item") DropItem dropItem, @Name("chance") double chance) {
    dropItem.setChance(chance);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono szanse wykopania przedmiotu."));
  }

  @Command(
      value = "drop belowY",
      description = "Ustawia poniżej, którego poziomu przedmiot może zostać wykopany",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void belowY(
           @Sender Player sender,  @Name("item") DropItem dropItem, @Name("y") int chance) {
    dropItem.setDropBelowY(chance);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono chance przedmiotu."));
  }

  @Command(value = "drop index", description = "Ustawia index przedmiotu", async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void index(
           @Sender Player sender,  @Name("item") DropItem dropItem, @Name("index") int index) {
    dropItem.setIndex(index);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono index przedmiotu."));
  }

  @Command(
      value = "drop pickaxe add",
      description = "Dodaje kilof do mozliwych uzycia",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void pickaxeAdd( @Sender Player sender,  @Name("item") DropItem dropItem) {
    ItemStack item = sender.getItemInHand();

    if (item == null || item.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz mieć jakiś przedmiot w ręce."));
      return;
    }

    Material material = item.getType();

    if (!material.toString().contains("PICKAXE")) {
      sender.sendMessage(Text.colored("&cPrzedmiot musi być kilofem."));
      return;
    }

    if (dropItem.canMine(material)) {
      sender.sendMessage(Text.colored("&cTen kilof już należy do możliwych użyć."));
      return;
    }

    dropItem.addPickaxe(material);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie dodano przedmiot."));
  }

  @Command(
      value = "drop pickaxe remove",
      description = "Usuwa kilof do mozliwych uzycia",
      async = true)
  @Permission(DropPermissions.DROP_MANAGE)
  public void pickaxeRemove( @Sender Player sender,  @Name("item") DropItem dropItem) {
    ItemStack item = sender.getItemInHand();

    if (item == null || item.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz mieć jakiś przedmiot w ręce."));
      return;
    }

    Material material = item.getType();

    if (!material.toString().contains("PICKAXE")) {
      sender.sendMessage(Text.colored("&cPrzedmiot musi być kilofem."));
      return;
    }

    if (!dropItem.canMine(material)) {
      sender.sendMessage(Text.colored("&cTen kilof nie należy do możliwych użyć."));
      return;
    }

    dropItem.removePickaxe(material);
    this.dropItemUpdater.update(dropItem);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto przedmiot."));
  }
}
