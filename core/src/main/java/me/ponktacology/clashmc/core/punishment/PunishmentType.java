package me.ponktacology.clashmc.core.punishment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public enum PunishmentType {
  BLACKLIST("Blacklista", "zblacklistowany", "odblacklistowany"),
  BAN("Ban", "zbanowany", "odbanowany"),
  KICK("Kick", "wyrzucony", "wyrzucony"),
  MUTE("Mute", "wyciszony", "odciszony");

  
  private final String formattedName;
  
  private final String addFormat;
  
  private final String removeFormat;
}
