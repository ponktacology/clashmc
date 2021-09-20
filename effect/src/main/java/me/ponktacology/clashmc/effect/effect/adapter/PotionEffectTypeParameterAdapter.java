package me.ponktacology.clashmc.effect.effect.adapter;

import com.google.common.base.Strings;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffectType;



import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class PotionEffectTypeParameterAdapter implements BladeProvider<PotionEffectType> {

  
  @Override
  public PotionEffectType provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;
    input = input.trim();

    String finalInput = input;
    return Optional.ofNullable(PotionEffectType.getByName(input))
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono typu efektu "
                        + ChatColor.YELLOW
                        + finalInput
                        + ChatColor.RED
                        + "."));
  }


  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return Arrays.stream(PotionEffectType.values())
        .map(PotionEffectType::getName)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
