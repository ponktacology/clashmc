package me.ponktacology.clashmc.core.util;


import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;


@UtilityClass
public class Text {

    public static final String SB_BAR = ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH.toString() + "----------------------";
    public static final String CMD_BAR = ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + ChatColor.STRIKETHROUGH.toString() + "------------";

    
    public static String colored( String message) {
        if (message == null) {
            return null;
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
