package me.ponktacology.clashmc.guild.guild.region;

import me.ponktacology.clashmc.api.Region;
import me.ponktacology.clashmc.core.blazingpack.cuboid.MessagedRectangle;
import me.ponktacology.clashmc.core.util.Text;


public class GuildRegion extends Region {

  public GuildRegion(int minX, int maxX, int minZ, int maxZ) {
    super(minX, maxX, -1, 999, minZ, maxZ);
  }

  public MessagedRectangle toRectangle(String message, boolean allowDigging, boolean allowPlacing) {
    return new MessagedRectangle(
        message,
        this.getMinX(),
        this.getMinZ(),
        this.height(),
        this.width(),
        allowDigging,
        allowPlacing);
  }

  public MessagedRectangle toEnemyRectangle() {
    return this.toRectangle(
        Text.colored("&cNie możesz budować na terenie wrogiej gildii."), false, false);
  }

  public int height() {
    return this.getMaxX() - this.getMinX();
  }

  public int width() {
    return this.getMaxZ() - this.getMinZ();
  }
}
