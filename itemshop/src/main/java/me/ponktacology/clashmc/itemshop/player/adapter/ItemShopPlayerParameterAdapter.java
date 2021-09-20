package me.ponktacology.clashmc.itemshop.player.adapter;

import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.itemshop.player.ItemShopPlayer;
import me.ponktacology.clashmc.itemshop.player.cache.ItemShopPlayerCache;
import me.vaperion.blade.command.argument.BladeProvider;
import me.vaperion.blade.command.container.BladeParameter;
import me.vaperion.blade.command.context.BladeContext;
import me.vaperion.blade.command.exception.BladeExitMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;



import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ItemShopPlayerParameterAdapter implements BladeProvider<ItemShopPlayer> {


  private final ItemShopPlayerCache playerCache;

  
  @Override
  public ItemShopPlayer provide(
       BladeContext bladeContext,
       BladeParameter bladeParameter,
       String input)
      throws BladeExitMessage {
    if (input == null) return null;

    return this.playerCache
        .getOrCreate(input);
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
