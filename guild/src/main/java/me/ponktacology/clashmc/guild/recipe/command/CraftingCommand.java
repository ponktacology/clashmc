package me.ponktacology.clashmc.guild.recipe.command;

import me.ponktacology.clashmc.guild.recipe.menu.RecipeMenu;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;


public class CraftingCommand {

  @Command(value = "craftingi", description = "Pokazuje wszystkie customowe craftingi")
  public void craftings( @Sender Player sender) {
    new RecipeMenu().openMenu(sender);
  }
}
