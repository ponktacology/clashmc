package me.ponktacology.clashmc.effect.effect.factory;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.factory.Factory;
import me.ponktacology.clashmc.api.service.data.DataService;
import me.ponktacology.clashmc.effect.effect.Effect;


import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EffectFactory implements Factory<Effect> {


  private final DataService dataService;


  public Effect create(String name) {
    return new Effect(name);
  }

  public Optional<Effect> load(String name) {
    return this.dataService.load(name, Effect.class);
  }


  public List<Effect> loadAll() {
    return this.dataService.loadAll(Effect.class);
  }

  public void delete(Effect effect) {
    this.dataService.delete(effect);
  }
}
