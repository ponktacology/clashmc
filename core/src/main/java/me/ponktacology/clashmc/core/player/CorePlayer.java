package me.ponktacology.clashmc.core.player;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.util.Console;
import me.ponktacology.clashmc.api.util.Timer;
import me.ponktacology.clashmc.core.CoreConstants;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.log.chat.ChatLog;
import me.ponktacology.clashmc.core.log.command.CommandLog;
import me.ponktacology.clashmc.core.punishment.Punishment;
import me.ponktacology.clashmc.core.punishment.PunishmentType;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity(collection = "players", database = "core")
public final class CorePlayer extends BukkitPlayerWrapper {

  private final long joinTime = System.currentTimeMillis();
  private final Set<String> ips = Sets.newConcurrentHashSet();
  private final Set<UUID> ignoredPlayers = Sets.newConcurrentHashSet();
  private final List<Grant> grants = Lists.newCopyOnWriteArrayList();
  private final List<Punishment> punishments = Lists.newCopyOnWriteArrayList();
  private final List<CommandLog> commandLogs = Lists.newCopyOnWriteArrayList();
  private final List<ChatLog> chatLogs = Lists.newCopyOnWriteArrayList();

  private boolean vanished;
  private boolean god;
  private UUID replyPlayer;

  private final transient long loginTime = System.currentTimeMillis();
  private final transient Timer commandTimer = new Timer(1500L);
  private final transient Timer chatTimer = new Timer(3000L);
  private final transient Timer requestTimer = new Timer(CoreConstants.REQUEST_DELAY_TIME, 0L);

  private transient PermissionAttachment attachment;

  public CorePlayer(UUID uuid, String name) {
    super(uuid, name);
  }

  public void addIp(String ip) {
    this.ips.add(ip);
  }

  public void ignore(CorePlayer corePlayer) {
    this.ignoredPlayers.add(corePlayer.getUuid());
  }

  public void unIgnore(CorePlayer corePlayer) {
    this.ignoredPlayers.remove(corePlayer.getUuid());
  }

  public boolean isIgnored(CorePlayer corePlayer) {
    return this.ignoredPlayers.contains(corePlayer.getUuid());
  }

  public void addRank(Grant grant) {
    this.grants.add(grant);
    this.recalculatePermissions();
  }

  public void removeRank(Grant grant) {
    removeRank(
        grant.getRankName(), grant.getRemovedOn(), grant.getRemoveReason(), grant.getRemovedBy());
    this.recalculatePermissions();
  }

  public void removeRank(String rankName, long removedOn, String removeReason, UUID removedBy) {
    this.grants.stream()
        .filter(it -> it.getRankName().equals(rankName) && !it.hasExpired())
        .forEach(
            it -> {
              it.setRemoved(true);
              it.setRemovedOn(removedOn);
              it.setRemovedBy(removedBy);
              it.setRemoveReason(removeReason);
            });
  }

  public void addPunishment(Punishment punishment) {
    this.punishments.add(punishment);
  }

  public void removePunishment(PunishmentType type, String removeReason, UUID removedBy) {
    this.punishments.stream()
        .filter(it -> it.getType().equals(type) && !it.hasExpired())
        .forEach(
            it -> {
              it.setRemoved(true);
              it.setRemovedOn(System.currentTimeMillis());
              it.setRemovedBy(removedBy);
              it.setRemoveReason(removeReason);
            });
  }

  public void addCommandLog(CommandLog log) {
    this.commandLogs.add(log);

    if (this.commandLogs.size() > CoreConstants.MAX_LOG_COUNT) {
      this.commandLogs.remove(0);
    }
  }

  public void addChatLog(ChatLog log) {
    this.chatLogs.add(log);

    if (this.chatLogs.size() > CoreConstants.MAX_LOG_COUNT) {
      this.chatLogs.remove(0);
    }
  }

  public Set<CorePlayer> getAlts() {
    Set<CorePlayer> alts = new LinkedHashSet<>();

    for (String ip : this.ips) {
      Set<CorePlayer> players =
          CorePlugin.INSTANCE.getDataService().loadIfFieldContains("ips", ip, CorePlayer.class);

      for (CorePlayer corePlayer : players) {
        if (!this.equals(corePlayer)) {
          alts.add(corePlayer);
        }
      }
    }

    return alts;
  }

  public boolean isStaff() {
    return getMainRank().isStaff();
  }

  public Rank getMainRank() {
    return getRanks().stream()
        .max(Comparator.comparingInt(Rank::getPower))
        .orElse(CorePlugin.INSTANCE.getRankCache().defaultRank());
  }

