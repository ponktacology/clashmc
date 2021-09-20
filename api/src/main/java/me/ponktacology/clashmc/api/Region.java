package me.ponktacology.clashmc.api;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public abstract class Region {

  private int minX, maxX, minY, maxY, minZ, maxZ;

  public void setX(int x) {
    if (x > this.maxX) {
      this.maxX = x;
    } else if (x < this.minX) {
      this.minX = x;
    }
  }

  public void setZ(int z) {
    if (z > this.maxZ) {
      this.maxZ = z;
    } else if (z < this.minZ) {
      this.minZ = z;
    }
  }

  public void setY(int y) {
    if (y > this.maxY) {
      this.maxY = y;
    } else if (y < this.minY) {
      this.minY = y;
    }
  }

  public int length() {
    return this.maxX - this.minX + 1;
  }

  public int width() {
    return this.maxZ - this.minZ + 1;
  }

  public int area() {
    return this.length() * this.width();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Region region = (Region) o;

    if (minX != region.minX) return false;
    if (maxX != region.maxX) return false;
    if (minY != region.minY) return false;
    if (maxY != region.maxY) return false;
    if (minZ != region.minZ) return false;
    return maxZ == region.maxZ;
  }

  @Override
  public int hashCode() {
    int result = minX;
    result = 31 * result + maxX;
    result = 31 * result + minY;
    result = 31 * result + maxY;
    result = 31 * result + minZ;
    result = 31 * result + maxZ;
    return result;
  }
}
