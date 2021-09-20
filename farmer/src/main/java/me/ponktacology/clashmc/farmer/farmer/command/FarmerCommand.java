package me.ponktacology.clashmc.farmer.farmer.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.InventoryUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.farmer.FarmerConstants;
import me.ponktacology.clashmc.farmer.farmer.Farmer;
import me.ponktacology.clashmc.farmer.farmer.cache.FarmerCache;
import me.ponktacology.clashmc.farmer.farmer.type.RegionFarmer;
import me.ponktacology.clashmc.farmer.player.FarmerPlayer;
import me.ponktacology.clashmc.farmer.player.cache.FarmerPlayerCache;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class FarmerCommand {

  private final GuildPlayerCache guildPlayerCache;
  private final FarmerPlayerCache playerCache;
  private final FarmerCache farmerCache;

  @Command(value = "kopaczfosy", description = "Daje graczowi różdżkę kopacza fosy")
  public void execute(@Sender Player sender) {
    GuildPlayer guildPlayer = this.guildPlayerCache.getOrKick(sender);

    if (!guildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz być w gildii, aby użyć kopacza fosy."));
      return;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (!guild.hasPermission(guildPlayer, GuildPermission.Permissions.STONE_FARMER)) {
      sender.sendMessage(
          Text.colored(
              "&cNie posiadasz permisji do korzystania z kopacza fosy. Poproś lidera o zmianę ich w /g panel."));
      return;
    }

    InventoryUtil.addItem(sender, FarmerConstants.FARMER_REGION_WAND.clone());
    sender.sendMessage(
        Text.colored(
            "&aPomyślnie dodano ródżkę kopacza fosy do ekwipunku. Aby rozpocząć pracę kopacza fosy użyj komendy &7/kopaczfosy start&a."));
  }

  @Command(value = "kopaczfosy start", description = "Rozpoczyna pracę kopacza fosy")
  public void start(@Sender Player sender) {
    FarmerPlayer farmerPlayer = this.playerCache.getOrKick(sender);

    if (!farmerPlayer.isFarmerRegionReady()) {
      sender.sendMessage(Text.colored("&cMusisz ustawić oba rogi regionu kopacza fosy."));
      return;
    }

    GuildPlayer guildPlayer = this.guildPlayerCache.getOrKick(sender);

    if (!guildPlayer.hasGuild()) {
      sender.sendMessage(Text.colored("&cMusisz być w gildii."));
      return;
    }

    Guild guild = guildPlayer.getGuild().get();

    if (RegionUtil.areOverlapping(farmerPlayer.farmerRegion(), guild.getHeartRoomRegion())) {
      sender.sendMessage(Text.colored("&cTeren kopacza fosy nie może zawierać serca gildii."));
      return;
    }

    if (!InventoryUtil.hasItem(sender, Material.EMERALD_BLOCK, farmerPlayer.farmerRegionPrice())) {
      sender.sendMessage(Text.colored("&cNie posiadasz wymaganych przedmiotów."));
      return;
    }

    InventoryUtil.removeItem(sender, Material.EMERALD_BLOCK, farmerPlayer.farmerRegionPrice());

    Farmer farmer = new RegionFarmer(farmerPlayer.farmerRegion(), Material.AIR);

    this.farmerCache.add(farmer);
    farmer.start();
    sender.sendMessage(Text.colored("&aPomyślnie rozpoczęto kopanie regionu."));
  }
}
