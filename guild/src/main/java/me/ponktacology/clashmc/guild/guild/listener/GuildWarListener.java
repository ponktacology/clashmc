package me.ponktacology.clashmc.guild.guild.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.action.ActionType;
import me.ponktacology.clashmc.guild.guild.action.GuildWarAction;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.updater.GuildUpdater;
import me.ponktacology.clashmc.guild.guild.war.action.GuildWarActionAnnouncer;
import me.ponktacology.clashmc.guild.guild.war.updater.GuildWarUpdater;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

@RequiredArgsConstructor
public class GuildWarListener implements Listener {

  private final GuildCache guildCache;
  private final GuildPlayerCache playerCache;
  private final GuildWarUpdater warUpdater;
  private final GuildUpdater guildUpdater;
  private final GuildWarActionAnnouncer warActionAnnouncer;
  private final TaskDispatcher taskDispatcher;

  @EventHandler(ignoreCancelled = true)
  public void onEvent(BlockBreakEvent event) {
    Block block = event.getBlock();

    if (block.getType() != Material.EMERALD_BLOCK) return;

    Optional<Guild> guildOptional = this.guildCache.getByCenter(block.getLocation());

    if (!guildOptional.isPresent()) {
      return;
    }

    event.setCancelled(true);

    Guild guild = guildOptional.get();

    Player player = event.getPlayer();
    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) {
      player.sendMessage(Text.colored("&cMusisz być w gildii, aby zniszczyć serce innej gildii."));
      return;
    }

    Guild playerGuild = guildPlayer.getGuild().get();

    if (guild.equals(playerGuild)) {
      player.sendMessage(Text.colored("&cNie możesz zniszczyć serca własnej gildii."));
      return;
    }

    if (!guild.isInWar(playerGuild)) {
      player.sendMessage(
          Text.colored("&cMusisz być podczas wojny z gildią, której serce chcesz zniszczyć."));
      return;
    }

    if (!guild.canBeConquered()) {
      player.sendMessage(
          Text.colored(
              "&cTa gildia może zostać podbita za "
                  + TimeUtil.formatTimeMillis(guild.timeTillCanBeConquered())
                  + "."));
      return;
    }

    int health = guild.destroyHeart();
    if (health <= 0) {
      if (guild.conquer() <= 0) {
        this.taskDispatcher.runAsync(
            () -> {
              this.warActionAnnouncer.announce(
                  new GuildWarAction(ActionType.CONQUER, playerGuild, guild));
              this.guildUpdater.remove(guild);
            });
      } else {
        this.taskDispatcher.runAsync(() -> this.warUpdater.remove(playerGuild, guild));
      }
    } else {
      player.sendMessage(
          Text.colored(
              "&eZniszczono serce gildii &6" + health + "&e/" + GuildConstants.MAX_HEALTH + "."));
    }
  }
}
