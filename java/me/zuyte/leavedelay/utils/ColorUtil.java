package me.zuyte.leavedelay.utils;

import org.bukkit.ChatColor;

public class ColorUtil {
    public static String getMsg(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
