package me.ponktacology.clashmc.guild.player.tab;

import io.github.thatkawaiisam.ziggurat.ZigguratAdapter;
import io.github.thatkawaiisam.ziggurat.utils.BufferedTabObject;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.api.sector.cache.SectorCache;
import org.bukkit.entity.Player;



import java.util.Collections;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class TabAdapter implements ZigguratAdapter {


  private final GuildPlayerCache playerCache;

  private final SectorCache sectorCache;

  @Override
  public Set<BufferedTabObject> getSlots( Player player) {
    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(player);

    if (!guildPlayerOptional.isPresent()) {
      return Collections.emptySet();
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    return guildPlayer.getCachedTabObjects();
  }

  
  @Override
  public String getFooter() {
    return Text.colored(
        "&7Strona: &ewww.clashmc.pl &8| &7Discord: &edc.clashmc.pl\n&7TeamSpeak: &ets.clashmc.pl &8| &7Fanpage: &efb.clashmc.pl");
  }

  
  @Override
  public String getHeader() {
    return Text.colored(
        "&4&m-----&8&m[---&f &c&lClash&f&lMC.pl &8&m---]&4&m-----\n&7Online: &e"
            + this.sectorCache.getMaskedPlayersCount()
            + " + "
            + this.sectorCache.getSpecialPlayersCount());
  }
}
