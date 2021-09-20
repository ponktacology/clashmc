package me.ponktacology.clashmc.core.log.command;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.log.Log;


@RequiredArgsConstructor
@Getter
public class CommandLog extends Log {


  private final String command;
}
