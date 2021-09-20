package me.ponktacology.clashmc.farmer.player.cache;

import me.ponktacology.clashmc.core.player.cache.BukkitPlayerCache;
import me.ponktacology.clashmc.farmer.player.FarmerPlayer;
import me.ponktacology.clashmc.farmer.player.factory.FarmerPlayerFactory;

public class FarmerPlayerCache extends BukkitPlayerCache<FarmerPlayer> {

    public FarmerPlayerCache(FarmerPlayerFactory playerFactory) {
        super(playerFactory);
    }
}
