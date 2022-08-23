package me.zuyte.leavedelay.events;

import com.andrei1058.bedwars.api.arena.GameState;
import com.andrei1058.bedwars.api.events.player.PlayerJoinArenaEvent;
import me.zuyte.leavedelay.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

public class PlayerEvents implements Listener {

    @EventHandler
    public void onPlayerArenaJoin(PlayerJoinArenaEvent e) {
        Player p = e.getPlayer();
        if (e.getArena().getStatus() == GameState.waiting || e.getArena().getStatus() == GameState.starting) {
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), new Runnable() {
                @Override
                public void run() {
                    String preitemslot = Main.getBedWars().getConfigs().getMainConfig().getYml().getString(".pre-game-items.leave.slot");
                    p.getInventory().setItem(Integer.parseInt(preitemslot), new ItemStack(Material.AIR, 1));
                    Main.bedItem(p);
                }
            }, 10L);
        }
    }
}
