package xigmatic.me.dogfight.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import xigmatic.me.dogfight.Dogfight;

public class CountdownTask extends BukkitRunnable {
    private int timeLeft;
    private Runnable action;
    private boolean active;
    /**
     * @param action Segment of code that is run every in-game tick
     * @param duration Length of time the runnable will loop in seconds
     */
    public CountdownTask(Runnable action, int duration) {
        this.action = action;
        this.timeLeft = duration * 20;
        this.active = false;
    }


    /**
     * Returns the number of ticks left before the countdown concludes
     * @return Ticks left
     */
    public int getTimeLeft() {
        return timeLeft;
    }


    /**
     * Starts the countdown
     */
    public void start() {
        super.runTaskTimer(Dogfight.getPlugin(Dogfight.class),0,1);
        this.active = true;
    }


    /**
     * Pauses the countdown
     */
    public boolean pause() {
        if (this.active) {
            this.active = false;
            return true;
        }
        return false;
    }


    /**
     * Resumes the countdown if paused
     */
    public boolean resume() {
        if (!this.active) {
            this.active = true;
            return true;
        }
        return false;
    }


    @Override
    public void run() {
        if(!active)
            return;

        timeLeft--;

        if(this.timeLeft <= 0) {
            this.action.run();
            cancel();
        }
    }
}
