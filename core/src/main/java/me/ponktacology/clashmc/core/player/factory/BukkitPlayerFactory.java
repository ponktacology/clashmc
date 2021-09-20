package me.ponktacology.clashmc.core.player.factory;

import com.mongodb.client.model.Filters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.player.factory.PlayerFactory;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.function.BiFunction;

@RequiredArgsConstructor
@Slf4j
public abstract class BukkitPlayerFactory<P extends BukkitPlayerWrapper>
    implements PlayerFactory<P> {

  private final Class<P> type;

  private final BiFunction<UUID, String, P> createFunction;

  public P create(UUID uuid, String name) {
    return this.createFunction.apply(uuid, name);
  }

  public P loadOrCreate(UUID uuid, String name) {
    Optional<P> playerOptional = this.load(uuid);

    P player =
        playerOptional.orElseGet(
            () -> {
              P player2 = this.create(uuid, name);
              player2.save();
              return player2;
            });
    return player;
  }

  @Override
  public Optional<P> load(UUID uuid) {
    Optional<P> player =
        CorePlugin.INSTANCE.getCacheNetworkService().load(uuid.toString(), this.type);

    if (player.isPresent()) {
      log.info("Loaded player from cache network service!");
      return player;
    }

    return CorePlugin.INSTANCE.getDataService().load(uuid.toString(), this.type);
  }

  @Override
  public Optional<P> load(String name) {
    return CorePlugin.INSTANCE
        .getDataService()
        .load(Filters.eq("nameLowerCase", name.toLowerCase(Locale.ROOT)), this.type);
  }
}
