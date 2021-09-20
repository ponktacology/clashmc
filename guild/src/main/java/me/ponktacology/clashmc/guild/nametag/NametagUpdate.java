package me.ponktacology.clashmc.guild.nametag;

import org.bukkit.entity.Player;


final class NametagUpdate {
    private final String toRefresh;
    private String refreshFor;

    public NametagUpdate( Player toRefresh) {
        this.toRefresh=toRefresh.getName();
    }

    public NametagUpdate( Player toRefresh,  Player refreshFor) {
        this.toRefresh=toRefresh.getName();
        this.refreshFor=refreshFor.getName();
    }

    public String getToRefresh() {
        return this.toRefresh;
    }

    public String getRefreshFor() {
        return this.refreshFor;
    }
}

