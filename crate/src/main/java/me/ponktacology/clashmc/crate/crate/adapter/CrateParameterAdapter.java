package me.ponktacology.clashmc.crate.crate.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;



import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CrateParameterAdapter implements BladeProvider<Crate> {

  
  private final CrateCache crateCache;

  
  @Override
  public Crate provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;
    input = input.trim();

    Player player = bladeContext.sender().parseAs(Player.class);

    if (player != null && (input.isEmpty() || input.equalsIgnoreCase("self"))) {
      input = bladeContext.sender().getName();
    }

    String finalInput = input;
    return this.crateCache
        .get(input)
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono skrzynki "
                        + ChatColor.YELLOW
                        + finalInput
                        + ChatColor.RED
                        + "."));
  }

  
  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return this.crateCache.values().stream()
        .map(Crate::getName)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
