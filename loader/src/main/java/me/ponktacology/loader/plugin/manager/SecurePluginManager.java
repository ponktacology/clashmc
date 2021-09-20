package me.ponktacology.loader.plugin.manager;

import me.ponktacology.loader.plugin.SecurePlugin;



import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class SecurePluginManager {

    private final Map<String, SecurePlugin> cache = new HashMap<>();

    public void addPlugin( SecurePlugin securePlugin) {
        cache.put(securePlugin.getName(), securePlugin);
    }

    
    public SecurePlugin getPlugin(String name) {
        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        for (SecurePlugin plugin : cache.values()) {
            if (plugin.getName().equalsIgnoreCase(name)) return plugin;
        }


        return null;
    }


    public Set<SecurePlugin> getPlugins() {
        return new HashSet<>(cache.values());
    }
}
