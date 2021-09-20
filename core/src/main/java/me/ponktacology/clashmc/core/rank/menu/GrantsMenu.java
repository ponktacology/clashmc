package me.ponktacology.clashmc.core.rank.menu;

import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.pagination.PaginatedMenu;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.player.factory.CorePlayerFactory;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import me.ponktacology.clashmc.core.rank.menu.button.GrantButton;
import org.bukkit.entity.Player;


import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class GrantsMenu extends PaginatedMenu {

  private final CorePlayer corePlayer;
  private final CorePlayerFactory playerFactory;


  public GrantsMenu( CorePlayer corePlayer, CorePlayerFactory playerFactory) {
    super(18);
    this.corePlayer = corePlayer;
    this.playerFactory = playerFactory;
  }


  @Override
  public String getPrePaginatedTitle(Player player) {
    return "&eRangi gracza &e" + corePlayer.getName();
  }


  @Override
  public Map<Integer, Button> getAllPagesButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    for (Grant grant : corePlayer.getGrants().stream()
            .sorted((o1, o2) -> (int) -(o1.getAddedOn() - o2.getAddedOn()))
            .collect(Collectors.toList())) {
      buttons.put(buttons.size(), new GrantButton(grant, playerFactory));
    }

    return buttons;
  }
}
