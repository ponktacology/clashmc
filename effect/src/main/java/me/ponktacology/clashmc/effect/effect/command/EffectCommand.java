package me.ponktacology.clashmc.effect.effect.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.util.TimeUtil;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.effect.EffectPermissions;
import me.ponktacology.clashmc.effect.effect.Effect;
import me.ponktacology.clashmc.effect.effect.cache.EffectCache;
import me.ponktacology.clashmc.effect.effect.factory.EffectFactory;
import me.ponktacology.clashmc.effect.effect.menu.EffectMenu;
import me.ponktacology.clashmc.effect.effect.updater.EffectUpdater;
import me.vaperion.blade.command.annotation.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class EffectCommand {


  private final EffectCache effectCache;

  private final EffectFactory effectFactory;

  private final EffectUpdater effectUpdater;

  @Command(value = "efekty", description = "Wyświetla menu efektów")
  public void execute( @Sender Player sender) {
    new EffectMenu().openMenu(sender);
  }

  @Command(value = "effect create", description = "Tworzy nowy efekt", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void create( @Sender Player sender,  @Name("name") String name) {
    if (this.effectCache.get(name).isPresent()) {
      sender.sendMessage(Text.colored("&cTaki efekt już istnieję."));
      return;
    }

    Effect effect = this.effectFactory.create(name);

    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie utworzono nowy efekt."));
  }

  @Command(value = "effect remove", description = "Usuwa efekt", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void remove( @Sender Player sender, @Name("effect") Effect effect) {
    this.effectUpdater.remove(effect);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto efekt."));
  }

  @Command(value = "effect icon", description = "Ustawia ikonę efektu", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void icon( @Sender Player sender,  @Name("name") Effect effect) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand == null || itemInHand.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz trzymać jakiś przedmiot w ręce."));
      return;
    }

    effect.setIcon(itemInHand.clone());
    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono ikone efektu."));
  }

  @Command(value = "effect effect", description = "Ustawia typ efektu", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void type(
           @Sender Player sender,  @Name("name") Effect effect, @Name("type") PotionEffectType type,  @Name("duration") Time duration, @Name("amplifier") int amplifier) {
    effect.setEffect(new PotionEffect(type, TimeUtil.convertTimeToTicks(duration.getTimeStamp(), TimeUnit.MILLISECONDS), amplifier, true, true));
    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono efekt efektu."));
  }

  @Command(value = "effect displayname", description = "Ustawia wygląd nazwy efektu", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void displayName(
           @Sender Player sender,
           @Name("effect") Effect effect,
          @Name("displayName") @Combined String displayName) {
    effect.setDisplayName(displayName);
    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono wygląd nazwy efektu."));
  }

  @Command(
      value = "effect price",
      description = "Ustawia cene efektu w blokach emeraldów",
      async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void price(
           @Sender Player sender,  @Name("effect") Effect effect, @Name("price") int price) {
    effect.setPrice(price);
    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono odstęp pomiędzy użyciami efektu."));
  }

  @Command(value = "effect index", description = "Ustawa index efektu", async = true)
  @Permission(EffectPermissions.EFFECT_MANAGE)
  public void index(
           @Sender Player sender,  @Name("effect") Effect effect, @Name("index") int index) {
    effect.setIndex(index);
    this.effectUpdater.update(effect);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono index efektu."));
  }
}
