package me.ponktacology.clashmc.effect.effect.cache;

import me.ponktacology.clashmc.api.cache.keyvalue.IgnoreCaseKeyValueCache;
import me.ponktacology.clashmc.effect.effect.Effect;


import java.util.List;

public class EffectCache extends IgnoreCaseKeyValueCache<Effect> {

  public void add( Effect effect) {
    this.add(effect.getName(), effect);
  }

  public void addAll( List<Effect> effects) {
    effects.forEach(this::add);
  }
}
