package me.ponktacology.clashmc.drop.item;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Material;


@RequiredArgsConstructor
@Getter
public enum DropType {
  STONE("&eDrop ze stone", "&eDrop ze stone", Material.STONE),
  LEAVES("&eDrop z liści", "&eDrop z liści", Material.LEAVES),
  COBBLEX("&eDrop z cobblex", "&eDrop z cobblex", Material.MOSSY_COBBLESTONE);


  private final String menuTitle;

  private final String buttonTitle;

  private final Material icon;
}
