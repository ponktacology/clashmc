package me.ponktacology.clashmc.sector.border;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldBorder.EnumWorldBorderAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

@RequiredArgsConstructor
public class Border {

  @Getter private final UUID targetPlayer;
  private final boolean isZOrientation = SectorPlugin.INSTANCE.getLocalSector().getName().toUpperCase().endsWith("E") || SectorPlugin.INSTANCE.getLocalSector().getName().toUpperCase().endsWith("W");

  private boolean isZOrientation() {
    return this.isZOrientation;
  }

  private double getRadius() {
    return (RegionUtil.distanceToBorder(SectorPlugin.INSTANCE.getLocalSector(), getCenter()) * 2)
        - 1;
  }

  private Location getCenter() {
    return RegionUtil.getCenter(0, SectorPlugin.INSTANCE.getLocalSector());
  }

  private int getCenterX() {
    return getCenter().getBlockX();
  }

  private int getCenterZ() {
    return getCenter().getBlockZ();
  }

  public void update(Player player) {
    Location location = player.getLocation();
    if (isZOrientation()) {
      int axis = calculateNewAxis(getCenterZ(), location.getBlockZ());
      updateBorder(player, getCenterX(), axis);
    } else {
      int axis = calculateNewAxis(getCenterX(), location.getBlockX());
      updateBorder(player, axis, getCenterZ());
    }
  }

  private int getBorderOffsetComponent() {
    return isZOrientation()
        ? SectorPlugin.INSTANCE.getLocalSector().getRegion().getMinZ()
        : SectorPlugin.INSTANCE.getLocalSector().getRegion().getMinX();
  }

  private final int RADIUS_OFFSET = (int) (getRadius() / 2);

  private int calculateNewAxis(int cAxis, int pAxis) {
    int maxOffset = Math.abs(cAxis - (getBorderOffsetComponent() + RADIUS_OFFSET));
    int normalizedOffsetA = cAxis + maxOffset;
    int normalizedOffsetB = cAxis - maxOffset;

    if (pAxis > normalizedOffsetA) {
      return normalizedOffsetA;
    } else if (pAxis < normalizedOffsetB) {
      return normalizedOffsetB;
    }

    return pAxis;
  }

  private void updateBorder(Player player, int centerX, int centerZ) {
    net.minecraft.server.v1_8_R3.WorldBorder worldBorder =
        new net.minecraft.server.v1_8_R3.WorldBorder();
    worldBorder.setCenter(centerX, centerZ);
    worldBorder.setSize(getRadius());
    ((CraftPlayer) player)
        .getHandle()
        .playerConnection
        .sendPacket(new PacketPlayOutWorldBorder(worldBorder, EnumWorldBorderAction.SET_SIZE));
    ((CraftPlayer) player)
        .getHandle()
        .playerConnection
        .sendPacket(new PacketPlayOutWorldBorder(worldBorder, EnumWorldBorderAction.SET_CENTER));
  }

  public Player getPlayer() {
    return Bukkit.getPlayer(targetPlayer);
  }
}
