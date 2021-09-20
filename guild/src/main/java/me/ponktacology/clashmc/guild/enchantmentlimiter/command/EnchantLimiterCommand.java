package me.ponktacology.clashmc.guild.enchantmentlimiter.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.guild.GuildPermissions;
import me.ponktacology.clashmc.guild.enchantmentlimiter.menu.EnchantLimiterTypeMenu;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class EnchantLimiterCommand {

  @Command(value = "enchantlimiter", description = "Ustawia limit enchantów")
  @Permission(GuildPermissions.ENCHANT_LIMITER)
  public void enchantLimiter(@Sender Player sender) {
    new EnchantLimiterTypeMenu().openMenu(sender);
  }
}
