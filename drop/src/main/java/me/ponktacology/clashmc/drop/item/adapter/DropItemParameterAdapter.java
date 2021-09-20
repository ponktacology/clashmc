package me.ponktacology.clashmc.drop.item.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
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
public class DropItemParameterAdapter implements BladeProvider<DropItem> {

  
  private final DropItemCache dropItemCache;

  
  @Override
  public DropItem provide(
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

    return this.dropItemCache
        .get(input)
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono przedmiotu w dropie "
                        + ChatColor.YELLOW
                        + finalInput
                        + ChatColor.RED
                        + "."));
  }

  
  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return this.dropItemCache.values().stream()
        .map(DropItem::getName)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
