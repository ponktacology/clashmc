package me.ponktacology.clashmc.farmer.player.factory;

import me.ponktacology.clashmc.core.player.factory.BukkitPlayerFactory;
import me.ponktacology.clashmc.farmer.player.FarmerPlayer;

public class FarmerPlayerFactory extends BukkitPlayerFactory<FarmerPlayer> {
    public FarmerPlayerFactory() {
        super(FarmerPlayer.class, FarmerPlayer::new);
    }
}
