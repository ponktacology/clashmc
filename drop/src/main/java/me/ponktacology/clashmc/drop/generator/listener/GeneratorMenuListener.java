package me.ponktacology.clashmc.drop.generator.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.drop.generator.Generator;
import me.ponktacology.clashmc.drop.generator.cache.GeneratorCache;
import me.ponktacology.clashmc.drop.menu.DropMenuFactory;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;


import java.util.Optional;

@RequiredArgsConstructor
public class GeneratorMenuListener implements Listener {


    private final GeneratorCache generatorCache;

    private final DropPlayerCache playerCache;

    private final DropMenuFactory menuFactory;

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteractEvent( PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK || !player.isSneaking()) {
            return;
        }

        Block block = event.getClickedBlock();

        if (block == null || block.getType() != Material.STONE) {
            return;
        }

        Optional<Generator> generatorOptional = this.generatorCache.get(block.getLocation());

        if (!generatorOptional.isPresent()) {
            return;
        }

        Generator generator =generatorOptional.get();
        DropPlayer dropPlayer = this.playerCache.getOrKick(player);

        this.menuFactory.getGeneratorMenu(generator, dropPlayer).openMenu(player);
    }
}
