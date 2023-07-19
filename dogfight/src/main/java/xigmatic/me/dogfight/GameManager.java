package xigmatic.me.dogfight;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public class GameManager {
    private GameState gameState;
    private int timerID;
    private int tickNum;
    private int tickMax;
    private final BukkitScheduler bs = Bukkit.getScheduler();


    /**
     * Creates new GameManager (Not singleton)
     */
    public GameManager() {

    }


    /**
     * Tests timing between events (Timer is displayed on actionbar to check accurate timing)
     */
    public void testSchedule() {
        gameState = GameState.WAITING1;
        setTimePeriod(10);
        tickNum = tickMax;

        // Schedules timer task
        timerID = bs.scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {

            // Checks if timer has run out & sets to next GameState
            if(tickNum == 0) {
                nextGameState();
                tickNum = tickMax;
            }

            // Actionbar Display
            try {
                if (tickNum > 1200)
                    Bukkit.getPlayer("Xigmatic").spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent((tickNum / 1200) + ":" + (tickNum % 1200) / 20));
                else
                    Bukkit.getPlayer("Xigmatic").spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(String.format("%,.2f", tickNum / 20.0)));
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            // Decrement
            tickNum--;

        }, 0, 1);
    }


    /**
     * Changes gameState variable
     * @param newGameState New game section to transition to
     */
    private void changeGameState(GameState newGameState) {
        gameState = newGameState;
    }


    /**
     * Sets tickMax to the number of in-game tick from the time given in seconds
     * @param seconds Number of secondsd declared for the current gameState
     */
    private void setTimePeriod(int seconds) {
        tickMax = 20 * seconds;
    }


    /**
     * Changes gameState to the next sequential state (found in GameState enum) and executes necessary actions when changing states
     */
    private void nextGameState() {
        switch(gameState) {
            case WAITING1:


                setTimePeriod(30);
                changeGameState(GameState.SELECTING1);
                break;
            case SELECTING1:


                setTimePeriod(15);
                changeGameState(GameState.PREROUND1);
                break;
            case PREROUND1:


                setTimePeriod(90);
                changeGameState(GameState.ROUND1);
                break;
            case ROUND1:


                setTimePeriod(10);
                changeGameState(GameState.POSTROUND1);
                break;
            case POSTROUND1:


                setTimePeriod(10);
                changeGameState(GameState.WAITING2);
                break;
            case WAITING2:


                setTimePeriod(30);
                changeGameState(GameState.SELECTING2);
                break;
            case SELECTING2:


                setTimePeriod(15);
                changeGameState(GameState.PREROUND2);
                break;
            case PREROUND2:


                setTimePeriod(90);
                changeGameState(GameState.ROUND2);
                break;
            case ROUND2:


                setTimePeriod(10);
                changeGameState(GameState.POSTROUND2);
                break;
            case POSTROUND2:


                setTimePeriod(30);
                changeGameState(GameState.FINISH);
                break;
            default:
                setTimePeriod(0);
                bs.cancelTask(timerID);
                break;
        }
    }
}
