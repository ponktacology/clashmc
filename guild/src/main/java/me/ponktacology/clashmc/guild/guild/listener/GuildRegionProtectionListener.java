package me.ponktacology.clashmc.guild.guild.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.guild.settings.cache.GuildSettingsCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@RequiredArgsConstructor
public class GuildRegionProtectionListener implements Listener {

  private final GuildCache guildCache;
  private final GuildSettingsCache settingsCache;
  private final CorePlayerCache corePlayerCache;
  private final GuildPlayerCache playerCache;

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockBreakEvent(BlockBreakEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    Block block = event.getBlock();
    Location location = block.getLocation();

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    guildPlayer
        .getGuild(location)
        .ifPresent(
            it -> {
              if (!it.hasMember(guildPlayer)) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored("&cNie możesz nisczyć bloków na terenie wrogiej gildii."));
                return;
              }

              if (!it.hasPermission(guildPlayer, GuildPermission.Permissions.BLOCK_BREAK)) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored(
                        "&cNie posiadasz permisji do niszczenia bloków na terenie gildii. Poproś lidera o zmianę ich w /g panel."));
              }
            });
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onBlockPlaceEvent(BlockPlaceEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    Block block = event.getBlock();
    Location location = block.getLocation();

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    guildPlayer
        .getGuild(location)
        .ifPresent(
            it -> {
              if (!it.hasMember(guildPlayer)) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored("&cNie możesz stawiać bloków na terenie wrogiej gildii."));
                return;
              }

              if (!it.hasPermission(guildPlayer, GuildPermission.Permissions.BLOCK_PLACE)) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored(
                        "&cNie posiadasz permisji do stawiać bloków na terenie gildii. Poproś lidera o zmianę ich w /g panel."));
              }

              if (block.getY() < 50 && guildPlayer.hasCombatTag()) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored(
                        "&cNie możesz stawiać bloków poniżej 50 poziomu Y podczas walki."));
                return;
              }

              if (it.hasTntExplodedRecently()) {
                event.setCancelled(true);
                player.sendMessage(
                    Text.colored(
                        "&cNie możesz stawiać bloków, gdyż na terenie twojej gildii niedawno wybuchło TnT.\n&cKolejny raz możesz postawić blok za "
                            + TimeUtil.formatTimeMillis(it.timeTillTnTExplosionEviction())
                            + "."));
                return;
              }
            });
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onPlayerDropItemEvent(PlayerBucketEmptyEvent event) {
    Player player = event.getPlayer();

    CorePlayer corePlayer = this.corePlayerCache.getOrKick(player);
    if (corePlayer.isStaff()) return;

    Block block = event.getBlockClicked();
    Location location = block.getLocation();

    GuildPlayer guildPlayer = this.playerCache.getOrKick(player);

    /*guildPlayer
    .getGuild(location)
    .ifPresent(
        it -> {
          if (!it.hasMember(guildPlayer)) {
            event.setCancelled(true);
            player.sendMessage(
                Text.colored("&cNie możesz stawiać bloków na terenie wrogiej gildii."));
            return;
          }

          if (!it.hasPermission(guildPlayer, GuildPermission.Permissions.BLOCK_PLACE)) {
            event.setCancelled(true);
            player.sendMessage(
                Text.colored(
                    "&cNie posiadasz permisji do stawiać bloków na terenie gildii. Poproś lidera o zmianę ich w /g panel."));
          }
        }); */
  }

  @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
  public void onEntityExplodeEvent(EntityExplodeEvent event) {
    if (this.settingsCache.get().isEnabledTnt()) {

      if (event.getLocation().getY() > 50) {
        event.blockList().clear();
        this.getSource(event)
            .ifPresent(it -> it.sendMessage(Text.colored("&cTnt działa poniżej 50 poziomu Y.")));
        return;
      }

      this.removeIfAndWarn(
          event,
          guild -> {
            boolean flag = guild.hasCreationProtection() && !guild.isTnt();

            if (!flag) {
              guild.resetLastTntExplode();
            }

            return flag;
          },
          false,
          Text.colored("&cTa gildia posiada ochronę przed TnT."));
      return;
    }

    this.removeIfAndWarn(
        event, it -> !it.isTnt(), true, Text.colored("&cTnT jest aktualnie wyłączone."));
  }

  private void removeIfAndWarn(
      EntityExplodeEvent event,
      Function<Guild, Boolean> supplier,
      boolean defaultValue,
      String warnMessage) {
    AtomicBoolean warned = new AtomicBoolean();

    event
        .blockList()
        .removeIf(
            block -> {
              Location location = block.getLocation();
              Optional<Guild> guildOptional = this.guildCache.getByLocation(location);

              if (!guildOptional.isPresent()) return defaultValue;

              Guild guild = guildOptional.get();

              boolean remove = supplier.apply(guild);

              if (remove && !warned.get()) {
                this.getSource(event).ifPresent(it -> it.sendMessage(warnMessage));
                warned.set(true);
              }

              return remove;
            });
  }

  private Optional<Player> getSource(EntityExplodeEvent event) {
    if (event.getEntity() instanceof TNTPrimed) {
      TNTPrimed tntPrimed = (TNTPrimed) event.getEntity();
      Entity source = tntPrimed.getSource();

      if (source instanceof Player) {
        Player player = (Player) source;

        return Optional.of(player);
      }
    }

    return Optional.empty();
  }
}
