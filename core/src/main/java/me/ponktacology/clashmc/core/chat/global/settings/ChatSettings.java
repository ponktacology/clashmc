package me.ponktacology.clashmc.core.chat.global.settings;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;


import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@Entity(database = "core", collection = "chat-settings")
@SettingsKey(key = "chat-settings")
public class ChatSettings extends Settings {

  private long delay = TimeUnit.MILLISECONDS.convert(3, TimeUnit.SECONDS);

  private ChatState state = ChatState.NORMAL;
  private int minPower;

  @RequiredArgsConstructor
  @Getter
  public enum ChatState {
    NORMAL("Normalny"),
    DELAYED("Zwolniony"),
    POWER("Limit power"),
    STAFF("Administracyjny");


    private final String formattedName;

    public ChatState next() {
      ChatState[] states = ChatState.values();
      int index = Arrays.asList(states).indexOf(this);

      if (++index == states.length) {
        index = 0;
      }

      return states[index];
    }
  }
}
