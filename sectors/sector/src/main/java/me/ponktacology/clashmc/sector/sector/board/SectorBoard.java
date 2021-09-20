package me.ponktacology.clashmc.sector.sector.board;

import com.google.common.collect.Lists;
import io.github.thatkawaiisam.assemble.AssembleAdapter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import me.ponktacology.clashmc.sector.api.sector.data.SectorData;
import org.bukkit.entity.Player;



import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class SectorBoard implements AssembleAdapter {

  
  private static final String BOARD_TITLE = Text.colored("&c&lClash&f&lMC.pl");


  private final Sector localSector;

  private final SectorCache sectorCache;

  private final CorePlayerCache playerCache;

  private int currentBoard = 0;
  private long lastBoardChange = System.currentTimeMillis();

  
  @Override
  public String getTitle(Player player) {
    return BOARD_TITLE;
  }


  @Override
  public List<String> getLines( Player player) {
    List<String> lines = Lists.newArrayList();

    SectorData data = this.localSector.getData();

    if (data == null) {
      return Collections.emptyList();
    }

    Optional<CorePlayer> corePlayerOptional = this.playerCache.get(player);

    if (!corePlayerOptional.isPresent()) {
      return Collections.emptyList();
    }

    CorePlayer corePlayer = corePlayerOptional.get();
    Rank rank = corePlayer.getMainRank();

    if (rank.getPower() >= 990) {
      for (Sector sector : this.sectorCache.sortedValues()) {
        SectorData sectorData = sector.getData();

        lines.add(
            Text.colored(
                (sector.equals(localSector) ? "&e" : "")
                    + sector.getName()
                    + "&f "
                    + StyleUtil.formatPercentage(sectorData.getCpuUsage())
                    + " "
                    + sectorData.getRamUsage()
                    + "/"
                    + sectorData.getMaxRam()));
      }
    } else if (this.localSector.isSpawn()) {
      lines.add("&7Spawn: &f" + this.localSector.getName());
      lines.add("&7Graczy: &f" + data.getPlayers());
      lines.add("");

      switch (currentBoard) {
        case 0:
          {
            lines.add("&7Sektor możesz zmienić");
            lines.add("&7pod &f/ch");
            break;
          }
        case 1:
          {
            lines.add("&7Bezpieczny handel");
            lines.add("&7pod &f/wymiana");
            break;
          }
        case 2:
          {
            lines.add("&7Otwórz swój sklep");
            lines.add("&7pod &f/bazar");
            break;
          }
      }

      if (System.currentTimeMillis() - lastBoardChange > 5_000L) {
        if (++currentBoard > 2) {
          currentBoard = 0;
        }
        lastBoardChange = System.currentTimeMillis();
      }
    } else {
      return Collections.emptyList();
    }

    lines.add(0, "");
    lines.add("");

    return lines;
  }
}
