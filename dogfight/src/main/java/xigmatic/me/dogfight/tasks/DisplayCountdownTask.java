package xigmatic.me.dogfight.tasks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Objects;

public class DisplayCountdownTask extends CountdownTask {
    private final ArrayList<Player> players;

    /**
     * @param action Segment of code that will run after the countdown has finished
     * @param duration Length of time the until the runnable is executed in seconds
     * @param players List of players to show the countdown timer to on their actionbar
     */
    public DisplayCountdownTask(Runnable action, int duration, ArrayList<Player> players) {
        super(action, duration);
        this.players = players;
    }


    /**
     * Converts and displays current countdown time left in standard time notation
     * @param tick Current countdown tick (decreases over time)
      */
    private void tickToActionbarTime(int tick) {
        // Creates the string that shows the time left (if-statement is within the declaration here \/ \/ \/)
        String timeString = (tick > 1200) ? (tick / 1200) + ":" + String.format("%02d", (tick % 1200) / 20) : String.format("%,.2f", tick / 20.0);

        // Sends message to players
        for (Player player : this.players) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(timeString));
        }

        // FOR TESTING - Sends time to server console
        Bukkit.getConsoleSender().sendMessage(timeString);
    }


    public void run() {
        super.run();

        // Prints time left on actionbar
        tickToActionbarTime(super.getTimeLeft());
    }
}