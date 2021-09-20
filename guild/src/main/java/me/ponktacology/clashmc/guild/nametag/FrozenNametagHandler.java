package me.ponktacology.clashmc.guild.nametag;

import com.google.common.collect.Lists;
import com.google.common.primitives.Ints;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.guild.GuildPlugin;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class FrozenNametagHandler {

  private static final Map<String, Map<String, NametagInfo>> teamMap = new ConcurrentHashMap<>();
  private static final List<NametagInfo> registeredTeams =
      Collections.synchronizedList(Lists.newArrayList());
  private static int teamCreateIndex = 1;
  private static final List<NametagProvider> providers = new ArrayList<>();
  private static boolean nametagRestrictionEnabled = false;
  private static String nametagRestrictBypass = "";
  private static boolean initiated = false;
  private static boolean async = true;
  private static int updateInterval = 20;
  private static TaskDispatcher taskDispatcher;

  private FrozenNametagHandler() {}

  public static void init(TaskDispatcher taskDispatcher2) {
    taskDispatcher = taskDispatcher2;
    initiated = true;
    new NametagThread().start();
    GuildPlugin.INSTANCE
        .getPlugin()
        .getServer()
        .getPluginManager()
        .registerEvents(new NametagListener(taskDispatcher2), GuildPlugin.INSTANCE.getPlugin());
  }

  public static void registerProvider(NametagProvider newProvider) {
    providers.add(newProvider);
    providers.sort((a, b) -> Ints.compare(b.getWeight(), a.getWeight()));
  }

  public static void reloadPlayer(Player toRefresh) {
    NametagUpdate update = new NametagUpdate(toRefresh);
    if (async) {
      NametagThread.getPendingUpdates().put(update, true);
    } else {
      FrozenNametagHandler.applyUpdate(update);
    }
  }

  public static void reloadOthersFor(Player refreshFor) {
    for (Player toRefresh : GuildPlugin.INSTANCE.getPlugin().getServer().getOnlinePlayers()) {
      if (refreshFor == toRefresh) continue;
      FrozenNametagHandler.reloadPlayer(toRefresh, refreshFor);
    }
  }

  public static void reloadPlayer(Player toRefresh, Player refreshFor) {
    NametagUpdate update = new NametagUpdate(toRefresh, refreshFor);
    if (async) {
      NametagThread.getPendingUpdates().put(update, true);
    } else {
      FrozenNametagHandler.applyUpdate(update);
    }
  }

  protected static void applyUpdate(NametagUpdate nametagUpdate) {
    Player toRefreshPlayer =
        GuildPlugin.INSTANCE.getPlugin().getServer().getPlayerExact(nametagUpdate.getToRefresh());
    if (toRefreshPlayer == null) {
      return;
    }
    if (nametagUpdate.getRefreshFor() == null) {
      for (Player refreshFor : GuildPlugin.INSTANCE.getPlugin().getServer().getOnlinePlayers()) {
        FrozenNametagHandler.reloadPlayerInternal(toRefreshPlayer, refreshFor);
      }
    } else {
      Player refreshForPlayer =
          GuildPlugin.INSTANCE
              .getPlugin()
              .getServer()
              .getPlayerExact(nametagUpdate.getRefreshFor());
      if (refreshForPlayer != null) {
        FrozenNametagHandler.reloadPlayerInternal(toRefreshPlayer, refreshForPlayer);
      }
    }
  }

  protected static void reloadPlayerInternal(Player toRefresh, Player refreshFor) {
    if (refreshFor == null || toRefresh == null) return;
    NametagInfo provided = null;
    int providerIndex = 0;
    while (provided == null) {
      provided = providers.get(providerIndex++).fetchNametag(toRefresh, refreshFor);
    }
    Map<String, NametagInfo> teamInfoMap = new HashMap<>();
    if (teamMap.containsKey(refreshFor.getName())) {
      teamInfoMap = teamMap.get(refreshFor.getName());
    }

    NametagInfo finalProvided = provided;
    taskDispatcher.runAsync(
        () ->
            new ScoreboardTeamPacketMod(
                    finalProvided.getName(), Collections.singletonList(toRefresh.getName()), 3)
                .sendToPlayer(refreshFor));

    teamInfoMap.put(toRefresh.getName(), provided);
    teamMap.put(refreshFor.getName(), teamInfoMap);
  }

  protected static void initiatePlayer(Player player) {
    for (NametagInfo teamInfo : registeredTeams) {
      teamInfo.getTeamAddPacket().sendToPlayer(player);
    }
  }

  protected static NametagInfo getOrCreate(String prefix, String suffix) {
    for (NametagInfo teamInfo : registeredTeams) {
      if (!teamInfo.getPrefix().equals(prefix) || !teamInfo.getSuffix().equals(suffix)) continue;
      return teamInfo;
    }
    NametagInfo newTeam = new NametagInfo(String.valueOf(teamCreateIndex++), prefix, suffix);
    registeredTeams.add(newTeam);
    ScoreboardTeamPacketMod addPacket = newTeam.getTeamAddPacket();
    for (Player player : GuildPlugin.INSTANCE.getPlugin().getServer().getOnlinePlayers()) {
      addPacket.sendToPlayer(player);
    }
    return newTeam;
  }

  protected static Map<String, Map<String, NametagInfo>> getTeamMap() {
    return teamMap;
  }

  public static boolean isNametagRestrictionEnabled() {
    return nametagRestrictionEnabled;
  }

  public static void setNametagRestrictionEnabled(boolean nametagRestrictionEnabled) {
    FrozenNametagHandler.nametagRestrictionEnabled = nametagRestrictionEnabled;
  }

  public static String getNametagRestrictBypass() {
    return nametagRestrictBypass;
  }

  public static void setNametagRestrictBypass(String nametagRestrictBypass) {
    FrozenNametagHandler.nametagRestrictBypass = nametagRestrictBypass;
  }

  public static boolean isInitiated() {
    return initiated;
  }

  public static boolean isAsync() {
    return async;
  }

  public static void setAsync(boolean async) {
    FrozenNametagHandler.async = async;
  }

  public static int getUpdateInterval() {
    return updateInterval;
  }

  public static void setUpdateInterval(int updateInterval) {
    FrozenNametagHandler.updateInterval = updateInterval;
  }
}
