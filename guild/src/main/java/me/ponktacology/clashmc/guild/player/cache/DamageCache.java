package me.ponktacology.clashmc.guild.player.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import me.ponktacology.clashmc.api.cache.Cache;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.util.Tuple;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class DamageCache implements Cache<Double> {

  private final com.github.benmanes.caffeine.cache.Cache<UUID, Double> damageReceived =
      Caffeine.newBuilder().expireAfterWrite(3L, TimeUnit.MINUTES).build();
  private UUID lastAttacker;
  private long lastAttacked;

  public void setLastAttacker( GuildPlayer guildPlayer) {
    this.lastAttacker = guildPlayer.getUuid();
  }

  public void add( GuildPlayer attacker,  Double damage) {
    this.setLastAttacker(attacker);
    this.lastAttacked = System.currentTimeMillis();

    Double currentDamage = this.damageReceived.getIfPresent(attacker);

    if (currentDamage == null) {
      this.damageReceived.put(attacker.getUuid(), damage);
    } else this.damageReceived.put(attacker.getUuid(), currentDamage + damage);
  }

  /*
     Returns damagers that meet required total damage percentage
     Returns Set of Tuple<Player UUID, Percentage of total damae>
  */
  
  public Set<Tuple<UUID, Double>> getHighestDamageAttackers(double minDamagePct) {
    double sum = this.values().stream().mapToDouble(it -> it).sum();

    if (sum == 0) return Collections.emptySet();

    Set<Tuple<UUID, Double>> highestDamagers = new HashSet<>();

    for (Map.Entry<UUID, Double> entry : this.damageReceived.asMap().entrySet()) {
      double pct = entry.getValue() / sum;
      if (pct > minDamagePct) {
        highestDamagers.add(new Tuple<>(entry.getKey(), pct));
      }
    }

    return highestDamagers;
  }

  public Optional<UUID> getLastAttacker() {
    if (System.currentTimeMillis() - this.lastAttacked > GuildConstants.LAST_ATTACK_EVICTION_TIME) {
      return Optional.empty();
    }

    return Optional.ofNullable(this.lastAttacker);
  }

  
  public Set<Double> values() {
    return new HashSet<>(damageReceived.asMap().values());
  }
}
