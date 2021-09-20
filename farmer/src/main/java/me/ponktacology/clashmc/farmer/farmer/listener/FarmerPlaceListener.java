package me.ponktacology.clashmc.farmer.farmer.listener;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.farmer.farmer.type.PillarFarmer;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.recipe.CustomRecipe;
import me.ponktacology.clashmc.guild.recipe.cache.RecipeCache;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class FarmerPlaceListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final RecipeCache recipeCache;
  private final TaskDispatcher taskDispatcher;

  private final ItemStack obsidianFarmerItem;
  private final ItemStack sandFarmerItem;

  public FarmerPlaceListener(
      GuildPlayerCache playerCache, RecipeCache recipeCache, TaskDispatcher taskDispatcher) {
    this.playerCache = playerCache;
    this.recipeCache = recipeCache;
    this.taskDispatcher = taskDispatcher;

    Optional<CustomRecipe> obsidianFarmerRecipe = this.recipeCache.get("BoyFarmer");
    this.obsidianFarmerItem = obsidianFarmerRecipe.map(ShapedRecipe::getResult).orElse(null);

    Optional<CustomRecipe> sandFarmerRecipe = this.recipeCache.get("SandFarmer");
    this.sandFarmerItem = sandFarmerRecipe.map(ShapedRecipe::getResult).orElse(null);
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(BlockPlaceEvent event) {
    ItemStack itemInHand = event.getItemInHand();
    if (itemInHand == null) return;

    Block block = event.getBlockPlaced();
    Location location = block.getLocation();
    Player player = event.getPlayer();

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    Material material;
    ItemStack itemStack;

    if (itemInHand.isSimilar(this.obsidianFarmerItem)) {
      material = Material.OBSIDIAN;
      itemStack = this.obsidianFarmerItem;
    } else if (itemInHand.isSimilar(this.sandFarmerItem)) {
      material = Material.SAND;
      itemStack = this.sandFarmerItem;
    } else return;

    event.setCancelled(true);

    if (!canPlaceFarmer(guildPlayer, location)) {
      return;
    }

    InventoryUtil.removeItem(player, itemStack);

    this.taskDispatcher.runLater(
        () -> new PillarFarmer(block.getLocation(), material).start(), 100L, TimeUnit.MILLISECONDS);
  }

  private boolean canPlaceFarmer(GuildPlayer guildPlayer, Location location) {
    if (!guildPlayer.hasGuild()) {
      guildPlayer.sendMessage(Text.colored("&cMusisz być w gildii, aby użyć farmera."));
      return false;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (!guild.isIn(location)) {
      guildPlayer.sendMessage(
          Text.colored("&cMusisz być na terenie swojej gildii, aby użyć farmera."));
      return false;
    }

    if (guild.isInHeartRoomIgnoreY(location)) {
      guildPlayer.sendMessage(Text.colored("&cNie możesz użyć farmera w okolicy serca gildii."));
      return false;
    }

    return true;
  }
}
