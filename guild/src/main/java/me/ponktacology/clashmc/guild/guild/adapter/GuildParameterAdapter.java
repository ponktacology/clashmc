package me.ponktacology.clashmc.guild.guild.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.guild.Guild;
import me.ponktacology.clashmc.guild.guild.cache.GuildCache;
import me.ponktacology.clashmc.guild.player.GuildPlayer;
import me.ponktacology.clashmc.guild.player.cache.GuildPlayerCache;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GuildParameterAdapter implements BladeProvider<Guild> {

  private final GuildCache guildCache;

  private final GuildPlayerCache playerCache;

  @Override
  public Guild provide(BladeContext bladeContext, BladeParameter bladeParameter, String input)
      throws BladeExitMessage {

    if (input == null) return null;

    if ("DEFAULT_VALUE".equals(input)) {
      Optional<GuildPlayer> guildPlayerOptional =
          this.playerCache.get(bladeContext.sender().getName());

      if (!guildPlayerOptional.isPresent()) {
        return null;
      }

      GuildPlayer guildPlayer = guildPlayerOptional.get();

      if (!guildPlayer.hasGuild()) {
        throw new BladeExitMessage(ChatColor.RED + "Nie jesteś w gildii.");
      }

      return guildPlayer.getGuild().get();
    }

    return this.guildCache
        .getByTagOrName(input)
        .orElseThrow(
            () ->
                new BladeExitMessage(
                    ChatColor.RED
                        + "Nie znaleziono gildii "
                        + ChatColor.YELLOW
                        + input
                        + ChatColor.RED
                        + "."));
  }

  @Override
  public List<String> suggest(BladeContext context, String input) throws BladeExitMessage {
    return this.guildCache.values().stream()
        .map(Guild::getTag)
        .filter(
            it ->
                Strings.isNullOrEmpty(input)
                    || it.toLowerCase(Locale.ROOT).startsWith(input.toLowerCase(Locale.ROOT)))
        .collect(Collectors.toList());
  }
}
