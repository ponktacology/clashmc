package me.ponktacology.clashmc.guild.player.grief.task;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.player.grief.PlacedBlockCache;


@RequiredArgsConstructor
public class AntiGriefTask implements Runnable {


  private final PlacedBlockCache blockCache;

  @Override
  public void run() {
    this.blockCache.removeIf(
        it -> {
          boolean expired = it.hasExpired();

          if (expired) {
            it.clear();
          }

          return expired;
        });
  }
}
