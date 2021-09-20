package me.ponktacology.clashmc.api.util;

import lombok.experimental.UtilityClass;
import me.ponktacology.clashmc.api.settings.Settings;
import me.ponktacology.clashmc.api.settings.annotation.SettingsKey;


@UtilityClass
public class SettingsUtil {

  public static String getKey( Class<? extends Settings> type) {
    if (!type.isAnnotationPresent(SettingsKey.class)) {
      throw new NullPointerException("no SingletoneKey annotation on Settings object!");
    }

    SettingsKey settingsKey = type.getAnnotation(SettingsKey.class);

    return settingsKey.key();
  }
}
