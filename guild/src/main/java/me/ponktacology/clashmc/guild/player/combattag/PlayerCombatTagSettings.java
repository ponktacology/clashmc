package me.ponktacology.clashmc.guild.player.combattag;

import lombok.Getter;
import lombok.Setter;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.api.settings.PlayerSettings;
import me.ponktacology.clashmc.guild.GuildConstants;

@Setter
@Getter
public class PlayerCombatTagSettings implements PlayerSettings, Expiring {

  private long lastCombatTagReset;

  public void disable() {
    this.lastCombatTagReset = 0L;
  }

  public void reset() {
    this.lastCombatTagReset = System.currentTimeMillis();
  }

  @Override
  public boolean hasExpired() {
    return this.lastCombatTagReset + GuildConstants.COMBAT_TAG_TIME <= System.currentTimeMillis();
  }
}
