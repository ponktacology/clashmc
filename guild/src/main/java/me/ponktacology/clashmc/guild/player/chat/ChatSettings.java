package me.ponktacology.clashmc.guild.player.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.ponktacology.clashmc.api.settings.PlayerSettings;


import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ChatSettings implements PlayerSettings {

  private final Map<Settings, Boolean> settings = new HashMap<>();

  public boolean isEnabled(Settings setting) {
    return this.settings.computeIfAbsent(setting, it -> true);
  }

  public void toggle(Settings setting) {
    this.settings.put(setting, !isEnabled(setting));
  }

  @RequiredArgsConstructor
  @Getter
  public enum Settings {
    GLOBAL_CHAT_MESSAGES("Wiadomości globalne"),
    DEATH_MESSAGES("Wiadomości dotyczące śmierci"),
    AUTO_MESSAGES("Automatyczne wiadomości serwerowe"),
    PRIVATE_MESSAGES("Prywatne wiadomości"),
    GUILD_MESSAGES("Wiadomości dotyczące gildii"),
    CRATE_MESSAGES("Wiadomości dotyczące skrzynek"),
    ITEM_SHOP_MESSAGES("Wiadomości o zakupach w itemshopie");

    
    private final String formattedName;
  }
}
