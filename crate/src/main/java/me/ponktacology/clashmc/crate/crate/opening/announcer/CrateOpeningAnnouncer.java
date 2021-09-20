package me.ponktacology.clashmc.crate.crate.opening.announcer;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.announcer.Announcer;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.crate.crate.opening.CrateOpening;
import me.ponktacology.clashmc.crate.crate.opening.announcer.packet.PacketCrateOpeningAnnounce;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;

@RequiredArgsConstructor
public class CrateOpeningAnnouncer implements Announcer<CrateOpening, PacketCrateOpeningAnnounce> {

  private final NetworkService networkService;

  private final GuildPlayerCache playerCache;

  @Override
  public void announce(CrateOpening crateOpening) {
    String message =
        "&eGracz &f"
            + crateOpening.getPlayer().getName()
            + " &eotworzył skrzynkę &6"
            + crateOpening.getCrate().getName()
            + "&e i wylosował &a"
            + crateOpening.getReward().getDisplayName()
            + "&e!";

    this.networkService.publish(new PacketCrateOpeningAnnounce(message));
  }

  @PacketHandler
  public void handle(PacketCrateOpeningAnnounce packet) {
    for (GuildPlayer guildPlayer : this.playerCache.values()) {
      if (!guildPlayer.isChatSettingEnabled(ChatSettings.Settings.CRATE_MESSAGES)) continue;

      guildPlayer.sendMessage(Text.colored(packet.getMessage()));
    }
  }
}
