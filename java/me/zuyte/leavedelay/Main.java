package me.zuyte.leavedelay;

import com.andrei1058.bedwars.api.BedWars;
import com.andrei1058.bedwars.api.configuration.ConfigManager;
import me.zuyte.leavedelay.events.InventoryEvents;
import me.zuyte.leavedelay.events.ItemEvents;
import me.zuyte.leavedelay.events.PlayerEvents;
import me.zuyte.leavedelay.utils.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin {
    private static ConfigManager cfg;
    private static ConfigManager msg;
    private static BedWars bw;
    private static Main instance;
    private final Map<String, Boolean> click = new HashMap<>();
    @Override
    public void onEnable() {
        if (Bukkit.getPluginManager().getPlugin("BedWars1058") == null) {
            getLogger().severe("BedWars1058 was not found. Disabling...");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        instance = this;
        bw = Bukkit.getServicesManager().getRegistration(BedWars.class).getProvider();
        cfg = new ConfigManager(this, "config", "plugins/BedWars1058/Addons/Leave-Delay");
        msg = new ConfigManager(this, "messages", "plugins/BedWars1058/Addons/Leave-Delay");
        getServer().getPluginManager().registerEvents(new ItemEvents(), this);
        getServer().getPluginManager().registerEvents(new PlayerEvents(), this);
        getServer().getPluginManager().registerEvents(new InventoryEvents(), this);
        setupConfigs();
        getLogger().info("Plugin enabled successfully!");
    }

    public static ConfigManager getCfg() {
        return cfg;
    }

    public static ConfigManager getMsg() {
        return msg;
    }

    public static BedWars getBedWars() {
        return bw;
    }

    public static Main getInstance() {
        return instance;
    }

    public Boolean existClick(String s) {
        return click.containsKey(s);
    }

    public void setClick(String s) {
        click.put(s, true);
    }

    public void removeClick(String s) {
        click.remove(s);
    }

    private static void setupConfigs() {
        YamlConfiguration cfgYml = getCfg().getYml();
        cfgYml.options().header("Bedwars1058-LeaveDelay Addon for Bedwars1058 by Zuyte!\nDocumentation: https://zuyte.netlify.app/docs/Welcome");
        cfgYml.addDefault(ConfigPath.CFG_ITEM_MATERIAL, getBedWars().getForCurrentVersion("BED", "BED", "RED_BED"));
        cfgYml.addDefault(ConfigPath.CFG_ITEM_SLOT, 8);
        cfgYml.addDefault(ConfigPath.CFG_ITEM_NAME, "&c&lReturn to Lobby &7(Right Click)");
        cfgYml.addDefault(ConfigPath.CFG_ITEM_LORE, Arrays.asList("&fRight-Click to leave arena!"));
        cfgYml.addDefault(ConfigPath.CFG_SET_DELAY, 3);
        cfgYml.options().copyDefaults(true);
        getCfg().save();

        YamlConfiguration msgYml = getMsg().getYml();
        msgYml.options().header("Bedwars1058-LeaveDelay Addon for Bedwars1058 by Zuyte!\nDocumentation: https://zuyte.netlify.app/docs/Welcome");
        msgYml.addDefault(ConfigPath.MSG_CLICK_START, "&a&lTeleporting you to the lobby in 3 seconds... Right-click again to cancel the teleport!");
        msgYml.addDefault(ConfigPath.MSG_CLICK_CANCEL, "&c&lTeleport cancelled!");
        msgYml.options().copyDefaults(true);
        getMsg().save();
    }

    public static void bedItem(Player p) {
        Material BedMaterial = null;
        try {
            BedMaterial = Material.getMaterial(getCfg().getYml().getString(ConfigPath.CFG_ITEM_MATERIAL));
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_MATERIAL from config.yml, Material name might be invalid please check again!");
            ex.printStackTrace();
            return;
        }
        ItemStack BedItem = new ItemStack(BedMaterial, 1);
        ItemMeta BedMeta = BedItem.getItemMeta();
        try {
            BedMeta.setDisplayName(ColorUtil.getMsg(Main.getCfg().getString(ConfigPath.CFG_ITEM_NAME)));
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_NAME from config.yml");
            ex.printStackTrace();
            return;
        }
        try {
            BedMeta.setLore(getInstance().colorizeList(Main.getCfg().getYml().getStringList(ConfigPath.CFG_ITEM_LORE)));
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_LORE from config.yml");
            ex.printStackTrace();
            return;
        }
        BedMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        BedItem.setItemMeta(BedMeta);
        ItemStack BedItem1 = Main.getBedWars().getVersionSupport().addCustomData(BedItem, "BWLEAVE-DELAY");
        try {
        p.getInventory().setItem(Integer.parseInt(Main.getCfg().getString(ConfigPath.CFG_ITEM_SLOT)), BedItem1);
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_SLOT from config.yml");
            ex.printStackTrace();
            return;
        }
    }

    public List<String> colorizeList(List<String> input) {
        List<String> ret = new ArrayList<String>();
        try {
            for (String line : input) ret.add(ChatColor.translateAlternateColorCodes('&', line));
        } catch (Exception ex) {
            Main.getInstance().getLogger().severe("An error occurred while getting CFG_ITEM_LORE from config.yml");
            ex.printStackTrace();
            return null;
        }
        return ret;
    }
}
