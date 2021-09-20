package me.ponktacology.clashmc.farmer.farmer.listener;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.farmer.FarmerPlugin;
import me.ponktacology.clashmc.farmer.farmer.Farmer;
import me.ponktacology.clashmc.farmer.farmer.cache.FarmerCache;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;

@RequiredArgsConstructor
public class ForceCompleteFarmerListener implements Listener {

    private final FarmerCache farmerCache;

    @EventHandler
    public void onEvent(PluginDisableEvent event) {
        if(event.getPlugin().getName().equals(FarmerPlugin.INSTANCE.getPlugin().getName())) {
            this.farmerCache.values().forEach(Farmer::forceFinish);
        }
    }
}
