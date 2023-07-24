package xigmatic.me.dogfight.tasks;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class DisplayCountdownTask extends CountdownTask {
    /**
     * @param action Segment of code that is run every in-game tick
     * @param duration Length of time the runnable will loop in seconds
     */
    public DisplayCountdownTask(Runnable action, int duration) {
        super(action, duration);
    }


    private void tickToActionbarTime(int tick) {
        if (getTimeLeft() > 1200)
            Objects.requireNonNull(Bukkit.getPlayer("Xigmatic")).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((tick / 1200) + ":" + (tick % 1200) / 20));
        else
            Objects.requireNonNull(Bukkit.getPlayer("Xigmatic")).spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%,.2f", tick / 20.0)));
    }


    public void run() {
        super.run();

        tickToActionbarTime(getTimeLeft());
    }

}
