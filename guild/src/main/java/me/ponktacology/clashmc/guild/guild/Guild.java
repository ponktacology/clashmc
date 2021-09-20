package me.ponktacology.clashmc.guild.guild;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.Expiring;
import me.ponktacology.clashmc.api.SimpleRegion;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.blazingpack.cuboid.CuboidHelper;
import me.ponktacology.clashmc.core.blazingpack.cuboid.CustomPayload;
import me.ponktacology.clashmc.core.blazingpack.cuboid.MessagedRectangle;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.guild.GuildConstants;
import me.ponktacology.clashmc.guild.GuildPlugin;
import me.ponktacology.clashmc.guild.guild.action.GuildAction;
import me.ponktacology.clashmc.guild.guild.action.GuildActionWrapper;
import me.ponktacology.clashmc.guild.guild.action.GuildWarAction;
import me.ponktacology.clashmc.guild.guild.action.GuildWarActionWrapper;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermission;
import me.ponktacology.clashmc.guild.guild.permission.GuildPermissions;
import me.ponktacology.clashmc.guild.guild.region.GuildRegion;
import me.ponktacology.clashmc.guild.guild.war.GuildWar;
import me.ponktacology.clashmc.guild.guild.war.wrapper.GuildWarWrapper;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.util.SchematicUtil;
import me.ponktacology.clashmc.sector.SectorPlugin;
import me.ponktacology.clashmc.sector.sector.util.RegionUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Data
@NoArgsConstructor
@Entity(collection = "guilds", database = "guild")
public final class Guild implements Expiring {

  private final Map<UUID, GuildRole> members = Maps.newConcurrentMap();
  private final GuildPermissions permissions = new GuildPermissions();
  private final List<GuildActionWrapper> actions = Lists.newArrayList();
  private final List<GuildWarActionWrapper> warActions = Lists.newArrayList();
  private final long createdOn = System.currentTimeMillis();
  private final Set<String> allies = new HashSet<>();
  private final List<GuildWarWrapper> wars = Lists.newArrayList();
  private final Map<String, Long> warTimeStamps = Maps.newConcurrentMap();
  private final GuildStatistics statistics = new GuildStatistics();

  @SerializedName("_id")
  private String tag;

  private String name;
  private UUID creator;
  private Location center;
  private Location base;
  private GuildRegion region;
  private SimpleRegion heartRoomRegion;
  private int health = GuildConstants.MAX_HEALTH;
  private int lives = GuildConstants.MAX_LIVES;
  private boolean friendlyFire = false;
  private boolean tnt = false;
  private long lastRenewTime = System.currentTimeMillis();
  private int regionSize = GuildConstants.BASE_GUILD_REGION_SIZE;
  private long lastTntExplode;
  private long lastConquer;

  private final transient com.github.benmanes.caffeine.cache.Cache<UUID, Boolean> memberInvites =
      Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();
  private final transient com.github.benmanes.caffeine.cache.Cache<Guild, Boolean> allyInvites =
      Caffeine.newBuilder().expireAfterWrite(5, TimeUnit.MINUTES).build();

  public Guild(String tag, String name, Player creator, Location center) {
    this.tag = tag;
    this.name = name;
    this.creator = creator.getUniqueId();
    this.center = center.add(0.5, 0, 0.5);
    this.center.setY(20);
    this.base = this.center;
    this.region = this.createRegion();
    this.heartRoomRegion = this.createHeartRoomRegion();
    this.members.put(this.creator, GuildRole.LEADER);

    if (RegionUtil.isIn(this.center, SectorPlugin.INSTANCE.getLocalSector())) {
      CorePlugin.INSTANCE
          .getTaskDispatcher()
          .run(
              () -> {
                this.spawnHeartRoom(this.center);
                this.spawnHeart();
              });
    }
  }

  public void spawnHeart() {
    this.createHologram();
  }

