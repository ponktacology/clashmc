package me.ponktacology.clashmc.farmer.farmer.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.farmer.FarmerConstants;
import me.ponktacology.clashmc.farmer.player.FarmerPlayer;
import me.ponktacology.clashmc.farmer.player.cache.FarmerPlayerCache;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

@RequiredArgsConstructor
public class FarmerRegionWandListener implements Listener {

  private final FarmerPlayerCache playerCache;
  private final GuildPlayerCache guildPlayerCache;

  @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
  public void onEvent(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    ItemStack itemInHand = event.getItem();

    if (itemInHand == null) return;

    if (!itemInHand.isSimilar(FarmerConstants.FARMER_REGION_WAND)) {
      return;
    }

    GuildPlayer guildPlayer = this.guildPlayerCache.getOrKick(player);

    if (!guildPlayer.hasGuild()) {
      player.sendMessage(Text.colored("&cMusisz być w gildii, aby użyć kopacza fosy."));
      return;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (!guild.hasPermission(guildPlayer, GuildPermission.Permissions.STONE_FARMER)) {
      player.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do korzystania z kopacza fosy. Poproś lidera o zmianę ich w /g panel."));
      return;
    }

    FarmerPlayer farmerPlayer = this.playerCache.getOrKick(player);
    Location corner = event.getClickedBlock().getLocation();
    Action action = event.getAction();

    if (!guild.isIn(corner)) {
      player.sendMessage(
          Text.colored("&cMożesz użyć kopacza fosy tylko na terenie swojej gildii."));
      return;
    }

    if (guild.isInHeartRoomIgnoreY(corner)) {
      player.sendMessage(Text.colored("&cNie możesz użyć kopacza fosy tak blisko serca gildii."));
      return;
    }

    switch (action) {
      case RIGHT_CLICK_BLOCK:
        {
          farmerPlayer.setFirstCorner(corner);
          player.sendMessage(
              Text.colored("&aPomyślnie ustawiono pierwszy róg regionu kopacza fosy."));

          if (farmerPlayer.isFarmerRegionReady()) {
            if (RegionUtil.areOverlapping(
                farmerPlayer.farmerRegion(), guild.getHeartRoomRegion())) {
              player.sendMessage(
                  Text.colored("&cTeren kopacza fosy nie może zawierać serca gildii."));
              return;
            }

            player.sendMessage(
                Text.colored(
                    "&eCena wykopania tego regionu: &f"
                        + farmerPlayer.farmerRegionPrice()
                        + " &abloków szmaragdu"));
            return;
          }
          break;
        }
      case LEFT_CLICK_BLOCK:
        {
          farmerPlayer.setSecondCorner(corner);
          player.sendMessage(Text.colored("&aPomyślnie ustawiono drugi róg regionu kopacza fosy."));

          if (farmerPlayer.isFarmerRegionReady()) {
            if (RegionUtil.areOverlapping(
                farmerPlayer.farmerRegion(), guild.getHeartRoomRegion())) {
              player.sendMessage(
                  Text.colored("&cTeren kopacza fosy nie może zawierać serca gildii."));
              return;
            }

            player.sendMessage(
                Text.colored(
                    "&eCena wykopania tego regionu: &f"
                        + farmerPlayer.farmerRegionPrice()
                        + " &abloków szmaragdu"));
            return;
          }
          break;
        }
    }
  }
}
