package me.ponktacology.clashmc.itemshop.player.purchase.announcer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.ponktacology.clashmc.itemshop.player.purchase.ItemShopPurchase;
import me.ponktacology.clashmc.itemshop.player.purchase.announcer.packet.PacketItemShopPurchaseAnnounce;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class ItemShopPurchaseAnnouncer
    implements Announcer<ItemShopPurchase, PacketItemShopPurchaseAnnounce> {

  private final NetworkService networkService;
  private final ItemShopPlayerCache playerCache;
  private final GuildPlayerCache guildPlayerCache;

  @Override
  public void announce( ItemShopPurchase announce) {
    this.networkService.publish(
        new PacketItemShopPurchaseAnnounce(
            announce.getPlayer().getUuid(), announce.getFormattedName()));
  }

  @Override
  @PacketHandler
  public void handle( PacketItemShopPurchaseAnnounce packet) {
    Optional<ItemShopPlayer> itemShopPlayerOptional = this.playerCache.get(packet.getPlayer());

    if (!itemShopPlayerOptional.isPresent()) {
      log.info(
          "Received item shop purchase announce but player not found in database player= "
              + packet.getPlayer());
      return;
    }

    ItemShopPlayer itemShopPlayer = itemShopPlayerOptional.get();

    String message =
        Text.colored(
            "&eGracz &f" + itemShopPlayer.getName() + "&e zakupił " + packet.getProduct() + "&e!");

    for (GuildPlayer guildPlayer : this.guildPlayerCache.values()) {
      if (!guildPlayer.isChatSettingEnabled(ChatSettings.Settings.ITEM_SHOP_MESSAGES)) continue;

      guildPlayer.sendMessage("");
      guildPlayer.sendMessage(message);
      guildPlayer.sendMessage("");
    }
  }
}
