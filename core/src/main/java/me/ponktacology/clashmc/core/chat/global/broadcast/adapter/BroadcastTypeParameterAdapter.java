package me.ponktacology.clashmc.core.chat.global.broadcast.adapter;

import com.google.common.base.Strings;
import me.ponktacology.clashmc.core.chat.global.broadcast.type.BroadcastType;
import me.ponktacology.clashmc.core.util.Text;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.entity.Player;



import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class BroadcastTypeParameterAdapter implements BladeProvider<BroadcastType> {

  
  @Override
  public BroadcastType provide(
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
      return BroadcastType.valueOf(finalInput.toUpperCase());
    } catch (Exception e) {
      throw new BladeExitMessage(
          Text.colored("&cNie znaleziono typu ogłoszenia &e" + finalInput + "&c."));
    }
  }

  
  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return Arrays.stream(BroadcastType.values())
        .map(BroadcastType::toString)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
