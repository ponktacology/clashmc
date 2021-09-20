package me.ponktacology.clashmc.core.log.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.log.Log;


@RequiredArgsConstructor
@Getter
public class ChatLog extends Log {


  private final String command;
}
