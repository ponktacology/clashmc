package me.ponktacology.clashmc.guild.player.tab.cache;

import com.google.common.collect.Sets;
import io.github.thatkawaiisam.ziggurat.utils.BufferedTabObject;
import io.github.thatkawaiisam.ziggurat.utils.SkinTexture;
import io.github.thatkawaiisam.ziggurat.utils.TabColumn;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.api.util.MojangUtil;
import me.ponktacology.clashmc.api.util.Pair;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.cache.CorePlayerCache;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.api.sector.Sector;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class TabCache {

  private final GuildPlayerCache playerCache = GuildPlugin.INSTANCE.getPlayerCache();
  private final GuildCache guildCache = GuildPlugin.INSTANCE.getGuildCache();
  private final CorePlayerCache corePlayerCache = CorePlugin.INSTANCE.getPlayerCache();
  private final Sector localSector = SectorPlugin.INSTANCE.getLocalSector();
  private final TaskDispatcher taskDispatcher = CorePlugin.INSTANCE.getTaskDispatcher();

  private long lastFetch;
  private final Set<BufferedTabObject> cachedObjects = Sets.newConcurrentHashSet();

  public Set<BufferedTabObject> getCachedTabObjects(GuildPlayer guildPlayer) {
    Player player = guildPlayer.getPlayer();
    if (player == null) return Collections.emptySet();

    if (System.currentTimeMillis() - this.lastFetch < GuildConstants.TAB_CACHE_EVICTION_TIME) {
      return this.cachedObjects;
    }

    this.lastFetch = System.currentTimeMillis();
    this.taskDispatcher.runAsync(() -> this.fetch(guildPlayer, player));

    return this.cachedObjects;
  }

  public void fetch(GuildPlayer guildPlayer, Player player) {
    Set<BufferedTabObject> objects = Sets.newHashSet();

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(2)
            .text(Text.colored("    &cSTATYSTYKI")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(4)
            .text(Text.colored("&7Nick: &f" + guildPlayer.getName())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(5)
            .text(Text.colored("&7Ranking: &f" + guildPlayer.getRank())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(6)
            .text(Text.colored("&7Zabójstwa: &f" + guildPlayer.getKills())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(7)
            .text(Text.colored("&7Asysty: &f" + guildPlayer.getAssists())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(8)
            .text(Text.colored("&7Śmierci: &f" + guildPlayer.getDeaths())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.LEFT)
            .slot(9)
            .text(Text.colored("&7K/D: &f" + guildPlayer.getKD())));

    if (guildPlayer.hasGuild()) {
      Guild guild = guildPlayer.getGuild().get();

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(11)
              .text(Text.colored("    &cGILDIA")));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(13)
              .text(Text.colored("&7Tag: &f" + guild.getTag())));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(14)
              .text(Text.colored("&7Ranking: &f" + guild.rank())));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(15)
              .text(Text.colored("&7Zabójstwa: &f" + guild.kills())));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(16)
              .text(Text.colored("&7Śmierci: &f" + guild.deaths())));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(17)
              .text(Text.colored("&7KillStreak: &f" + guild.killStreak())));

      String leaderName = guild.leader().getName();

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.LEFT)
              .slot(18)
              .text(Text.colored("&7Lider: &f" + leaderName)));

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.RIGHT)
              .slot(19)
              .text(Text.colored("&8#" + this.guildCache.getPosition(guild) + " &fTwoja gildia")));
    }

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.MIDDLE)
            .slot(2)
            .text(Text.colored("    &cTOPKA GRACZY")));

    BufferedTabObject tabObject =
        new BufferedTabObject()
            .column(TabColumn.MIDDLE)
            .slot(19)
            .text(
                Text.colored(
                    "&8#" + this.playerCache.getPosition(guildPlayer) + " &fTwoja pozycja"));

    Pair<String, String> skin = MojangUtil.getSkinAndSignature(guildPlayer.getUuid());

    if (skin != null) {
      tabObject.setSkinTexture(new SkinTexture(skin.getKey(), skin.getValue()));
    }

    objects.add(tabObject);

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.RIGHT)
            .slot(2)
            .text(Text.colored("    &cTOPKA GILDII")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(2)
            .text(Text.colored("    &cINFORMACJE")));

    Optional<CorePlayer> corePlayerOptional = this.corePlayerCache.get(player);

    if (!corePlayerOptional.isPresent()) {
      return;
    }

    CorePlayer corePlayer = corePlayerOptional.get();
    Rank rank = corePlayer.getMainRank();

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(4)
            .text(Text.colored("&7Ranga: &f" + rank.getFormattedName())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(5)
            .text(Text.colored("&7Sektor: &f" + this.localSector.getName())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(6)
            .text(Text.colored("&7Założonych gildii: &f" + this.guildCache.size())));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(7)
            .text(Text.colored("&7Ping: &f" + StyleUtil.colorPing(player.spigot().getPing()))));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(8)
            .text(
                Text.colored(
                    "&7Godzina: &f"
                        + TimeUtil.formatTimeMillisToTimeOnlyDate(System.currentTimeMillis()))));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(10)
            .text(Text.colored("    &cPRZYDATNE")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(12)
            .text(Text.colored("&7Lista komend: &f/pomoc")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(13)
            .text(Text.colored("&7Komendy gildyjne: &f/g")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(14)
            .text(Text.colored("&7Dropy: &f/drop")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(15)
            .text(Text.colored("&7Zarządzanie czatem: &f/cc")));

    objects.add(
        new BufferedTabObject()
            .column(TabColumn.FAR_RIGHT)
            .slot(16)
            .text(Text.colored("&7Craftingi: &f/craftingi")));

    for (int i = 0; i < 15; i++) {
      Guild guild = this.guildCache.getTop(i).orElse(null);

      objects.add(
          new BufferedTabObject()
              .column(TabColumn.RIGHT)
              .slot(4 + i)
              .text(
                  Text.colored(
                      "&8#"
                          + (i + 1)
                          + (guild == null ? "" : " &f" + guild.getTag() + " &e" + guild.rank()))));

      GuildPlayer guildPlayer1 = this.playerCache.getTop(i).orElse(null);

      BufferedTabObject object =
          new BufferedTabObject()
              .column(TabColumn.MIDDLE)
              .slot(4 + i)
              .text(
                  Text.colored(
                      "&8#"
                          + (i + 1)
                          + (guildPlayer1 == null
                              ? ""
                              : " &f"
                                  + guildPlayer1.getName()
                                  + " &e"
                                  + guildPlayer1.getStatistics().getRank())));

      if (guildPlayer1 != null) {
        Pair<String, String> skinAndSignature =
            MojangUtil.getSkinAndSignature(guildPlayer1.getUuid());

        if (skinAndSignature != null) {
          object.setSkinTexture(
              new SkinTexture(skinAndSignature.getKey(), skinAndSignature.getValue()));
        }
      }

      objects.add(object);
    }

    this.cachedObjects.clear();
    this.cachedObjects.addAll(objects);
  }
}
