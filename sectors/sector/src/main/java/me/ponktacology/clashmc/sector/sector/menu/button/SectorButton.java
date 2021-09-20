package me.ponktacology.clashmc.sector.sector.menu.button;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.MathUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import me.ponktacology.clashmc.sector.api.sector.data.SectorData;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class SectorButton extends Button {


  private final Sector sector;

  @Override
  public ItemStack getButtonItem(Player player) {
    SectorData data = sector.getData();

    if (data == null) return new ItemStack(Material.AIR);

    double cpuUsage = sector.getData().getCpuUsage();
    return new ItemBuilder(Material.STAINED_CLAY)
        .durability(getDurability(cpuUsage))
        .name(sector.getName())
        .lore(
            "",
            "&eTyp: &f" + sector.getType().toString(),
            "&eTPS: &f"
                + Arrays.stream(data.getTps())
                    .map(it -> MathUtil.roundOff(it, 1))
                    .mapToObj(String::valueOf)
                    .collect(Collectors.joining(", ")),
            "&eCPU: &f" + StyleUtil.formatPercentage(data.getCpuUsage()),
            "&eOnline: &f" + data.getPlayers(),
            "&eMax Online: &f" + data.getMaxPlayers(),
            "&eRam: &f" + data.getRamUsage() + "/" + data.getMaxRam(),
            "",
            "&7Kliknij lewym, aby dołączyć.",
            "&7Kliknij prawym, aby odświeżyć dane.")
        .build();
  }

  @Override
  public boolean shouldUpdate( Player player,  ClickType clickType) {
    if (clickType.isLeftClick()) {
      Location location = RegionUtil.getRandomLocationInSector(sector);

      if (SectorPlugin.INSTANCE.getLocalSector().equals(sector)) {
        player.sendMessage(Text.colored("&cJesteś już na tym sektorze."));
        return true;
      }

      CorePlugin.INSTANCE
          .getTaskDispatcher()
          .runAsync(
              () -> SectorPlugin.INSTANCE.getTransferUpdater().update(player, sector, location));
    }

    return true;
  }

  private int getDurability(double cpuUsage) {
    if (cpuUsage >= 0.8) {
      return 14;
    } else if (cpuUsage >= 0.4) {
      return 4;
    }

    return 5;
  }
}
