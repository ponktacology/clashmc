package me.ponktacology.clashmc.api.player.factory;

import lombok.SneakyThrows;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.player.PlayerWrapper;

import java.util.Optional;
import java.util.UUID;

public interface PlayerFactory<V extends PlayerWrapper> extends Factory<V> {

  @SneakyThrows
  default V create(UUID uuid, String name) {
    throw new InstantiationException("no constructor");
  }

  @SneakyThrows
  default Optional<V> load(UUID uuid) {
    throw new InstantiationException("no constructor");
  }

  @SneakyThrows
  default Optional<V> load(String name) {
    throw new InstantiationException("no constructor");
  }
}
