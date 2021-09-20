package me.ponktacology.clashmc.sector.proxy;

import net.md_5.bungee.api.plugin.Plugin;

public class SectorProxyBootstrap extends Plugin {

    @Override
    public void onEnable() {
        SectorProxy.INSTANCE.enable(this);
    }

    @Override
    public void onDisable() {
        SectorProxy.INSTANCE.disable();
    }
}
