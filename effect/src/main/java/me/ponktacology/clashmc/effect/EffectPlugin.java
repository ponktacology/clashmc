package me.ponktacology.clashmc.effect;

import lombok.Getter;
import me.ponktacology.clashmc.api.service.network.packet.cache.PacketCache;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.effect.effect.Effect;
import me.ponktacology.clashmc.effect.effect.adapter.EffectParameterAdapter;
import me.ponktacology.clashmc.effect.effect.adapter.PotionEffectTypeParameterAdapter;
import me.ponktacology.clashmc.effect.effect.cache.EffectCache;
import me.ponktacology.clashmc.effect.effect.command.EffectCommand;
import me.ponktacology.clashmc.effect.effect.factory.EffectFactory;
import me.ponktacology.clashmc.effect.effect.updater.EffectUpdater;
import me.ponktacology.clashmc.effect.effect.updater.packet.PacketEffectRemove;
import me.ponktacology.clashmc.effect.effect.updater.packet.PacketEffectUpdate;
import me.ponktacology.loader.plugin.BukkitPlugin;
import me.vaperion.blade.Blade;
import me.vaperion.blade.command.container.impl.BukkitCommandContainer;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;

@Getter
public enum EffectPlugin implements BukkitPlugin {
  INSTANCE;

  private final EffectFactory effectFactory =
      new EffectFactory(CorePlugin.INSTANCE.getDataService());
  private final EffectCache effectCache = new EffectCache();
  private final EffectUpdater effectUpdater =
      new EffectUpdater(
          this.effectFactory, this.effectCache, CorePlugin.INSTANCE.getNetworkService());

  @Override
  public void enable(JavaPlugin plugin) {
    this.registerEffects();

    Blade corePluginBlade = CorePlugin.INSTANCE.getBlade();
    Blade.of()
        .fallbackPrefix("effects")
        .containerCreator(BukkitCommandContainer.CREATOR)
        .bindings(corePluginBlade.getBindings())
        .asyncExecutor(corePluginBlade.getAsyncExecutor())
        .customProviderMap(corePluginBlade.getCustomProviderMap())
        .tabCompleter(corePluginBlade.getTabCompleter())
        .helpGenerator(corePluginBlade.getHelpGenerator())
        .bind(PotionEffectType.class, new PotionEffectTypeParameterAdapter())
        .bind(Effect.class, new EffectParameterAdapter(this.effectCache))
        .build()
        .register(new EffectCommand(this.effectCache, this.effectFactory, this.effectUpdater));
  }

  @Override
  public void disable() {}

  private void registerEffects() {
    PacketCache packetCache = CorePlugin.INSTANCE.getPacketCache();

    packetCache.add(PacketEffectUpdate.class);
    packetCache.add(PacketEffectRemove.class);

    CorePlugin.INSTANCE.getNetworkService().subscribe(this.effectUpdater);

    this.effectCache.addAll(this.effectFactory.loadAll());
  }
}
