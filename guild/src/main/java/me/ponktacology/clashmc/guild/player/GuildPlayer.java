package me.ponktacology.clashmc.guild.player;

import com.google.common.base.Strings;
import com.mojang.authlib.GameProfile;
import io.github.thatkawaiisam.ziggurat.utils.BufferedTabObject;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.player.cache.DamageCache;
import me.ponktacology.clashmc.guild.player.cache.KillCache;
import me.ponktacology.clashmc.guild.player.chat.ChatSettings;
import me.ponktacology.clashmc.guild.player.combattag.PlayerCombatTagSettings;
import me.ponktacology.clashmc.guild.player.cps.CPSLimiter;
import me.ponktacology.clashmc.guild.player.home.PlayerHome;
import me.ponktacology.clashmc.guild.player.home.cache.PlayerHomeCache;
import me.ponktacology.clashmc.guild.player.incognito.IncognitoSettings;
import me.ponktacology.clashmc.guild.player.region.cache.RegionCache;
import me.ponktacology.clashmc.guild.player.spawnprotection.SpawnProtectionSettings;
import me.ponktacology.clashmc.guild.player.statistics.PlayerStatistics;
import me.ponktacology.clashmc.guild.player.tab.cache.TabCache;
import me.ponktacology.clashmc.guild.util.Tuple;
import org.bukkit.Location;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "guild")
public class GuildPlayer extends BukkitPlayerWrapper {

  private final PlayerStatistics statistics = new PlayerStatistics();
  private final IncognitoSettings incognitoSettings = new IncognitoSettings();
  private final ChatSettings chatSettings = new ChatSettings();
  private final SpawnProtectionSettings spawnProtectionSettings = new SpawnProtectionSettings();
  private final KillCache killCache = new KillCache();
  private final PlayerHomeCache homeCache = new PlayerHomeCache();

  private final transient PlayerCombatTagSettings combatTagSettings = new PlayerCombatTagSettings();
  private final transient DamageCache damageCache = new DamageCache();
  private final transient RegionCache regionCache = new RegionCache();
  private final transient TabCache tabCache = new TabCache();
  private final transient CPSLimiter cpsLimiter = new CPSLimiter();
  private transient Location deathLocation;

  private transient long lastConsumeTime;

  private String guild;

  public GuildPlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public void addHome(PlayerHome home) {
    this.homeCache.add(home);
  }

  public Optional<PlayerHome> getHome(String name) {
    return this.homeCache.get(name);
  }

  public Optional<PlayerHome> removeHome(String name) {
    return this.homeCache.remove(name);
  }

  public Set<PlayerHome> getHomes() {
    return this.homeCache.values();
  }

  public int homesSize() {
    return this.homeCache.size();
  }

  public boolean hasGuild() {
    return getGuild().isPresent();
  }

  public Optional<Guild> getGuild() {
    if (Strings.isNullOrEmpty(this.guild)) return Optional.empty();

    Optional<Guild> guildOptional = GuildPlugin.INSTANCE.getGuildCache().getByTag(this.guild);

    if (!guildOptional.isPresent()) {
      this.guild = null;
    }

    return guildOptional;
  }

  public long getPlayTime() {
    return this.statistics.getPlayTime(this);
  }

  public String getPlayTimeFormatted() {
    return TimeUtil.formatTimeMillis(this.getPlayTime());
  }

  public int getAssists() {
    return this.statistics.assists();
  }

  public int getKills() {
    return this.statistics.kills();
  }

  public int getDeaths() {
    return this.statistics.deaths();
  }

  public double getKD() {
    return this.statistics.getKD();
  }

  public Set<Tuple<UUID, Double>> getHighestDamages() {
    return this.getDamageCache().getHighestDamageAttackers(0.4).stream()
        .filter(it -> it.getKey().equals(this.getUuid()))
        .collect(Collectors.toSet());
  }

  public boolean isIncognitoEnabled() {
    return this.incognitoSettings.isEnabled();
  }

  public String getIncognitoName() {
    return this.incognitoSettings.getName();
  }

  public void setCachedIncognitoGameProfile(GameProfile gameProfile) {
    this.incognitoSettings.setCachedGameProfile(gameProfile);
  }

  public Optional<GameProfile> getCachedIncognitoGameProfile() {
    return Optional.ofNullable(this.incognitoSettings.getCachedGameProfile());
  }

