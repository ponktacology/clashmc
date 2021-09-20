package me.ponktacology.clashmc.guild.tnt;

import lombok.RequiredArgsConstructor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class TnTFixListener implements Listener {

  @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
  public void onEvent(EntityExplodeEvent event) {
    CompletableFuture.supplyAsync(
            () ->
                event.blockList().parallelStream()
                    .filter(it -> it.getType() == Material.TNT)
                    .collect(Collectors.toList()))
        .thenAccept(
            it ->
                it.forEach(
                    block ->
                        event
                            .getLocation()
                            .getWorld()
                            .spawnEntity(block.getLocation(), EntityType.PRIMED_TNT)));
  }
}
