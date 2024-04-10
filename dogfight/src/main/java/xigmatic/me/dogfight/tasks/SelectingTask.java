package xigmatic.me.dogfight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import xigmatic.me.dogfight.inventory.InventoryManager;

public class SelectingTask extends CountdownTask {
    private final InventoryManager inventoryManager;


    /**
     * @param action   Segment of code that will run after the countdown has finished
     * @param duration Length of time the until the runnable is executed in seconds
     */
    public SelectingTask(Runnable action, int duration, InventoryManager inventoryManager) {
        super(action, duration);
        this.inventoryManager = inventoryManager;
    }


    public void run() {
        // Runs at exactly 5 seconds left
        if(getTimeLeft() == 5 * 20)
            Bukkit.broadcastMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "5 seconds left to select roles");

        super.run();
    }
}
