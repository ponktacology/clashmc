package me.ponktacology.clashmc.guild.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@RequiredArgsConstructor
@Getter
public class Tuple<V, K> {


  private final V key;

  private final K value;
}
