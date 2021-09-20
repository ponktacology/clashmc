package me.ponktacology.clashmc.itemshop;

import lombok.Getter;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.util.PluginUtil;
import me.ponktacology.clashmc.crate.CratePlugin;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.itemshop.configuration.ItemShopConfiguration;
import me.ponktacology.clashmc.itemshop.menu.ItemShopMenuFactory;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.adapter.ItemShopPlayerParameterAdapter;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.ponktacology.clashmc.itemshop.player.command.ReClaimCommand;
import me.ponktacology.clashmc.itemshop.player.factory.ItemShopPlayerFactory;
import me.ponktacology.clashmc.itemshop.player.listener.PlayerDataListener;
import me.ponktacology.clashmc.itemshop.player.purchase.announcer.ItemShopPurchaseAnnouncer;
import me.ponktacology.clashmc.itemshop.player.purchase.announcer.packet.PacketItemShopPurchaseAnnounce;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.ItemShopPurchaseUpdater;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.packet.PacketPurchaseCrateUpdate;
import me.ponktacology.clashmc.itemshop.player.purchase.updater.packet.PacketPurchaseRankUpdate;
import me.ponktacology.clashmc.itemshop.task.ItemShopFetchTask;
import me.ponktacology.loader.LoaderPlugin;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.concurrent.TimeUnit;

@Getter
public enum ItemShopPlugin implements BukkitPlugin {
  INSTANCE;

  private final ItemShopPlayerFactory playerFactory = new ItemShopPlayerFactory();
  private final ItemShopPlayerCache playerCache = new ItemShopPlayerCache(this.playerFactory);
  private final ItemShopPurchaseUpdater purchaseUpdater =
      new ItemShopPurchaseUpdater(
          CorePlugin.INSTANCE.getNetworkService(),
          this.playerCache,
          CorePlugin.INSTANCE.getPlayerCache(),
          CratePlugin.INSTANCE.getCrateCache());
  private final ItemShopPurchaseAnnouncer purchaseAnnouncer =
      new ItemShopPurchaseAnnouncer(
          CorePlugin.INSTANCE.getNetworkService(),
          this.playerCache,
          GuildPlugin.INSTANCE.getPlayerCache());
  private final ItemShopMenuFactory menuFactory =
      new ItemShopMenuFactory(this.playerCache, CorePlugin.INSTANCE.getTaskDispatcher());
  private final ItemShopConfiguration configuration =
      new ItemShopConfiguration(LoaderPlugin.INSTANCE.getConfigurationSerializer());

  @Override
  public void enable(JavaPlugin plugin) {
    PluginUtil.registerListener(
        new PlayerDataListener(
            CorePlugin.INSTANCE.getTaskDispatcher(), this.playerCache, this.playerFactory));

    Blade corePluginBlade = CratePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("itemshop")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(ItemShopPlayer.class, new ItemShopPlayerParameterAdapter(this.playerCache))
        .build()
        .register(
            new ReClaimCommand(this.purchaseUpdater, this.purchaseAnnouncer, this.menuFactory));

    this.registerItemShop();
  }

  public void registerItemShop() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketItemShopPurchaseAnnounce.class);
    packetCache.add(PacketPurchaseCrateUpdate.class);
    packetCache.add(PacketPurchaseRankUpdate.class);

    NetworkService networkService = CorePlugin.INSTANCE.getNetworkService();
    networkService.subscribe(this.purchaseAnnouncer);
    networkService.subscribe(this.purchaseUpdater);

    if (this.configuration.isMain()) {
      CorePlugin.INSTANCE
          .getTaskDispatcher()
          .runTimerAsync(
              new ItemShopFetchTask(
                  this.configuration.getItemShopMySqlUrl(),
                  CorePlugin.INSTANCE.getTaskDispatcher()),
              1L,
              TimeUnit.SECONDS);
    }
  }
}
