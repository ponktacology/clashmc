package me.ponktacology.clashmc.api.player;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



import java.util.Locale;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Setter
public abstract class PlayerWrapper {

  @SerializedName("_id")
  private UUID uuid;

  private String name;
  private String nameLowerCase;

  public PlayerWrapper(UUID uuid,  String name) {
    this.uuid = uuid;
    this.name = name;
    this.nameLowerCase = name.toLowerCase(Locale.ROOT);
  }

  @Override
  public int hashCode() {
    return this.getUuid().hashCode();
  }

  @Override
  public boolean equals( Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    PlayerWrapper that = (PlayerWrapper) o;

    return this.getUuid().equals(that.getUuid());
  }
}
