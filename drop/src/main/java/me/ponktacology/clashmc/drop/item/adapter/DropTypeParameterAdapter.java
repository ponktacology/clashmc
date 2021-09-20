package me.ponktacology.clashmc.drop.item.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.drop.item.DropType;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.entity.Player;



import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DropTypeParameterAdapter implements BladeProvider<DropType> {
  
  @Override
  public DropType provide(
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
    try {
      return DropType.valueOf(finalInput.toUpperCase());
    } catch (Exception e) {
      throw new BladeExitMessage(
          Text.colored("&cNie znaleziono typu dropu &e" + finalInput + "&c."));
    }
  }


  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return Arrays.stream(DropType.values())
        .map(DropType::toString)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
