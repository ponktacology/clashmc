package me.ponktacology.clashmc.kit.kit.command;

import lombok.RequiredArgsConstructor;
import me.ponktacology.clashmc.core.rank.Rank;
import me.ponktacology.clashmc.core.time.Time;
import me.ponktacology.clashmc.core.util.Text;
import me.ponktacology.clashmc.kit.KitPermissions;
import me.ponktacology.clashmc.kit.kit.Kit;
import me.ponktacology.clashmc.kit.kit.cache.KitCache;
import me.ponktacology.clashmc.kit.kit.factory.KitFactory;
import me.ponktacology.clashmc.kit.menu.KitMenuFactory;
import me.ponktacology.clashmc.kit.kit.updater.KitUpdater;
import me.ponktacology.clashmc.kit.player.KitPlayer;
import me.ponktacology.clashmc.kit.player.cache.KitPlayerCache;
import me.vaperion.blade.command.annotation.Command;
import me.vaperion.blade.command.annotation.Name;
import me.vaperion.blade.command.annotation.Permission;
import me.vaperion.blade.command.annotation.Sender;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
public class KitCommand {


  private final KitCache kitCache;
  private final KitFactory kitFactory;
  private final KitPlayerCache playerCache;
  private final KitUpdater kitUpdater;
  private final KitMenuFactory menuFactory;

  @Command(value = "kit", description = "Otwiera menu kitów")
  public void kit(
           @Sender Player sender,
           @me.vaperion.blade.command.annotation.Optional("gui") @Name("kit") String name) {
    if ("gui".equals(name)) {
      this.menuFactory.getKitSelectorMenu().openMenu(sender);
    } else {
      Optional<Kit> kitOptional = this.kitCache.get(name);

      if (!kitOptional.isPresent()) {
        sender.sendMessage(Text.colored("&cNie znaleziono takiego zestawu."));
        return;
      }

      Kit kit = kitOptional.get();

      Optional<KitPlayer> kitPlayerOptional = this.playerCache.get(sender);

      if (!kitPlayerOptional.isPresent()) {
        return;
      }

      KitPlayer kitPlayer = kitPlayerOptional.get();

      kitPlayer.handleKit(kit);
    }
  }

  @Command(value = "kit create", description = "Tworzy nowy kit", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitCreate( @Sender Player sender,  @Name("name") String name) {
    if (this.kitCache.get(name).isPresent()) {
      sender.sendMessage(Text.colored("&cTaki zestaw już istnieję."));
      return;
    }

    Kit kit = this.kitFactory.create(name);

    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie utworzono nowy zestaw."));
  }

  @Command(value = "kit remove", description = "Usuwa kit", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitRemove( @Sender Player sender, @Name("kit") Kit kit) {
    this.kitUpdater.remove(kit);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto zestaw."));
  }

  @Command(value = "kit icon", description = "Ustawia ikonę kita", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitIcon( @Sender Player sender,  @Name("name") Kit kit) {
    ItemStack itemInHand = sender.getItemInHand();

    if (itemInHand == null || itemInHand.getType() == Material.AIR) {
      sender.sendMessage(Text.colored("&cMusisz trzymać jakiś przedmiot w ręce."));
      return;
    }

    kit.setIcon(itemInHand.clone());
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono ikone zestawu."));
  }

  @Command(value = "kit inventory", description = "Ustawia zawartość kita", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitInventory( @Sender Player sender,  @Name("kit") Kit kit) {
    kit.setItems(sender.getInventory().getContents());
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono ekwipunek zestawu."));
  }

  @Command(
      value = "kit delay",
      description = "Ustawia odstęp czasowy pomiędzy użyciami kita",
      async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitDelay( @Sender Player sender,  @Name("kit") Kit kit,  @Name("delay") Time delay) {
    kit.setDelay(delay.getTimeStamp());
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono odstęp pomiędzy użyciami zestawu."));
  }

  @Command(value = "kit index", description = "Ustawa index kita", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitIndex( @Sender Player sender,  @Name("kit") Kit kit, @Name("index") int index) {
    kit.setIndex(index);
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie zmieniono index zestawu."));
  }

  @Command(value = "kit ranks list", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitRanksList( @Sender Player sender,  @Name("kit") Kit kit) {
    sender.sendMessage(
        Arrays.toString(kit.getAllowedRanks().stream().map(Rank::getName).toArray()));
  }

  @Command(value = "kit ranks add", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitRanksAdd( @Sender Player sender,  @Name("kit") Kit kit,  @Name("rank") Rank rank) {
    if (kit.isRankAllowed(rank)) {
      sender.sendMessage(Text.colored("&cTa ranga już należy do tego zestawu."));
      return;
    }

    kit.addRank(rank);
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie dodano rangę do zestawu."));
  }

  @Command(value = "kit ranks remove", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitRanksRemove( @Sender Player sender,  @Name("kit") Kit kit,  @Name("rank") Rank rank) {
    if (!kit.isRankAllowed(rank)) {
      sender.sendMessage(Text.colored("&cTa ranga nie należy do tego zestawu."));
      return;
    }

    kit.removeRank(rank);
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie usunięto rangę z zestawu."));
  }

  @Command(value = "kit enable", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitEnable( @Sender Player sender,  @Name("kit") Kit kit) {
    kit.setEnabled(true);
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie wyłączono zestaw."));
  }

  @Command(value = "kit disable", async = true)
  @Permission(KitPermissions.KIT_MANAGE)
  public void kitDisable( @Sender Player sender,  @Name("kit") Kit kit) {
    kit.setEnabled(false);
    this.kitUpdater.update(kit);

    sender.sendMessage(Text.colored("&aPomyślnie wyłączono zestaw."));
  }
}
