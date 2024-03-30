package xigmatic.me.dogfight.tasks.gameplay;

import org.bukkit.entity.Player;
import xigmatic.me.dogfight.tasks.DisplayCountdownTask;

public class RoundOneTask extends DisplayCountdownTask {
    private int ticksUntilTrail = 10 * 20;
    private final BlockTrailManager blockTrailManager;


    /**
     * Creates and manages all Round 1 Gameplay
     * @param action   Segment of code that will run after the countdown has finished
     * @param duration Length of time the until the runnable is executed in seconds
     * @param players  List of players to show the countdown timer to on their actionbar
     */
    public RoundOneTask(Runnable action, int duration, Player[] players) {
        super(action, duration, players);
        this.blockTrailManager = new BlockTrailManager(players);
    }


    public void run() {
        if(ticksUntilTrail == 0) {
            this.blockTrailManager.spawnTrails(5);
        }



        ticksUntilTrail--;

        super.run();
    }
}
