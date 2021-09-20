package me.ponktacology.clashmc.guild.player.home.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.guild.player.home.PlayerHome;


public class PlayerHomeCache extends IgnoreCaseKeyValueCache<PlayerHome> {

    public void add( PlayerHome home) {
        super.add(home.getName(), home);
    }

}
