package me.ponktacology.clashmc.drop.generator.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.drop.generator.Generator;
import me.ponktacology.clashmc.drop.generator.cache.GeneratorCache;
import me.ponktacology.clashmc.drop.generator.factory.GeneratorFactory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Optional;

@RequiredArgsConstructor
public class GeneratorListener implements Listener {

  private final GeneratorCache generatorCache;

  private final GeneratorFactory generatorFactory;

  private final TaskDispatcher taskDispatcher;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();

    if (player == null) {
      return;
    }

    Block block = event.getBlock();

    if (block.getType() != Material.STONE) {
      return;
    }

    Optional<Generator> generatorOptional = this.generatorCache.get(block.getLocation());

    if (!generatorOptional.isPresent()) {
      return;
    }

    Generator generator = generatorOptional.get();
    ItemStack itemInHand = event.getPlayer().getItemInHand();

    if (itemInHand != null && itemInHand.getType().equals(Material.GOLD_PICKAXE)) {
      ItemStack itemStack = generator.getItem();
      this.generatorCache.remove(generator.getLocation());
      this.taskDispatcher.runAsync(() -> this.generatorFactory.delete(generator));
      InventoryUtil.addItem(player, itemStack);
      block.setType(Material.AIR);
      event.setCancelled(true);
      return;
    }

    generator.regen(block);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();

    if (player == null) {
      return;
    }

    Block block = event.getBlock();

    if (block.getType() != Material.STONE) {
      return;
    }

    ItemStack item = event.getItemInHand();
    if (item == null || item.getEnchantmentLevel(Enchantment.DURABILITY) != 10) {
      return;
    }

    ItemMeta meta = item.getItemMeta();

    if (meta == null) {
      return;
    }

    int level =
        Integer.parseInt(ChatColor.stripColor(meta.getLore().get(1).replace("Poziom: ", "")));

    Generator generator = new Generator(block.getLocation());
    generator.setLevel(level);

    this.generatorCache.add(generator);
    this.taskDispatcher.runAsync(generator::save);
  }
}