  public void despawnHeart() {
    this.deleteHologram();
    HologramsAPI.unregisterPlaceholder(
        GuildPlugin.INSTANCE.getPlugin(), this.hologramLivesPlaceholder());
    HologramsAPI.unregisterPlaceholder(
        GuildPlugin.INSTANCE.getPlugin(), this.hologramHealthPlaceholder());
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  public void enlarge() {
    this.regionSize++;
    this.region = this.createRegion();
  }

  public void addWar(GuildWar war) {
    this.addWar(GuildWarWrapper.from(war));
  }

  public void addWar(GuildWarWrapper war) {
    this.wars.add(war);
    this.warTimeStamps.put(war.getEnemy(), war.getTimeStamp());
  }

  public long timeTillCanStartWar(Guild enemy) {
    return GuildConstants.WAR_COOLDOWN_TIME
        - (System.currentTimeMillis() - this.warTimeStamps.getOrDefault(enemy.getTag(), 0L));
  }

  public boolean canStartWarAgainst(Guild enemy) {
    return timeTillCanStartWar(enemy) < 0;
  }

  public void removeWar(Guild enemy) {
    this.wars.removeIf(it -> it.getEnemy().equals(enemy.getTag()));
  }

  public boolean isInWar(Guild guild) {
    return this.wars.stream().anyMatch(it -> it.getEnemy().equals(guild.getTag()));
  }

  public List<GuildWar> wars() {
    this.wars.removeIf(GuildWarWrapper::hasExpired);
    this.warTimeStamps
        .keySet()
        .removeIf(
            it -> {
              Optional<Guild> guildOptional = GuildPlugin.INSTANCE.getGuildCache().get(it);

              if (!guildOptional.isPresent()) return true;

              return canStartWarAgainst(guildOptional.get());
            });

    return this.wars.stream()
        .map(it -> GuildWar.from(it, false))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public void resetLastTntExplode() {
    this.lastTntExplode = System.currentTimeMillis();
  }

  public void addAction(GuildActionWrapper wrapper) {
    this.actions.add(wrapper);
  }

  public void addAction(GuildAction action) {
    this.addAction(
        new GuildActionWrapper(
            action.getType(), action.getPlayer().getUuid(), action.getGuild().getTag()));
  }

  public void addAction(GuildWarActionWrapper wrapper) {
    this.warActions.add(wrapper);
  }

  public void addAction(GuildWarAction action) {
    this.addAction(
        new GuildWarActionWrapper(
            action.getType(), action.getGuild().getTag(), action.getEnemy().getTag()));
  }

  public boolean hasTntExplodedRecently() {
    return System.currentTimeMillis() - this.lastTntExplode
        <= GuildConstants.NO_BUILD_TIME_AFTER_TNT_EXPLODE;
  }

  public long timeTillTnTExplosionEviction() {
    return GuildConstants.NO_BUILD_TIME_AFTER_TNT_EXPLODE
        - (System.currentTimeMillis() - this.lastTntExplode);
  }

  public void addToInvited(GuildPlayer guildPlayer) {
    this.memberInvites.put(guildPlayer.getUuid(), Boolean.TRUE);

    if (guildPlayer.isOnThisServer()) {
      guildPlayer.sendMessage(
          Text.colored(
              "&aZostałeś zaproszony do gildii &8[&7"
                  + this.getTag()
                  + "&8]&a. &aWpisz &7/g dolacz "
                  + this.getTag()
                  + " &aaby dołączyć&8"));
    }
  }

  public boolean isInvited(GuildPlayer guildPlayer) {
    return this.memberInvites.getIfPresent(guildPlayer.getUuid()) != null;
  }

  public void renew() {
    this.lastRenewTime = System.currentTimeMillis();
  }

  @Override
  public boolean hasExpired() {
    return this.lastRenewTime + GuildConstants.GUILD_RENEW_TIME < System.currentTimeMillis();
  }

  public void spawnHeartRoom(Location location) {
    SchematicUtil.pasteSchematic(
        GuildConstants.CENTER_SCHEMATIC,
        new Location(
            location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ()),
        false);
  }

  public int destroyHeart() {
    this.health--;

    return this.health;
  }

  public int conquer() {
    this.lives--;
    this.health = GuildConstants.MAX_HEALTH;
    this.lastConquer = System.currentTimeMillis();

    return this.lives;
  }

  public boolean canBeConquered() {
    return timeTillCanBeConquered() < 0;
  }

  public long timeTillCanBeConquered() {
    return GuildConstants.GUILD_CONQUER_DELAY - (System.currentTimeMillis() - lastConquer);
  }

  public boolean hasCreationProtection() {
    // System.currentTimeMillis() < this.getCreationProtectionExpireTime()
    return false;
  }

  public long creationProtectionExpireTime() {
    return createdOn + GuildConstants.CREATION_PROTECTION_TIME;
  }

  public Set<Guild> allies() {
    if (!GuildPlugin.INSTANCE.getGuildSettingsCache().get().isEnabledAllies()) {
      return Collections.emptySet();
    }

    return this.allies.stream()
        .map(it -> GuildPlugin.INSTANCE.getGuildCache().getByTag(it))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toSet());
  }

  public void toggleFriendlyFire() {
    this.friendlyFire = !this.friendlyFire;
  }

  public void addMember(GuildPlayer guildPlayer) {
    this.members.put(guildPlayer.getUuid(), GuildRole.NEW);
    guildPlayer.setGuild(this);
  }

  public void removeMember(GuildPlayer guildPlayer) {
    this.members.remove(guildPlayer.getUuid());
    guildPlayer.setGuild(null);
  }

  public int warsSize() {
    return this.wars.size();
  }

  public boolean hasMember(UUID uuid) {
    return this.members.containsKey(uuid);
  }

  public boolean hasMember(Player player) {
    return hasMember(player.getUniqueId());
  }

  public boolean hasMember(GuildPlayer guildPlayer) {
    return hasMember(guildPlayer.getUuid());
  }

  public boolean hasAlly(GuildPlayer guildPlayer) {
    return this.allies().stream().anyMatch(it -> it.hasMember(guildPlayer));
  }

  public boolean hasPermission(GuildPlayer guildPlayer, GuildPermission.Permissions permission) {
    return permissions.hasPermission(getRole(guildPlayer), permission);
  }

  public boolean hasAlly(Guild guild) {
    return allies.contains(guild.getTag());
  }

  public GuildRole getRole(GuildPlayer guildPlayer) {
    return members.get(guildPlayer.getUuid());
  }

  public void broadcast(String message) {
    String formattedMessage = Text.colored(message);
    onlinePlayers().forEach(it -> it.sendMessage(formattedMessage));
  }

  public boolean canPlaceBlocks() {
    return true;
  }

  public void setBase(Location location) {
    this.base = location;
  }

  public int size() {
    return this.members.size();
  }

  public List<GuildPlayer> members() {
    return this.members.keySet().stream()
        .map(it -> GuildPlugin.INSTANCE.getPlayerCache().get(it))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .sorted(
            (o1, o2) -> {
              int compare = -(getRole(o1).getPower() - getRole(o2).getPower());
              if (compare == 0) compare = o1.getName().compareTo(o2.getName());
              return compare;
            })
        .collect(Collectors.toList());
  }

  public List<GuildPlayer> onlineMembers() {
    return this.onlinePlayers().parallelStream()
        .map(it -> GuildPlugin.INSTANCE.getPlayerCache().get(it))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  public int kills() {
    return this.statistics.kills();
  }

  public int deaths() {
    return this.statistics.deaths();
  }

  public int killStreak() {
    return this.statistics.killStreak();
  }

  public void removeRectangle(GuildPlayer guildPlayer) {
    MessagedRectangle rectangle = this.region.toEnemyRectangle();
    CustomPayload payload = CuboidHelper.removeSomeCuboids(rectangle);

    CuboidHelper.send(guildPlayer, payload);
  }

  public void updateRectangle(GuildPlayer guildPlayer) {
    MessagedRectangle rectangle = this.region.toEnemyRectangle();
    CustomPayload payload =
        this.hasMember(guildPlayer)
            ? CuboidHelper.removeSomeCuboids(rectangle)
            : CuboidHelper.addSomeCuboids(rectangle);
    CuboidHelper.send(guildPlayer, payload);
  }

  public GuildPlayer leader() {
    return GuildPlugin.INSTANCE
        .getPlayerCache()
        .get(
            this.members.entrySet().stream()
                .filter(it -> it.getValue() == GuildRole.LEADER)
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null))
        .orElseThrow(() -> new NullPointerException("leader is null"));
  }

  public List<Player> onlinePlayers() {
    return this.members.keySet().stream()
        .map(Bukkit::getPlayer)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  public int centerX() {
    return this.center.getBlockX();
  }

  public int centerZ() {
    return this.center.getBlockZ();
  }

  public boolean isIn(Location location) {
    return RegionUtil.isIn(location, this.region);
  }

  public boolean isInHeartRoom(Location location) {
    return RegionUtil.isIn(location, this.heartRoomRegion);
  }

  public boolean isInHeartRoomIgnoreY(Location location) {
    return RegionUtil.isInIgnoreY(location, this.heartRoomRegion);
  }

  public SimpleRegion createHeartRoomRegion() {
    return new SimpleRegion(
        this.centerX() - 3, this.centerX() + 3, 8, 14, this.centerZ() - 3, this.centerZ() + 3);
  }

  public GuildRegion createRegion() {
    return new GuildRegion(
        this.centerX() - this.regionSize,
        this.centerX() + this.regionSize,
        this.centerZ() - this.regionSize,
        this.centerZ() + this.regionSize);
  }

  public String hologramLivesPlaceholder() {
    return "%lives_".concat(this.tag).concat("%");
  }

  public String hologramHealthPlaceholder() {
    return "%health_".concat(this.tag).concat("%");
  }

  public Location centerBlockLocation() {
    return new Location(
        this.center.getWorld(),
        this.center.getBlockX(),
        this.center.getBlockY(),
        this.center.getBlockZ());
  }

  public Location hologramLocation() {
    return this.center.clone().add(0, 2, 0);
  }

  public Location heartBlockLocation() {
    return this.centerBlockLocation().subtract(0, 1, 0);
  }

  public Block heartBlock() {
    return this.heartBlockLocation().getBlock();
  }

  public void deleteHologram() {
    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .run(
            () -> {
              Location hologramLocation = hologramLocation();
              HologramsAPI.getHolograms(GuildPlugin.INSTANCE.getPlugin()).stream()
                  .filter(it -> it.getLocation().equals(hologramLocation))
                  .forEach(it -> it.delete());
            });
  }

  public void createHologram() {
    Collection<String> placeholders =
        HologramsAPI.getRegisteredPlaceholders(GuildPlugin.INSTANCE.getPlugin());

    String healthPlaceholder = this.hologramHealthPlaceholder();
    String livesPlaceholder = this.hologramLivesPlaceholder();

    if (!placeholders.contains(healthPlaceholder)) {
      HologramsAPI.registerPlaceholder(
          GuildPlugin.INSTANCE.getPlugin(),
          healthPlaceholder,
          1.0,
          () -> {
            assert 1 == 1;
            return Text.colored(getHealth() + "/" + GuildConstants.MAX_HEALTH + " &7HP");
          });
    }

    if (!placeholders.contains(livesPlaceholder)) {
      HologramsAPI.registerPlaceholder(
          GuildPlugin.INSTANCE.getPlugin(),
          livesPlaceholder,
          1.0,
          () -> {
            assert 1 == 1;
            return Text.colored(getLives() + " &c" + StyleUtil.getHeartIcon());
          });
    }

    Location hologramLocation = this.hologramLocation();

    CorePlugin.INSTANCE
        .getTaskDispatcher()
        .run(
            () -> {
              HologramsAPI.getHolograms(GuildPlugin.INSTANCE.getPlugin()).stream()
                  .filter(it -> it.getLocation().equals(hologramLocation))
                  .findFirst()
                  .orElseGet(
                      () -> {
                        Hologram hologram =
                            HologramsAPI.createHologram(
                                GuildPlugin.INSTANCE.getPlugin(), hologramLocation);

                        hologram.setAllowPlaceholders(true);
                        hologram.appendItemLine(new ItemBuilder(Material.NETHER_STAR).build());
                        hologram.appendTextLine(Text.colored("&4&lSERCE GILDII"));
                        hologram.appendTextLine(healthPlaceholder);
                        hologram.appendTextLine(livesPlaceholder);

                        return hologram;
                      });
            });
  }

  public void increaseRank(int rank) {
    this.statistics.increaseRank(rank);
  }

  public void decreaseRank(int rank) {
    this.statistics.decreaseRank(rank);
  }

  public void increaseKills(GuildPlayer killer, GuildPlayer victim, int eloChange) {
    this.statistics.increaseKills(killer, victim, eloChange);
    this.increaseRank(eloChange);
  }

  public void increaseDeaths(GuildPlayer killer, GuildPlayer victim, int eloChange) {
    this.statistics.increaseDeaths(killer, victim, eloChange);
    this.decreaseRank(eloChange);
  }

  public int rank() {
    return this.statistics.getRank();
  }

  public GuildPermission getPermission(GuildRole role) {
    return this.permissions.getPermission(role);
  }

  public boolean canManage(GuildPlayer guildPlayer, GuildPlayer otherPlayer) {
    return this.canManage(guildPlayer, getRole(otherPlayer));
  }

  public boolean canManage(GuildPlayer guildPlayer, GuildRole role) {
    return getRole(guildPlayer).canManage(role);
  }

  @Override
  public int hashCode() {
    return this.tag.hashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == null) return false;
    if (!(other instanceof Guild)) return false;
    Guild otherGuild = (Guild) other;

    return otherGuild.getTag().equals(tag);
  }

  public void setRole(GuildPlayer guildPlayer, GuildRole role) {
    if (!this.members.containsKey(guildPlayer.getUuid())) return;

    this.members.put(guildPlayer.getUuid(), role);
  }
}
