package me.ponktacology.clashmc.crate.crate.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.crate.CratePermissions;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.crate.crate.factory.CrateFactory;
import me.ponktacology.clashmc.crate.crate.item.CrateItem;
import me.ponktacology.clashmc.crate.crate.updater.CrateUpdater;
import me.ponktacology.clashmc.crate.crate.updater.PlayerCrateUpdater;
import me.ponktacology.clashmc.crate.player.CratePlayer;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class CrateCommand {

  private final CrateCache crateCache;
  private final CrateFactory crateFactory;
  private final CrateUpdater crateUpdater;
  private final PlayerCrateUpdater playerCrateUpdater;
  private final CorePlayerCache playerCache;

  @Command(
      value = "crate giveall",
      description = "Dodaje skrzynkę do ekwipunku wszystkim graczom",
      async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void giveAll(
      @Sender CommandSender sender, @Name("crate") Crate crate, @Name("amount") int amount) {
    this.playerCrateUpdater.update(crate, amount);
    sender.sendMessage(Text.colored("&aPomyślnie dodano skrzynkę wszystkim graczom do ekwipunku."));
  }

  @Command(value = "crate give", description = "Dodaje skrzynkę do ekwipunku", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void give(
      @Sender CommandSender sender,
      @Name("crate") Crate crate,
      @Name("amount") int amount,
      @Name("player") @Optional("self") CratePlayer player) {
    if (!this.playerCache.isOnlineNotInAuthOrLobby(player)) {
      sender.sendMessage(Text.colored("&cGracz musi być online."));
      return;
    }

    this.playerCrateUpdater.update(player, crate, amount);
    sender.sendMessage(Text.colored("&aPomyślnie dodano skrzynkę do ekwipunku."));
  }

  @Command(value = "crate create", description = "Tworzy nową skrzynkę", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void create(@Sender CommandSender sender, @Name("name") String name) {
    if (this.crateCache.get(name).isPresent()) {
      sender.sendMessage(Text.colored("&cTaka skrzynka już istnieje."));
      return;
    }

    Crate crate = this.crateFactory.create(name);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie dodano skrzynkę."));
  }

  @Command(value = "crate remove", description = "Usuwa skrzynkę", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void remove(@Sender CommandSender sender, @Name("crate") Crate crate) {
    this.crateUpdater.remove(crate);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto skrzynkę."));
  }

  @Command(value = "crate enable", description = "Włącza skrzynkę", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void enable(@Sender CommandSender sender, @Name("crate") Crate crate) {
    crate.setEnabled(true);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie włączono skrzynkę."));
  }

  @Command(value = "crate disable", description = "Wyłącza skrzynkę", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void disable(@Sender CommandSender sender, @Name("crate") Crate crate) {
    crate.setEnabled(false);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie wyłączono skrzynkę."));
  }

  @Command(value = "crate items add", description = "Dodaje przedmiot do skrzynki", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void itemsAdd(
      @Sender Player sender,
      @Name("crate") Crate crate,
      @Name("name") String name,
      @Name("chance") double chance) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand == null || itemInHand.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cNie trzymasz przedmiotu w ręce."));
      return;
    }

    CrateItem crateItem = new CrateItem(name, itemInHand, "", 0, chance);

    crate.addItem(crateItem);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie dodano przedmiot."));
  }

  @Command(value = "crate items displayName", description = "Ustawia wyświetlaną nazwę przedmiotu", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void displayName(
          @Sender CommandSender sender,
          @Name("crate") Crate crate,
          @Name("item") String item,
          @Combined @Name("name") String displayName) {
    CrateItem crateItem = crate.getItem(item);

    if (crateItem == null) {
      sender.sendMessage(Text.colored("&cNie znaleziono przedmiotu."));
      return;
    }

    crateItem.setDisplayName(displayName);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono wyświetlaną nazwę przedmiotu."));
  }

  @Command(value = "crate items index", description = "Ustawia index przedmiotu", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void itemsIndex(
      @Sender CommandSender sender,
      @Name("crate") Crate crate,
      @Name("item") String item,
      @Name("index") int index) {
    CrateItem crateItem = crate.getItem(item);

    if (crateItem == null) {
      sender.sendMessage(Text.colored("&cNie znaleziono przedmiotu."));
      return;
    }

    crateItem.setIndex(index);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono index przedmiotu."));
  }

  @Command(value = "crate items remove", description = "Usuwa przedmiot ze skrzynki", async = true)
  @Permission(CratePermissions.CRATE_MANAGE)
  public void itemsRemove(
      @Sender CommandSender sender, @Name("crate") Crate crate, @Name("item") String item) {
    CrateItem crateItem = crate.getItem(item);

    if (crateItem == null) {
      sender.sendMessage(Text.colored("&cNie znaleziono przedmiotu."));
      return;
    }

    crate.removeItem(crateItem);
    this.crateUpdater.update(crate);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto przedmiot."));
  }
}
