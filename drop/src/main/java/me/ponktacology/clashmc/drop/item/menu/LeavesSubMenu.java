package me.ponktacology.clashmc.drop.item.menu;

import me.ponktacology.clashmc.api.dispatcher.TaskDispatcher;
import me.ponktacology.clashmc.core.menu.Button;
import me.ponktacology.clashmc.core.menu.Menu;
import me.ponktacology.clashmc.core.menu.pagination.SubMenu;
import me.ponktacology.clashmc.crate.crate.cache.CrateCache;
import me.ponktacology.clashmc.drop.item.DropItem;
import me.ponktacology.clashmc.drop.item.DropType;
import me.ponktacology.clashmc.drop.item.cache.DropItemCache;
import me.ponktacology.clashmc.drop.item.menu.button.DropItemButton;
import me.ponktacology.clashmc.drop.player.cache.DropPlayerCache;
import org.bukkit.entity.Player;


import java.util.Map;

public class LeavesSubMenu extends SubMenu {

    private final DropItemCache dropItemCache;
    private final DropPlayerCache playerCache;
    private final TaskDispatcher taskDispatcher;
    private final CrateCache crateCache;

    public LeavesSubMenu(
            DropItemCache dropItemCache,
            DropPlayerCache playerCache,
            TaskDispatcher taskDispatcher,
            CrateCache crateCache) {
        super(18);
        this.dropItemCache = dropItemCache;
        this.playerCache = playerCache;
        this.taskDispatcher = taskDispatcher;
        this.crateCache = crateCache;
        setPlaceholder(true);
    }

    @Override
    public String getTitle(Player player) {
        return DropType.LEAVES.getMenuTitle();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = super.getButtons(player);

        for (DropItem dropItem : this.dropItemCache.values(DropType.LEAVES)) {
            buttons.put(
                    dropItem.getIndex(),
                    new DropItemButton(dropItem, this.playerCache, this.taskDispatcher));
        }

        return buttons;
    }


    @Override
    public Menu getPrevious(Player player) {
        return new DropMenu(this.crateCache, this.dropItemCache, this.playerCache, this.taskDispatcher);
    }
}
