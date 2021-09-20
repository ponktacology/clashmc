package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public abstract class Action {

  
  private final ActionType type;
}
