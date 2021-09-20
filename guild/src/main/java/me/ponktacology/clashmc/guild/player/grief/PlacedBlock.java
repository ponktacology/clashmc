package me.ponktacology.clashmc.guild.player.grief;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.guild.GuildConstants;
import org.bukkit.Material;
import org.bukkit.block.Block;


@RequiredArgsConstructor
public class PlacedBlock implements Expiring {


  private final Block block;
  private final long timeStamp = System.currentTimeMillis();

  public void clear() {
    this.block.setType(Material.AIR);
  }

  @Override
  public boolean hasExpired() {
    return System.currentTimeMillis() - this.timeStamp > GuildConstants.PLACED_BLOCK_CLEAR_TIME;
  }
}
