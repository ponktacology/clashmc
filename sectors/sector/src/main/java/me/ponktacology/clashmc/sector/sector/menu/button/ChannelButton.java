package me.ponktacology.clashmc.sector.sector.menu.button;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.data.SectorData;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


public class ChannelButton extends Button {

  private final Sector sector;
  private final TaskDispatcher taskDispatcher;
  private final PlayerTransferUpdater transferUpdater;
  private final boolean isLocal;
  private final CorePlayerCache playerCache;

  public ChannelButton(
      Sector sector,
      Sector localSector,
      TaskDispatcher taskDispatcher,
      PlayerTransferUpdater transferUpdater,
      CorePlayerCache playerCache) {
    this.sector = sector;
    this.taskDispatcher = taskDispatcher;
    this.transferUpdater = transferUpdater;
    this.playerCache = playerCache;

    this.isLocal = this.sector.equals(localSector);
  }

  @Override
  public ItemStack getButtonItem(Player player) {
    SectorData data = this.sector.getData();

    return new ItemBuilder(Material.STAINED_CLAY)
        .name("&e" + this.sector.getName())
        .durability(getDurability(sector))
        .lore(
            "",
            "&eOnline: &f" + data.getPlayers() + "&7/&f" + data.getMaxPlayers(),
            "",
            this.isLocal ? "&aTutaj się aktualnie znajdujesz." : "&7Kliknij lewym, aby dołączyć.")
        .build();
  }

  @Override
  public void clicked( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      if (this.isLocal) {
        player.sendMessage(Text.colored("&cJuż znajdujesz się na tym sektorze."));
        return;
      }

      if (this.sector.getOnlinePlayers() >= this.sector.getMaxPlayers()) {
        CorePlayer corePlayer = this.playerCache.getOrKick(player);

        if (!corePlayer.isStaff()) {
          player.sendMessage(Text.colored("&cTen sektor jest pełen."));
          return;
        }
      }

      this.taskDispatcher.runAsync(
          () ->
              this.transferUpdater.update(
                  player, this.sector, player.getLocation().getBlock().getLocation()));
    }
  }

  public int getDurability( Sector sector) {
    if (this.isLocal) {
      return 3;
    }

    double freeSlots =
        1 - ((double) sector.getData().getPlayers() / sector.getData().getMaxPlayers());

    if (freeSlots >= .75) {
      return 5;
    } else if (freeSlots < .75 && freeSlots >= .25) {
      return 4;
    } else {
      return 14;
    }
  }
}
