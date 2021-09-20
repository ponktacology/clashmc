package me.ponktacology.clashmc.core.util;

import lombok.experimental.UtilityClass;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

@UtilityClass
public class PlayerUtil {

  public static void resetPlayer0(Player player) {
    player.setHealth(20);
    player.setFoodLevel(20);
    player.setFireTicks(1);
    player.setFallDistance(0F);
    player.setExhaustion(0F);
    player.setSaturation(12F);
  }

  public static void resetPlayer(Player player) {
    resetPlayer(player, true);
  }

  public static void resetPlayer(Player player, boolean removeEffects) {
    player.getOpenInventory().close();
    resetPlayer0(player);
    if (removeEffects)
      player.getActivePotionEffects().forEach(it -> player.removePotionEffect(it.getType()));
  }

  public static Player getDamageSource(final Entity damager) {
    Player playerDamager = null;
    if (damager instanceof Player) {
      playerDamager = (Player) damager;
    } else if (damager instanceof Projectile) {
      final Projectile projectile = (Projectile) damager;
      if (projectile.getShooter() instanceof Player) {
        playerDamager = (Player) projectile.getShooter();
      }
    }
    return playerDamager;
  }

  public static String getAddress(Player player) {
    return player.getAddress().getAddress().toString().replace("/", "");
  }
}
