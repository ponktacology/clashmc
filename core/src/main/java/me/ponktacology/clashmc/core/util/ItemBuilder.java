package me.ponktacology.clashmc.core.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBuilder  {

    private final ItemStack is;

    public ItemBuilder( Material mat) {
        is = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack is) {
        this.is = is;

        if (!this.is.hasItemMeta()) {
            this.is.getItemMeta();
        }
    }


    public ItemBuilder amount(int amount) {
        is.setAmount(amount);
        return this;
    }


    public ItemBuilder name( String name) {
        ItemMeta meta = is.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        is.setItemMeta(meta);
        return this;
    }


    public ItemBuilder lore( String name) {
        ItemMeta meta = is.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(lore);

        is.setItemMeta(meta);

        return this;
    }


    public ItemBuilder lore( String... lore) {
        ItemMeta meta = is.getItemMeta();
        List<String> toSet = meta.getLore();

        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }


    public ItemBuilder lore( List<String> lore) {
        ItemMeta meta = is.getItemMeta();
        List<String> toSet = meta.getLore();

        if (toSet == null) {
            toSet = new ArrayList<>();
        }

        for (String string : lore) {
            toSet.add(ChatColor.translateAlternateColorCodes('&', string));
        }

        meta.setLore(toSet);
        is.setItemMeta(meta);

        return this;
    }


    public ItemBuilder durability(int durability) {
        is.setDurability((short) durability);
        return this;
    }


    public ItemBuilder skull(String name) {
        try {
            SkullMeta im = (SkullMeta) is.getItemMeta();
            is.setDurability((byte) 3);
            im.setOwner(name);
            is.setItemMeta(im);
        } catch (ClassCastException expected) {
        }
        return this;
    }


    public ItemBuilder enchantment(Enchantment enchantment, int level) {
        is.addUnsafeEnchantment(enchantment, level);
        return this;
    }


    public ItemBuilder enchantment( Map<Enchantment, Integer> enchantmens) {
        for (Map.Entry<Enchantment, Integer> enchantmentIntegerEntry : enchantmens.entrySet()) {
            is.addUnsafeEnchantment(enchantmentIntegerEntry.getKey(), enchantmentIntegerEntry.getValue());
        }

        return this;
    }


    public ItemBuilder enchantment(Enchantment enchantment) {
        is.addUnsafeEnchantment(enchantment, 1);
        return this;
    }


    public ItemBuilder type(Material material) {
        is.setType(material);
        return this;
    }


    public ItemBuilder clearLore() {
        ItemMeta meta = is.getItemMeta();

        meta.setLore(new ArrayList<>());
        is.setItemMeta(meta);

        return this;
    }


    public ItemBuilder clearEnchantments() {
        for (Enchantment e : is.getEnchantments().keySet()) {
            is.removeEnchantment(e);
        }

        return this;
    }

    public ItemStack build() {
        return is;
    }
}
