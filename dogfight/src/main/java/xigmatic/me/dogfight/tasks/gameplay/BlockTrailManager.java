package xigmatic.me.dogfight.tasks.gameplay;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import xigmatic.me.dogfight.Dogfight;

import java.util.ArrayList;
import java.util.HashMap;

public class BlockTrailManager {
    private Player[] players;
    final private HashMap<Player, ArrayList<Location>> previousPlayerLocationMap = new HashMap<>();
    private int locationListLength;
    private int ticksLeft;
    private int taskID;


    // LIST OF PLAYERS WILL NEED TO BE TIED BACK TO WHO'S ALIVE (POSSIBLY BY USING GAMEMANAGER)


    /**
     * Creates a block trail manager necessary for gameplay
     * @param players List of players to apply the Block Trail to
     */
    public BlockTrailManager(Player[] players) {
        this.players = players;
        for(Player player : players) {
            this.previousPlayerLocationMap.put(player, new ArrayList<>());
        }
    }


    /**
     * Begins spawning a trail of blocks slightly behind the player
     * @param duration Number of seconds the trail spawning will last
     */
    public void spawnTrails(int duration) {
        // Declares the amount of ticks left
        this.ticksLeft = duration * 20;

        // Schedules the trail task
        this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {
            // Checks if the previous 5 locations have been declared yet and only spawns blocks once those 5 slots are filled
            if(this.locationListLength < 5) {
                for(Player player : this.players) {
                    this.previousPlayerLocationMap.get(player).add(player.getLocation());
                }

                this.locationListLength++;
            } else {
                // Spawns in the blocks for the trail @ the first location, removes it, and inserts a new location at the end
                for(Player player : this.players) {
                    Location blockLoc = this.previousPlayerLocationMap.get(player).get(0);
                    blockLoc.getWorld().getBlockAt(blockLoc).setType(Material.PINK_WOOL);
                    this.previousPlayerLocationMap.get(player).remove(0);
                    this.previousPlayerLocationMap.get(player).add(player.getLocation());
                }
            }

            // Checks if the time for the blocks to spawn has terminated and cancels the current task if true
            if(this.ticksLeft == 0) {
                Bukkit.getScheduler().cancelTask(this.taskID);
            }

            this.ticksLeft--;
        }, 1, 0);




    }
}
