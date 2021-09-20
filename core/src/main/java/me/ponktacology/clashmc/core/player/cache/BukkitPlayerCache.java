package me.ponktacology.clashmc.core.player.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import me.ponktacology.clashmc.api.player.PlayerWrapper;
import me.ponktacology.clashmc.api.player.cache.PlayerCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.exception.PlayerNotFoundException;
import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class BukkitPlayerCache<P extends BukkitPlayerWrapper> extends PlayerCache<P> {

  private static final Cache<String, UUID> nameToUuidCache =
      Caffeine.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();
  private static final Cache<UUID, String> uuidToNameCache =
      Caffeine.newBuilder().expireAfterAccess(12, TimeUnit.HOURS).build();

  protected final BukkitPlayerFactory<P> playerFactory;

  @Override
  public void add(P player) {
    super.add(player);
  }

  public Optional<P> remove(Player player) {
    return super.remove(player.getUniqueId());
  }

  /**
   * Lookups for cached P object matching key, if not found then player is kicked
   *
   * @param player whose uuid is used as a key
   * @return looked up player
   */
  @SneakyThrows
  public P getOrKick(Player player) {
    return this.get(player).orElseThrow(() -> new PlayerNotFoundException(player));
  }

  /**
   * Lookups for cached P object matching key, if not found then player is kicked
   *
   * @param player whose uuid is used as a key
   * @return looked up player
   */
  @SneakyThrows
  public P getOrKick(BukkitPlayerWrapper player) {
    return this.get(player).orElseThrow(() -> new PlayerNotFoundException(player.getPlayer()));
  }

  /**
   * Lookups for cached P object matching key
   *
   * @param player whose uuid is used as a key
   * @return optional of looked up player, empty if not found
   */
  public Optional<P> get(Player player) {
    return super.get(player.getUniqueId());
  }

  /**
   * Lookups for cached P object matching key
   *
   * @param player whose uuid is used as a key
   * @return optional of looked up player, empty if not found
   */
  public Optional<P> get(BukkitPlayerWrapper player) {
    return super.get(player.getUuid());
  }

  /**
   * Lookups in cache and database for P object matching key
   *
   * @param name which is used as a kye
   * @return optional of looked up player, empty if not found
   */
  @Override
  public Optional<P> get(String name) {
    Optional<UUID> uuidOptional = this.getUuid(name);

    if (!uuidOptional.isPresent()) {
      return Optional.empty();
    }

    return this.get(uuidOptional.get());
  }

  @Override
  public Optional<P> get(UUID uuid) {
    Optional<P> playerOptional = super.get(uuid);

    if (playerOptional.isPresent()) {
      return playerOptional;
    }

    return this.playerFactory.load(uuid);
  }

  public Optional<UUID> getUuid(String name) {
    UUID uuid = nameToUuidCache.getIfPresent(name.toUpperCase(Locale.ROOT));

    if (uuid != null) {
      return Optional.of(uuid);
    }

    Optional<String> uuidOptional =
        CorePlugin.INSTANCE.getNetworkService().get("uuid-cache", name.toUpperCase(Locale.ROOT));

    if (uuidOptional.isPresent()) {
      uuid = UUID.fromString(uuidOptional.get());
      this.cache(name, uuid);

      return Optional.of(uuid);
    }

    Optional<P> playerOptional = this.playerFactory.load(name);

    return playerOptional.map(PlayerWrapper::getUuid);
  }

  public Optional<String> getName(UUID uuid) {
    String name = uuidToNameCache.getIfPresent(uuid);

    if (name != null) {
      return Optional.of(name);
    }

    Optional<String> nameOptional =
        CorePlugin.INSTANCE.getNetworkService().get("name-cache", uuid.toString());

    if (nameOptional.isPresent()) {
      name = nameOptional.get();
      this.cache(name, uuid);

      return nameOptional;
    }

    Optional<P> playerOptional = this.playerFactory.load(uuid);

    return playerOptional.map(PlayerWrapper::getName);
  }

  public boolean isOnline(String name) {
    if (Bukkit.getPlayer(name) != null) return true;

    Optional<String> onlineOptional =
        CorePlugin.INSTANCE.getNetworkService().get("player-online", name);

    return onlineOptional.filter(Boolean::parseBoolean).isPresent();
  }

  public boolean isOnline(PlayerWrapper abstractPlayer) {
    return this.isOnline(abstractPlayer.getNameLowerCase());
  }

  public boolean isOnlineNotInAuthOrLobby(PlayerWrapper abstractPlayer) {
    return this.isOnlineNotInAuthOrLobby(abstractPlayer.getNameLowerCase());
  }

  public boolean isOnlineNotInAuthOrLobby(String name) {
    if (!this.isOnline(name)) {
      return false;
    }

    Optional<String> currentServerOptional = getCurrentServer(name);

    if (!currentServerOptional.isPresent()) {
      return false;
    }

    String currentServer = currentServerOptional.get();

    return !("lobby".equalsIgnoreCase(currentServer) || "auth".equalsIgnoreCase(currentServer));
  }

  public Optional<String> getCurrentServer(String name) {
    return CorePlugin.INSTANCE.getNetworkService().get("player-server", name);
  }

  public Optional<String> getCurrentServer(BukkitPlayerWrapper player) {
    return this.getCurrentServer(player.getNameLowerCase());
  }

  protected void updateCurrentServer(PlayerWrapper player) {
    CorePlugin.INSTANCE
        .getNetworkService()
        .set(
            "player-server",
            player.getNameLowerCase(),
            CorePlugin.INSTANCE.getConfiguration().getServerName());
  }

  protected void cache(P player) {
    this.cache(player.getName(), player.getUuid());
  }

  private void cache(String name, UUID uuid) {
    final String uuidString = uuid.toString();

    uuidToNameCache.put(uuid, name);
    nameToUuidCache.put(name.toUpperCase(Locale.ROOT), uuid);

    CorePlugin.INSTANCE.getNetworkService().set("name-cache", uuidString, name);
    CorePlugin.INSTANCE.getNetworkService().set("uuid-cache", name, uuidString);
  }
}
