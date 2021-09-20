package me.ponktacology.clashmc.guild.nametag;

import jodd.util.StringUtil;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import org.bukkit.entity.Player;

import java.util.Optional;

public class GuildNameTagProvider extends NametagProvider {

  private final GuildPlayerCache playerCache;
  private final CorePlayerCache corePlayerCache;

  public GuildNameTagProvider(
      GuildPlayerCache playerCache,
      me.ponktacology.clashmc.core.player.cache.CorePlayerCache CorePlayerCache) {
    super("NameTag Provider", 5);
    this.playerCache = playerCache;
    this.corePlayerCache = CorePlayerCache;
  }

  @Override
  public NametagInfo fetchNametag(Player toRefresh, Player refreshFor) {
    Player player = refreshFor;
    Player other = toRefresh;

    Optional<GuildPlayer> guildPlayerOptional = this.playerCache.get(other);

    if (!guildPlayerOptional.isPresent()) {
      return createNametag("", "");
    }

    GuildPlayer guildPlayer = guildPlayerOptional.get();

    Optional<GuildPlayer> playerGuildPlayerOptional = this.playerCache.get(player);

    if (!playerGuildPlayerOptional.isPresent()) {
      return createNametag("", "");
    }

    GuildPlayer playerGuildPlayer = playerGuildPlayerOptional.get();

    Optional<CorePlayer> corePlayerOptional = this.corePlayerCache.get(other);

    if (!corePlayerOptional.isPresent()) {
      return createNametag("", "");
    }

    CorePlayer corePlayer = corePlayerOptional.get();
    Rank rank = corePlayer.getMainRank();

    String prefix = "";

    if (!rank.isStaff()) {

      if (guildPlayer.hasSpawnProtection()) {
        prefix = "&e[OCHRONA] &f";
      } else if (guildPlayer.hasGuild()) {
        Guild guild = guildPlayer.getGuild().get(); // this can't be null

        if (guild.hasMember(player.getUniqueId())) {
          prefix = "&a[" + guild.getTag() + "] ";
        } else if (playerGuildPlayer.hasGuild()) {
          Guild otherGuild = playerGuildPlayer.getGuild().get();
          if (guild.isInWar(otherGuild)) {
            prefix = "&4[" + guild.getTag() + "] ";
          } else if (guild.hasAlly(otherGuild)) {
            prefix = "&9[" + guild.getTag() + "] ";
          } else prefix = "&c[" + guild.getTag() + "] ";
        } else {
          prefix = "&c[" + guild.getTag() + "] ";
        }
      }
    }

    Optional<CorePlayer> playerCorePlayerOptional = this.corePlayerCache.get(player);

    if (!playerCorePlayerOptional.isPresent()) {
      return createNametag("", "");
    }

    CorePlayer playerCorePlayer = playerCorePlayerOptional.get();

    String suffix = "";
    boolean incognito = guildPlayer.isIncognitoEnabled();
    if (incognito && playerCorePlayer.isStaff()) {
      suffix = StringUtil.substring(" " + guildPlayer.getName(), 0, 16);
    } else if (!incognito && rank.getPower() > 0) {
      suffix = rank.getPower() > 0 ? " " + rank.getColor() + rank.getName() : "&f";
    }

    if (corePlayer.isVanished() && playerCorePlayer.isStaff()) {
      prefix += "&o";
    }

    return createNametag(Text.colored(prefix), Text.colored(suffix));
  }
}
