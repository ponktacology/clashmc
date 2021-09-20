package me.ponktacology.clashmc.sector.player.teleport.cache;

import com.google.common.collect.Sets;
import lombok.ToString;
import me.ponktacology.clashmc.api.cache.keyvalue.KeyValueCache;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.sector.player.teleport.request.PlayerTeleportRequest;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ToString
public class TeleportCache extends KeyValueCache<UUID, Set<PlayerTeleportRequest>> {

  public void add(PlayerTeleportRequest request) {
    UUID receiver = request.getReceiver().getUuid();
    Set<PlayerTeleportRequest> requests = super.get(receiver).orElse(Sets.newHashSet());
    requests.add(request);
    super.add(receiver, requests);
  }

  public void remove(BukkitPlayerWrapper receiver, BukkitPlayerWrapper sender) {
    get(receiver).removeIf(it -> it.getSender().getUuid().equals(sender.getUuid()));
  }

  public void removeAll(BukkitPlayerWrapper player) {
    super.remove(player.getUuid());
  }

  public void removeAll(Player player) {
    super.remove(player.getUniqueId());
  }

  public Set<PlayerTeleportRequest> get(BukkitPlayerWrapper receiver) {
    return super.get(receiver.getUuid()).orElse(Collections.emptySet());
  }

  public Optional<PlayerTeleportRequest> get(
      BukkitPlayerWrapper receiver, BukkitPlayerWrapper sender) {
    Set<PlayerTeleportRequest> requests =
        super.get(receiver.getUuid()).orElse(Collections.emptySet());

    return requests.stream()
        .filter(it -> it.getSender().getUuid().equals(sender.getUuid()))
        .sorted((o1, o2) -> (int) -(o1.getTimeStamp() - o2.getTimeStamp()))
        .findFirst();
  }
}
