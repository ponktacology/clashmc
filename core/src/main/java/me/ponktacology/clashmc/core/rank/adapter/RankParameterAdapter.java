package me.ponktacology.clashmc.core.rank.adapter;

import com.google.common.base.Strings;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.rank.cache.RankCache;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.ChatColor;



import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RankParameterAdapter implements BladeProvider<Rank> {

  private final RankCache rankCache;

  public RankParameterAdapter(RankCache rankCache) {
    this.rankCache = rankCache;
  }

  
  @Override
  public Rank provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;

    return this.rankCache
        .get(input)
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono rangi "
                        + ChatColor.YELLOW
                        + input
                        + ChatColor.RED
                        + "."));
  }


  @Override
  public List<String> suggest( BladeContext context,  String input)
      throws BladeExitMessage {
    return this.rankCache.values().stream()
        .map(Rank::getName)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
