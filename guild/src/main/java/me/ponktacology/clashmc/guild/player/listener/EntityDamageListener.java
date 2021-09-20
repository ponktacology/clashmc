package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.PlayerUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.DamageCache;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.combattag.global.cache.CombatTagSettingsCache;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;


@RequiredArgsConstructor
public class EntityDamageListener implements Listener {


  private final GuildPlayerCache playerCache;

  private final CombatTagSettingsCache combatTagSettingsCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityDamageEvent( EntityDamageEvent event) {
    Entity entity = event.getEntity();

    if (!(entity instanceof Player)) {
      return;
    }

    Player player = (Player) entity;

    this.playerCache
        .get(player)
        .ifPresent(
            it -> {
              if (it.hasSpawnProtection()) {
                event.setCancelled(true);
              }
            });
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEntityDamageByEntityEvent( EntityDamageByEntityEvent event) {
    Entity entity = event.getEntity();

    if (!(entity instanceof Player)) {
      return;
    }

    Player player = (Player) entity;

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (guildPlayer.hasSpawnProtection()) {
      event.setCancelled(true);
      return;
    }

    Player damagerPlayer = PlayerUtil.getDamageSource(event.getDamager());

    if (damagerPlayer == null) {
      return;
    }

    if (damagerPlayer.equals(player)) {
      return;
    }

    GuildPlayer damagerGuildPlayer = this.playerCache.getOrKick(damagerPlayer);

    if (damagerGuildPlayer.hasSpawnProtection()) {
      damagerPlayer.sendMessage(
          Text.colored(
              "&cNie możesz atakować podczas ochrony startowej, wyłącz ją komendą &f/ochrona wylacz&c."));
      event.setCancelled(true);
      return;
    }

    if (damagerGuildPlayer.hasSpawnProtection()) {
      event.setCancelled(true);
      return;
    }

    if (guildPlayer.hasGuild() && damagerGuildPlayer.hasGuild()) {
      Guild guild = guildPlayer.getGuild().get();
      if (guild.hasMember(damagerGuildPlayer)) {
        if (!guild.isFriendlyFire()) {
          event.setCancelled(true);
          return;
        }
      }
    }

    guildPlayer.increaseReceivedDamage(event.getFinalDamage());
    damagerGuildPlayer.increaseDealtDamage(event.getFinalDamage());

    DamageCache damageCache = guildPlayer.getDamageCache();
    damageCache.add(damagerGuildPlayer, event.getFinalDamage());

    DamageCache damageCache1 = damagerGuildPlayer.getDamageCache();
    damageCache1.setLastAttacker(guildPlayer);

    if (!this.combatTagSettingsCache.get().isEnabled()) {
      return;
    }

    guildPlayer.resetCombatTag();
    damagerGuildPlayer.resetCombatTag();
  }
}
