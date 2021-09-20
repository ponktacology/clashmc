package me.ponktacology.clashmc.core.player.sync;

import lombok.Data;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.sync.flag.SyncFlag;
import org.bukkit.entity.Player;

import java.util.Set;

@Data
public abstract class PlayerSync {

  private final Set<SyncFlag> flags;

  public void apply(BukkitPlayerWrapper playerWrapper) {
    Player player = playerWrapper.getPlayer();

    if(player == null) return;

    this.apply(player);
  }

  public abstract void apply(Player player);

  public boolean hasFlag(SyncFlag flag) {
    return this.flags.contains(flag);
  }
}
