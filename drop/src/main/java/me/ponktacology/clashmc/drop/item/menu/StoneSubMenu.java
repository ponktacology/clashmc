package me.ponktacology.clashmc.drop.item.menu;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.core.util.ItemBuilder;
import me.ponktacology.clashmc.core.util.StyleUtil;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.button.DropItemButton;
import me.ponktacology.clashmc.drop.player.DropPlayer;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import me.ponktacology.clashmc.drop.player.statistics.DropPlayerStatistics;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;


import java.util.Map;

public class StoneSubMenu extends SubMenu {

    private final DropItemCache dropItemCache;
    private final DropPlayerCache playerCache;
    private final TaskDispatcher taskDispatcher;
    private final CrateCache crateCache;

    public StoneSubMenu(
            DropItemCache dropItemCache,
            DropPlayerCache playerCache,
            TaskDispatcher taskDispatcher,
            CrateCache crateCache) {
        super(36);
        this.dropItemCache = dropItemCache;
        this.playerCache = playerCache;
        this.taskDispatcher = taskDispatcher;
        this.crateCache = crateCache;
        setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return DropType.STONE.getMenuTitle();
    }

    @Override
    public Map<Integer, Button> getButtons( Player player) {
        Map<Integer, Button> buttons = super.getButtons(player);

        for (DropItem dropItem : this.dropItemCache.values(DropType.STONE)) {
            buttons.put(
                    dropItem.getIndex(),
                    new DropItemButton(dropItem, this.playerCache, this.taskDispatcher));
        }

        DropPlayer dropPlayer = this.playerCache.getOrKick(player);

        buttons.put(
                36,
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.INK_SACK)
                                .durability(10)
                                .name("&aWłącz wszystko")
                                .build();
                    }

                    @Override
                    public boolean shouldUpdate(Player player,  ClickType clickType) {
                        if (clickType.isLeftClick()) {
                            dropPlayer.enableAll();
                            taskDispatcher.runAsync(dropPlayer::save);
                            return true;
                        }

                        return super.shouldUpdate(player, clickType);
                    }
                });

        buttons.put(
                37,
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.INK_SACK)
                                .durability(8)
                                .name("&cWyłącz wszystko")
                                .build();
                    }

                    @Override
                    public boolean shouldUpdate(Player player,  ClickType clickType) {
                        if (clickType.isLeftClick()) {
                            dropPlayer.disableAll();
                            taskDispatcher.runAsync(dropPlayer::save);
                            return true;
                        }

                        return super.shouldUpdate(player, clickType);
                    }
                });

        buttons.put(
                38,
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        return new ItemBuilder(Material.COBBLESTONE)
                                .name("&7Cobblestone")
                                .lore(
                                        "&7Włączony: "
                                                + StyleUtil.convertBooleanToText(dropPlayer.isMineCobblestone()))
                                .build();
                    }

                    @Override
                    public boolean shouldUpdate(Player player,  ClickType clickType) {
                        if (clickType.isLeftClick()) {
                            dropPlayer.toggleMineCobblestone();
                            taskDispatcher.runAsync(dropPlayer::save);
                            return true;
                        }

                        return super.shouldUpdate(player, clickType);
                    }
                });

        buttons.put(
                44,
                new Button() {
                    @Override
                    public ItemStack getButtonItem(Player player) {
                        DropPlayerStatistics statistics = dropPlayer.getStatistics();

                        return new ItemBuilder(Material.GOLD_NUGGET)
                                .name("&eStatystyki")
                                .lore(
                                        "&7Punkty kopania: &f" + dropPlayer.getPoints(),
                                        "&7Wykopany kamień: &f" + statistics.getMinedStone(),
                                        "&7Wykopany obsydian: &f" + statistics.getMinedObsidian())
                                .build();
                    }
                });

        return buttons;
    }


    @Override
    public Menu getPrevious(Player player) {
        return new DropMenu(this.crateCache, this.dropItemCache, this.playerCache, this.taskDispatcher);
    }
}
