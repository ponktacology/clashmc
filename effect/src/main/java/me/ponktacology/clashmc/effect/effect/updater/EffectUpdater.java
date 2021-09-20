package me.ponktacology.clashmc.effect.effect.updater;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.network.NetworkService;
import me.ponktacology.clashmc.api.service.network.packet.listener.PacketHandler;
import me.ponktacology.clashmc.api.updater.Updater;
import me.ponktacology.clashmc.effect.effect.Effect;
import me.ponktacology.clashmc.effect.effect.cache.EffectCache;
import me.ponktacology.clashmc.effect.effect.factory.EffectFactory;
import me.ponktacology.clashmc.effect.effect.updater.packet.PacketEffectRemove;
import me.ponktacology.clashmc.effect.effect.updater.packet.PacketEffectUpdate;


import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public class EffectUpdater implements Updater {

  private final EffectFactory effectFactory;
  private final EffectCache effectCache;
  private final NetworkService networkService;

  public void update( Effect effect) {
    effect.save();
    this.networkService.publish(new PacketEffectUpdate(effect.getName()));
  }

  
  public void remove( Effect effect) {
    this.effectFactory.delete(effect);
    this.networkService.publish(new PacketEffectRemove(effect.getName()));
  }

  @PacketHandler
  public void onUpdate( PacketEffectUpdate packet) {
    Optional<Effect> effectOptional = this.effectFactory.load(packet.getName());

    if (!effectOptional.isPresent()) {
      log.info(
          "Effect update received but effect not found in database effect= " + packet.getName());
      return;
    }

    Effect effect = effectOptional.get();

    this.effectCache.add(effect);
  }

  @PacketHandler
  public void onRemove( PacketEffectRemove packet) {
    this.effectCache.remove(packet.getName());
  }
}
