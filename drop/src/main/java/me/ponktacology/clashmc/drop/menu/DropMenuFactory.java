package me.ponktacology.clashmc.drop.menu;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.MenuFactory;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.generator.Generator;
import me.ponktacology.clashmc.drop.generator.menu.GeneratorMenu;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.DropMenu;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;


@RequiredArgsConstructor
public class DropMenuFactory implements MenuFactory {


  private final CrateCache crateCache;

  private final DropItemCache dropItemCache;

  private final DropPlayerCache playerCache;

  private final TaskDispatcher taskDispatcher;


  public DropMenu getDropMenu() {
    return new DropMenu(this.crateCache, this.dropItemCache, this.playerCache, this.taskDispatcher);
  }


  public GeneratorMenu getGeneratorMenu(Generator generator, DropPlayer dropPlayer) {
    return new GeneratorMenu(generator, dropPlayer, this.taskDispatcher);
  }
}