  public boolean tickSpawnProtection() {
    return this.spawnProtectionSettings.tick();
  }

  public boolean hasSpawnProtection() {
    return !this.spawnProtectionSettings.hasExpired();
  }

  public boolean hasCombatTag() {
    return !this.getCombatTagSettings().hasExpired();
  }

  public long getCombatTagTime() {
    return (this.getCombatTagSettings().getLastCombatTagReset()
            + GuildConstants.COMBAT_TAG_TIME
            - System.currentTimeMillis())
        / 1000L;
  }

  public int getSpawnProtectionTime() {
    return this.spawnProtectionSettings.getTime();
  }

  public float getSpawnProtectionProgress() {
    return (float) this.spawnProtectionSettings.getTime()
        / (float) GuildConstants.SPAWN_PROTECTION_SECONDS;
  }

  public float getCombatTagProgress() {
    return (float) this.getCombatTagTime() / (float) (GuildConstants.COMBAT_TAG_TIME / 1000L);
  }

  public int getRank() {
    return this.statistics.getRank();
  }

  public void increaseRank(int rank) {
    this.statistics.increaseRank(rank);
  }

  public void increaseUsedPearls() {
    this.statistics.increaseUsedPearls();
  }

  public void increaseDealtDamage(double damage) {
    this.statistics.increaseDealtDamage(damage);
  }

  public void increaseReceivedDamage(double damage) {
    this.statistics.increaseReceivedDamage(damage);
  }

  public void resetCombatTag() {
    this.combatTagSettings.reset();
  }

  public void disableCombatTag() {
    this.combatTagSettings.disable();
  }

  public boolean isChatSettingEnabled(ChatSettings.Settings setting) {
    return this.chatSettings.isEnabled(setting);
  }

  public void setGuild(Guild guild) {
    if (guild == null) {
      this.guild = "";
    } else {
      this.guild = guild.getTag();
    }
  }

  public void disableSpawnProtection() {
    this.spawnProtectionSettings.disable();
  }

  public boolean hasKilledBefore(GuildPlayer guildPlayer) {
    return this.killCache.hasKilledBefore(guildPlayer);
  }

  public void addKilledBefore(GuildPlayer guildPlayer) {
    this.killCache.add(guildPlayer);
  }

  public Set<BufferedTabObject> getCachedTabObjects() {
    return this.tabCache.getCachedTabObjects(this);
  }

  public Optional<Guild> getLastGuild() {
    return this.regionCache.getLastGuild();
  }

  public void spawnIncognitoHologramForStaffMembers() {
    //    this.incognitoHologram.spawnForStaffMembersAndGuildMembers();
  }

  public void destroyIncognitoHologramForStaffMembers() {
    //  this.incognitoHologram.destroyForStaffMembersAndGuildMembers();
  }

  public void spawnIncognitoHologram(GuildPlayer guildPlayer) {
    //  this.incognitoHologram.spawn(guildPlayer.getPlayer());
  }

  public Optional<Guild> getGuild(Location location) {
    Optional<Guild> guildOptional = this.getLastGuild();

    Guild guild = null;
    if (guildOptional.isPresent()) {
      guild = guildOptional.get();

      if (!guild.isIn(location)) {
        guild = null;
      }
    }

    if (guild == null) {
      guildOptional = GuildPlugin.INSTANCE.getGuildCache().getByLocation(location);

      if (guildOptional.isPresent()) {
        guild = guildOptional.get();
      }
    }

    return Optional.ofNullable(guild);
  }

  public void addPlayTime() {
    this.statistics.addPlayTime(this);
  }

  public void increaseKills(
      GuildPlayer killerGuildPlayer, GuildPlayer guildPlayer, int rankChange) {
    this.statistics.increaseKills(killerGuildPlayer, guildPlayer, rankChange);
  }

  public void decreaseRank(int rank) {
    this.statistics.decreaseRank(rank);
  }

  public void increaseDeaths(
      GuildPlayer killerGuildPlayer, GuildPlayer guildPlayer, int rankChange) {
    this.statistics.increaseDeaths(killerGuildPlayer, guildPlayer, rankChange);
  }

  public boolean toggleIncognito() {
    return this.incognitoSettings.toggle();
  }

    public void resetStatistics() {
    this.statistics.reset();
    }
}
