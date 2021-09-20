package me.ponktacology.clashmc.guild.player.spawnprotection;

import lombok.Getter;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.guild.GuildConstants;

@Getter
public class SpawnProtectionSettings implements Expiring {

  private int time = GuildConstants.SPAWN_PROTECTION_SECONDS;

  public boolean tick() {
    --this.time;

    return !hasExpired();
  }

  @Override
  public boolean hasExpired() {
    return this.time == 0;
  }

  public void disable() {
    this.time = 0;
  }
}