  public List<Rank> getRanks() {
    List<Rank> ranks =
        this.grants.stream()
            .filter(it -> !it.hasExpired())
            .map(it -> CorePlugin.INSTANCE.getRankCache().get(it.getRankName()).orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toCollection(ArrayList::new));

    if (ranks.isEmpty()) {
      Rank defaultRank = CorePlugin.INSTANCE.getRankCache().defaultRank();

      addRank(
          new Grant(
              defaultRank.getName(),
              Console.UUID,
              "Podstawowa ranga",
              System.currentTimeMillis(),
              -1L));
      ranks.add(defaultRank);
    }

    return ranks;
  }

  public Optional<Punishment> getActivePunishment(PunishmentType type) {
    for (Punishment punishment : this.punishments) {
      if (!punishment.getType().equals(type) || punishment.hasExpired()) continue;

      return Optional.of(punishment);
    }

    return Optional.empty();
  }

  public Optional<Grant> getActiveGrant(Rank rank) {
    for (Grant grant : this.grants) {
      if (!grant.getRankName().equals(rank.getName()) || grant.hasExpired()) continue;

      return Optional.of(grant);
    }

    return Optional.empty();
  }

  public String getFormattedName() {
    Rank rank = this.getMainRank();

    if (rank == null) {
      return this.getName();
    }

    String formattedName = Text.colored(rank.getColor() + this.getName());

    Optional.ofNullable(this.getPlayer())
        .ifPresent(
            it -> {
              it.setPlayerListName(formattedName);
            });

    return formattedName;
  }

  public boolean hasPermission(String permission) {
    PermissionAttachment attachment = this.getAttachment();

    if (attachment != null) {
      return attachment.getPermissions().getOrDefault(permission, true);
    }

    return getRanks().stream().anyMatch(it -> it.getPermissions().contains(permission));
  }

  public Optional<UUID> getReplyPlayer() {
    return Optional.ofNullable(this.replyPlayer);
  }

  public void recalculatePermissions() {
    Player player = this.getPlayer();

    if (player == null) {
      return;
    }

    for (PermissionAttachmentInfo attachmentInfo : player.getEffectivePermissions()) {
      if (attachmentInfo.getAttachment() == null
          || attachmentInfo.getAttachment().getPlugin() == null
          || !attachmentInfo.getAttachment().getPlugin().equals(CorePlugin.INSTANCE.getPlugin())) {
        continue;
      }

      attachmentInfo
          .getAttachment()
          .getPermissions()
          .forEach(
              (permission, value) -> {
                attachmentInfo.getAttachment().unsetPermission(permission);
              });
    }

    PermissionAttachment attachment = player.addAttachment(CorePlugin.INSTANCE.getPlugin());

    for (Rank rank : this.getRanks()) {
      for (String permission : rank.getPermissions()) {
        attachment.setPermission(permission, true);
      }
    }

    player.recalculatePermissions();
  }

  public void setupVanish() {
    Player player = this.getPlayer();

    if (player == null) return;

    boolean isStaff = this.isStaff();
    boolean isVanished = this.vanished;

    for (CorePlayer corePlayer : CorePlugin.INSTANCE.getPlayerCache().values()) {
      Player other = corePlayer.getPlayer();

      if (other == null) continue;

      if (isVanished) {
        if (!player.canSee(other)) player.showPlayer(other);

        if (corePlayer.isStaff()) {
          if (!other.canSee(player)) other.showPlayer(player);
        } else {
          if (other.canSee(player)) other.hidePlayer(player);
        }
      } else {
        if (other.canSee(player)) other.hidePlayer(player);
        if (!other.canSee(player)) other.showPlayer(player);
      }

      if (corePlayer.isVanished()) {
        if (!other.canSee(player)) other.showPlayer(player);

        if (isStaff) {
          if (!player.canSee(other))  player.showPlayer(other);
        } else {
          if (player.canSee(other))   player.hidePlayer(other);
        }
      } else {
        if (player.canSee(other))  player.hidePlayer(other);
        if (!player.canSee(other)) player.showPlayer(other);
      }
    }
  }

  public void toggleVanish() {
    this.vanished = !this.vanished;
    this.setupVanish();
  }

  public void toggleGod() {
    this.god = !this.god;
  }

  @Override
  public String toString() {
    return "CorePlayer{"
        + "joinTime="
        + joinTime
        + ", ips="
        + ips
        + ", ignoredPlayers="
        + ignoredPlayers
        + ", vanished="
        + vanished
        + ", mainRank="
        + getMainRank().getName()
        + ", god="
        + god
        + ", replyPlayer="
        + replyPlayer
        + ", loginTime="
        + loginTime
        + ", attachment="
        + attachment
        + '}';
  }
}
