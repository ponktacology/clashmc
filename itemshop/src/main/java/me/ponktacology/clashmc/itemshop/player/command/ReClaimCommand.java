package me.ponktacology.clashmc.itemshop.player.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.itemshop.ItemShopPermissions;
import me.ponktacology.clashmc.itemshop.menu.ItemShopMenuFactory;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.purchase.announcer.ItemShopPurchaseAnnouncer;
import me.ponktacology.clashmc.itemshop.player.purchase.type.ItemShopPurchaseCrate;
import me.ponktacology.clashmc.itemshop.player.purchase.type.ItemShopPurchaseRank;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.ItemShopPurchaseUpdater;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


@RequiredArgsConstructor
public class ReClaimCommand {

  
  private final ItemShopPurchaseUpdater purchaseUpdater;
  
  private final ItemShopPurchaseAnnouncer purchaseAnnouncer;
  
  private final ItemShopMenuFactory menuFactory;

  @Command(value = {"odbierz", "is", "itemshop"})
  public void reclaim( @Sender Player sender) {
    this.menuFactory.getItemShopMenu().openMenu(sender);
  }

  @Command(value = "is add crate", description = "Dodaje skrzynkę do odbioru", async = true)
  @Permission(ItemShopPermissions.ITEMSHOP_MANAGE)
  public void isAddCrate(
           @Sender CommandSender sender,
           @Name("player") ItemShopPlayer itemShopPlayer,
          @Name("crate") Crate crate,
          @Name("amount") int amount) {
    ItemShopPurchaseCrate purchase = new ItemShopPurchaseCrate(itemShopPlayer, crate, amount);

    this.purchaseAnnouncer.announce(purchase);
    this.purchaseUpdater.update(purchase);

    sender.sendMessage(
        Text.colored("&aPomyślnie dodano skrzynkę graczowi " + itemShopPlayer.getName() + "."));
  }

  @Command(value = "is add rank", description = "Dodaje rangę do odbioru", async = true)
  @Permission(ItemShopPermissions.ITEMSHOP_MANAGE)
  public void isAddCrate(
           @Sender CommandSender sender,
           @Name("player") ItemShopPlayer itemShopPlayer,
           @Name("rank") Rank rank,
           @Name("duration") @Optional("perm") Time time) {
    ItemShopPurchaseRank purchase =
        new ItemShopPurchaseRank(
            itemShopPlayer,
            new Grant(
                rank.getName(),
                Console.UUID,
                "Zakup w itemshopie",
                System.currentTimeMillis(),
                time.getTimeStamp()));

    this.purchaseAnnouncer.announce(purchase);
    this.purchaseUpdater.update(purchase);

    sender.sendMessage(
        Text.colored("&aPomyślnie dodano rangę graczowi " + itemShopPlayer.getName() + "."));
  }
}
