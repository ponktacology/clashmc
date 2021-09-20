package me.ponktacology.clashmc.itemshop.player;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import me.ponktacology.clashmc.api.service.data.Entity;
import me.ponktacology.clashmc.core.CorePlugin;
import me.ponktacology.clashmc.core.player.BukkitPlayerWrapper;
import me.ponktacology.clashmc.core.player.CorePlayer;
import me.ponktacology.clashmc.core.rank.grant.Grant;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
@Entity(collection = "players", database = "itemshop")
public class ItemShopPlayer extends BukkitPlayerWrapper {

  private final List<Grant> grants = Lists.newCopyOnWriteArrayList();
  private final List<ItemStack> items = Lists.newCopyOnWriteArrayList();

  public ItemShopPlayer(UUID uuid,  String name) {
    super(uuid, name);
  }

  // Player must be online
  public void initRanks() {
    Player player = this.getPlayer();

    if(player == null) return;

    Optional<CorePlayer> corePlayerOptional =
        CorePlugin.INSTANCE.getPlayerCache().get(player);

    corePlayerOptional.ifPresent(
        it -> {
          this.grants.forEach(it::addRank);
          this.clearRanks();
        });
  }

  public void addRank(Grant grant) {
    this.grants.add(grant);
  }

  public void addItem(ItemStack item) {
    this.items.add(item);
  }

  private void clearRanks() {
    this.grants.clear();
  }

  public void clearItems() {
    this.items.clear();
  }
}
