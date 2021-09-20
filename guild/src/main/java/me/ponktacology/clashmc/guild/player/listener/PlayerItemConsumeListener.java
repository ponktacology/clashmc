package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class PlayerItemConsumeListener implements Listener {

  private final TaskDispatcher taskDispatcher;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerItemConsumeEvent(PlayerItemConsumeEvent event) {
    Player player = event.getPlayer();
    ItemStack item = event.getItem();

    if (item != null && item.getType() == Material.GOLDEN_APPLE) {
      if (item.getDurability() == 0) {
        player.addPotionEffect(
            new PotionEffect(PotionEffectType.ABSORPTION, 2_400, 1, true, true), true);
      } else {
        this.taskDispatcher.runLater(
            () -> {
              player.addPotionEffect(
                  new PotionEffect(PotionEffectType.REGENERATION, 300, 4, true, true), true);
              player.addPotionEffect(
                  new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 3_000, 0, true, true), true);
              player.addPotionEffect(
                  new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 3_000, 0, true, true), true);
            },
            1L,
            TimeUnit.MILLISECONDS);

        this.taskDispatcher.runLater(
            () -> {
              player.removePotionEffect(PotionEffectType.REGENERATION);
            },
            15,
            TimeUnit.SECONDS);
      }
    }
  }
}
