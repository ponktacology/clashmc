package me.ponktacology.clashmc.core.rank;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;

import java.util.ArrayList;
import java.util.List;

@ToString
@Getter
@Setter
@Entity(collection = "ranks", database = "core")
public class Rank {

  @SerializedName("_id")
  private final String name;

  private final List<String> permissions = new ArrayList<>();


  private String prefix = "";

  private String suffix = "";

  private String color = "&7";

  private String messageColor = "";
  private boolean staff;
  private int power;

  public Rank(String name) {
    this.name = name;
  }

  public void save() {
    CorePlugin.INSTANCE.getDataService().save(this);
  }

  @Override
  public boolean equals( Object obj) {
    if (obj == null) return false;
    if (!(obj instanceof Rank)) return false;

    Rank rank = (Rank) obj;

    return name.equals(rank.getName());
  }

  @Override
  public int hashCode() {
    return name != null ? name.hashCode() : 0;
  }

  public String getFormattedName() {
    return this.color + this.name;
  }
}
