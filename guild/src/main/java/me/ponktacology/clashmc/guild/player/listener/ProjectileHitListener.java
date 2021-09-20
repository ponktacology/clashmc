package me.ponktacology.clashmc.guild.player.listener;

import me.ponktacology.clashmc.core.util.ActionBarUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.core.util.StyleUtil;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;


public class ProjectileHitListener implements Listener {

  @EventHandler(ignoreCancelled = true)
  public void onProjectileHitEvent( EntityDamageByEntityEvent event) {
    if (!(event.getDamager() instanceof Projectile)) {
      return;
    }

    Entity shooter = (Entity) ((Projectile) event.getDamager()).getShooter();

    if (!(shooter instanceof Player)) {
      return;
    }

    Entity entity = event.getEntity();

    if (!(entity instanceof Player)) {
      return;
    }

    double hp = StyleUtil.convertHealth(((Player) entity).getHealth());
    double absorptionHealth =
        StyleUtil.convertHealth(((CraftPlayer) entity).getHandle().getAbsorptionHearts());
    double damage = StyleUtil.convertHealth(event.getFinalDamage());

    if (damage >= absorptionHealth) {
      hp = Math.max(0, hp - (damage - absorptionHealth));
      absorptionHealth = 0;
    } else {
      absorptionHealth -= damage;
    }

    ActionBarUtil.sendActionBarMessage(
        (Player) shooter,
        Text.colored(
            "&4"
                + hp
                + " "
                + StyleUtil.getHeartIcon()
                + (absorptionHealth > 0
                    ? " &6" + absorptionHealth + " " + StyleUtil.getHeartIcon()
                    : "")));
  }
}
