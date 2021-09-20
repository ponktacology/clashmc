package me.ponktacology.clashmc.sector.sector.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.SectorType;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.player.transfer.PlayerTransferUpdater;
import me.ponktacology.clashmc.sector.sector.menu.button.ChannelButton;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class ChannelMenu extends Menu {


  private final SectorCache sectorCache;

  private final CorePlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  private final PlayerTransferUpdater transferUpdater;

  private final Sector localSector;


  @Override
  public String getTitle(Player player) {
    return "&eWybierz sektor";
  }


  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    for (Sector sector : this.sectorCache.sortedValues(SectorType.SPAWN)) {
      buttons.put(
          buttons.size(),
          new ChannelButton(
              sector, localSector, this.taskDispatcher, this.transferUpdater, this.playerCache));
    }

    return buttons;
  }
}
