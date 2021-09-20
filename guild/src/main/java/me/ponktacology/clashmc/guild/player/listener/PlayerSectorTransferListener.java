package me.ponktacology.clashmc.guild.player.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.player.transfer.event.AsyncPlayerSectorTransferEvent;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@RequiredArgsConstructor
public class PlayerSectorTransferListener implements Listener {

  private final CorePlayerCache corePlayerCache;
  private final GuildPlayerCache playerCache;

  @EventHandler(ignoreCancelled = true)
  public void onPlayerSectorTransfer(AsyncPlayerSectorTransferEvent event) {
    Player player = event.getPlayer();
    Optional<GuildPlayer> guildPlayerOptional = playerCache.get(player);

    if (!guildPlayerOptional.isPresent()) {
      event.setCancelled(true);
      return;
    }

    Optional<CorePlayer> corePlayerOptional = this.corePlayerCache.get(player);

    if (!corePlayerOptional.isPresent()) {
      event.setCancelled(true);
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();

    if (corePlayer.isStaff()) return;

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    if (guildPlayer.hasCombatTag()) {
      player.sendMessage(Text.colored("&cNie możesz przejść przez sektor podczas walki."));
      event.setCancelled(true);
      RegionUtil.knock(SectorPlugin.INSTANCE.getLocalSector(), player);
    }
  }
}
