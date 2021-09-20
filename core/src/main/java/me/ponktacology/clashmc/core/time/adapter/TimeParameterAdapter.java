package me.ponktacology.clashmc.core.time.adapter;

import me.ponktacology.clashmc.core.time.Time;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.ChatColor;



public class TimeParameterAdapter implements BladeProvider<Time> {

  
  @Override
  public Time provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;

    try {
      return new Time(input);
    } catch (IllegalArgumentException e) {
      throw new BladeExitMessage(
          ChatColor.RED
              + "Zły format, przykład poprawnego: "
              + ChatColor.YELLOW
              + "1d2h3s"
              + ChatColor.RED
              + ".");
    }
  }
}
