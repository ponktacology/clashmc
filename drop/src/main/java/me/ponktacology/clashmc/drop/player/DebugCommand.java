package me.ponktacology.clashmc.drop.player;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.core.CorePermissions;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.crate.player.cache.CratePlayerCache;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.safe.player.cache.SafePlayerCache;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class DebugCommand {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;
  private final DropPlayerCache dropPlayerCache;
  private final CratePlayerCache cratePlayerCache;
  private final SafePlayerCache safePlayerCache;

  @Command(value = "debug")
  @Permission(CorePermissions.DEV)
  public void debug(@Sender CommandSender sender) {
    sender.sendMessage("Online: " + Bukkit.getServer().getOnlinePlayers().size());
    sender.sendMessage("Cached guild players: " + this.playerCache.values().size());
    sender.sendMessage("Cached core players: " + this.corePlayerCache.values().size());
    sender.sendMessage("Cached drop players: " + this.dropPlayerCache.values().size());
    sender.sendMessage("Cached crate players: " + this.cratePlayerCache.values().size());
    sender.sendMessage("Cached safe players: " + this.safePlayerCache.values().size());

    List<Player> distinct = Lists.newArrayList();

    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      if (!this.corePlayerCache.get(player).isPresent()) {
        distinct.add(player);
      }
    }

    for (Player player : distinct) {
      log.info("PLAYER NOT FOUND: " + player.getName() + " uuid " + player.getUniqueId());
    }
  }
}
