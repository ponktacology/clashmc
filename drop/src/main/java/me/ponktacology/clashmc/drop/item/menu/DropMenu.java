package me.ponktacology.clashmc.drop.item.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.crate.crate.Crate;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.button.CrateButton;
import me.ponktacology.clashmc.drop.item.menu.button.DropTypeButton;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class DropMenu extends Menu {

  private final CrateCache crateCache;

  private final DropItemCache dropItemCache;

  private final DropPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;

  {
    setPlaceholder(true);
  }

  @Override
  public String getTitle(Player player) {
    return "&eDrop";
  }

  @Override
  public Map<Integer, Button> getButtons(Player player) {
    Map<Integer, Button> buttons = new HashMap<>();

    Optional<Crate> normalCrateOptional = this.crateCache.get("normal");

    if (normalCrateOptional.isPresent()) {
      Crate normalCrate = normalCrateOptional.get();

      buttons.put(
          11,
          new CrateButton(
              normalCrate,
              this.crateCache,
              this.dropItemCache,
              this.playerCache,
              this.taskDispatcher));
    }

    buttons.put(
        13,
        new DropTypeButton(
            DropType.LEAVES,
            this.dropItemCache,
            this.playerCache,
            this.crateCache,
            this.taskDispatcher));

    Optional<Crate> premiumCrateOptional = this.crateCache.get("premium");

    if (premiumCrateOptional.isPresent()) {
      Crate premiumCrate = premiumCrateOptional.get();

      buttons.put(
          15,
          new CrateButton(
              premiumCrate,
              this.crateCache,
              this.dropItemCache,
              this.playerCache,
              this.taskDispatcher));
    }

    buttons.put(
        21,
        new DropTypeButton(
            DropType.STONE,
            this.dropItemCache,
            this.playerCache,
            this.crateCache,
            this.taskDispatcher));

    buttons.put(
        23,
        new DropTypeButton(
            DropType.COBBLEX,
            this.dropItemCache,
            this.playerCache,
            this.crateCache,
            this.taskDispatcher));

    return buttons;
  }

  @Override
  public int getSize() {
    return 36;
  }
}
