package me.ponktacology.clashmc.guild.guild.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ActionType {
  PLAYER_JOIN,
  PLAYER_LEAVE,
  PLAYER_KICK,
  CREATE,
  WAR_START,
  WAR_WON,
  WAR_LOST,
  CONQUER,
  ALLY,
  BREAK_ALLY;
}
