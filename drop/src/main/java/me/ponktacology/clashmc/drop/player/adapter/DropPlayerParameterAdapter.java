package me.ponktacology.clashmc.drop.player.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.HumanEntity;



import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DropPlayerParameterAdapter implements BladeProvider<DropPlayer> {


  private final DropPlayerCache playerCache;

  
  @Override
  public DropPlayer provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;

    return this.playerCache
        .get(input)
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono gracza "
                        + ChatColor.YELLOW
                        + input
                        + ChatColor.RED
                        + "."));
  }


  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return Bukkit.getServer().getOnlinePlayers().stream()
        .map(HumanEntity::getName)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
