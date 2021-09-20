package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.util.SubClaimUtil;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.DoubleChestInventory;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

@RequiredArgsConstructor
public class SubClaimListener implements Listener {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEvent(SignChangeEvent event) {
    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) {
      return;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (!RegionUtil.isIn(event.getBlock().getLocation(), guild.getRegion())) {
      return;
    }

    if (event.getLines().length < 2) {
      return;
    }

    if (!event.getLine(0).equals("[Ochrona]")) {
      return;
    }

    if (!guild.hasPermission(guildPlayer, GuildPermission.Permissions.PRIVATE_CHEST_CREATION)) {
      player.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do tworzenia prywatnych skrzynek. Poproś lidera o zmianę ich w /g panel."));
      event.setCancelled(true);
      return;
    }

    event.setLine(0, Text.colored("&b&l[Ochrona]"));
  }

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onPlayerInteractEvent(PlayerInteractEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);
    if (!guildPlayer.hasGuild()) return;

    Block block = event.getClickedBlock();
    Guild guild = guildPlayer.getGuild().get();

    if (!RegionUtil.isIn(block.getLocation(), guild.getRegion())) {
      return;
    }

    if (block.getState() instanceof Chest) {
      Chest chest = (Chest) block.getState();
      Inventory inventory = chest.getInventory();

      if (inventory instanceof DoubleChestInventory) {
        DoubleChest doubleChest = (DoubleChest) inventory.getHolder();

        if (!SubClaimUtil.canAccess(
                (((Chest) doubleChest.getLeftSide()).getBlock()), player, guildPlayer, guild)
            || !SubClaimUtil.canAccess(
                (((Chest) doubleChest.getRightSide()).getBlock()), player, guildPlayer, guild)) {
          player.sendMessage(
              Text.colored(
                  "&cNie posiadasz permisji do otwierania wszystkich prywatnych skrzynek. Poproś lidera o zmianę ich w /g panel."));
          event.setCancelled(true);
        }
        return;
      }
    }

    if (!SubClaimUtil.canAccess(block, player, guildPlayer, guild)) {
      player.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do otwierania wszystkich prywatnych skrzynek. Poproś lidera o zmianę ich w /g panel."));
      event.setCancelled(true);
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockPlaceEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();

    if(block.getType() != Material.SIGN) return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) return;

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    Location location = block.getLocation();
    Optional<Guild> guildOptional = guildPlayer.getGuild(location);

    if (!guildOptional.isPresent()) return;

    Guild guild = guildOptional.get();

    if (!guild.hasMember(guildPlayer)) {
      return;
    }

    if (!SubClaimUtil.canAccess(block, player, guildPlayer, guild)) {
      player.sendMessage(
              Text.colored(
                      "&cNie posiadasz permisji do otwierania wszystkich prywatnych skrzynek. Poproś lidera o zmianę ich w /g panel."));
      event.setCancelled(true);
    }
  }

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();

    if (block.getType() != Material.HOPPER && !block.getType().toString().contains("PISTON"))
      return;

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);

    if (corePlayer.isStaff()) return;

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    Location location = block.getLocation();
    Optional<Guild> guildOptional = guildPlayer.getGuild(location);

    if (!guildOptional.isPresent()) return;

    Guild guild = guildOptional.get();

    if (!guild.hasMember(guildPlayer)) {
      return;
    }

    if (SubClaimUtil.checkForPrivate(block, player, guildPlayer, guild)) {
      event.setCancelled(true);
    }
  }
}
