package xigmatic.me.dogfight.tasks.gameplay;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.util.Vector;
import xigmatic.me.dogfight.Dogfight;

public class PointListeners implements Listener {
    private int collisionTaskID;


    public PointListeners() {}


    @EventHandler
    public void onNetherPortalEnter(EntityPortalEnterEvent event) {
        Player player;
        if(event.getEntity() instanceof Player)
            player = (Player) event.getEntity();
        else
            return;

        player.sendMessage("You entered a portal congrats now its gone");

        // Removes portal block from world (TEMPORARY)
        event.getLocation().getBlock().setType(Material.AIR);
    }


    /**
     * Starts collision detection (checks every other tick)
     */
    public void startBlockCollisionDetector() {
        this.collisionTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {
            for(Player player : PlayerLifeManager.getAlivePlayersAsEntities()) {
                Vector velocity = player.getVelocity();
                if(velocity.getX() == 0 || velocity.getZ() == 0 || velocity.getY() == 0) {
                    // Kills player automatically if they touch a block
                    PlayerLifeManager.killPlayer(player);

                    // Puts player in spectator mode after they die
                    player.setGameMode(GameMode.SPECTATOR);
                }
            }
        }, 2, 0);
    }


    /**
     * Stops block collision detection (stops checking every other tick)
     */
    public void stopBlockCollisionDetector() {
        Bukkit.getScheduler().cancelTask(this.collisionTaskID);
    }
}