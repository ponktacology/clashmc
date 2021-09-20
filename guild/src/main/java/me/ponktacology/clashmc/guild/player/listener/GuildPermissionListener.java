package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

@RequiredArgsConstructor
public class GuildPermissionListener implements Listener {

  private final GuildPlayerCache playerCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(PlayerInteractEvent event) {
    if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
    Block block = event.getClickedBlock();

    if (block.getType() != Material.CHEST && block.getType() != Material.TRAPPED_CHEST) return;

    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) return;

    Guild guild = guildPlayer.getGuild().get();

    if (!RegionUtil.isIn(block.getLocation(), guild.getRegion())) return;

    if (!guild.hasPermission(guildPlayer, GuildPermission.Permissions.CHEST_ACCESS)) {
      event.setCancelled(true);
      player.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do otwierania skrzynek. Poproś lidera o zmianę ich w /g panel."));
    }
  }
}
