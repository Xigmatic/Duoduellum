package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import xigmatic.me.dogfight.inventory.InventoryManager;
import xigmatic.me.dogfight.scoreboard.RoleManager;
import xigmatic.me.dogfight.tasks.CountdownTask;
import xigmatic.me.dogfight.tasks.DisplayCountdownTask;

public class GameManager implements CommandExecutor {
    private GameState gameState;
    private Plugin plugin;
    private InventoryManager inventoryManager;
    private RoleManager roleManager;
    /**
     * Creates new GameManager (Not singleton)
     */
    public GameManager(Dogfight plugin, InventoryManager inventoryManager, RoleManager roleManager) {
        this.plugin = plugin;
        this.inventoryManager = inventoryManager;
        this.roleManager = roleManager;
        this.gameState = GameState.SELECTING1;

        // Pairs inventoryManager's gameState
        this.inventoryManager.setGameState(this.gameState);
    }


    /**
     * Tests timing between events (Timer is displayed on actionbar to check accurate timing)
     */
    private void testSchedule() {
        nextGameState();
    }


    /**
     * Pauses current schedule by cancelling the current task
     */
    private boolean pauseSchedule() {
        if(currentTask != null && this.currentTask.pause()) {
            return true;
        }
        return false;
    }


    /**
     *
     */
    private boolean resumeSchedule() {
        if(currentTask != null && this.currentTask.resume()) {
            return true;
        }
        return false;
    }


    /**
     * Changes gameState variable
     * @param newGameState New game section to transition to
     */
    private void changeGameState(GameState newGameState) {
        this.gameState = newGameState;
    }


    private CountdownTask currentTask;
    /**
     * Sets the current task running as well as the current runnable being run
     * @param runnable This will be run every in-game tick
     */
    private void setTask(CountdownTask runnable) {
        this.currentTask = runnable;
        runnable.start();
    }


    /**
     * Changes gameState to the next sequential state (found in GameState enum) and executes necessary actions when changing states
     */
    private void nextGameState() {
        switch(gameState) {
            default:
                break;
            case PENDING:
                // WAITING1 Timer
                setTask(new CountdownTask(() -> {
                    Bukkit.getPlayer("Xigmatic").sendMessage("erm");
                    nextGameState();
                },5));

                changeGameState(GameState.WAITING1);
                break;
            case WAITING1:
                // SELECTING1 Timer
                setTask(new DisplayCountdownTask(() -> {
                    Bukkit.getPlayer("Xigmatic").sendMessage("erm");
                },10));

                changeGameState(GameState.SELECTING1);
                break;
            case SELECTING1:



                changeGameState(GameState.PREROUND1);
                break;
            case PREROUND1:



                changeGameState(GameState.ROUND1);
                break;
            case ROUND1:



                changeGameState(GameState.POSTROUND1);
                break;
            case POSTROUND1:



                changeGameState(GameState.WAITING2);
                break;
            case WAITING2:



                changeGameState(GameState.SELECTING2);
                break;
            case SELECTING2:



                changeGameState(GameState.PREROUND2);
                break;
            case PREROUND2:



                changeGameState(GameState.ROUND2);
                break;
            case ROUND2:



                changeGameState(GameState.POSTROUND2);
                break;
            case POSTROUND2:



                changeGameState(GameState.FINISH);
                break;
        }

        // Sets InventoryManager's gameState to the current one (parity)
        this.inventoryManager.setGameState(this.gameState);
    }


    // Commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // testSchedule
        if(label.equalsIgnoreCase("testSchedule")) {
            testSchedule();

            return true;
        }

        // pauseSchedule
        if(label.equalsIgnoreCase("pauseSchedule")) {
            if (!pauseSchedule())
                sender.sendMessage("No schedule is currently running or is already paused");
        }

        // resumeSchedule
        if(label.equalsIgnoreCase("resumeSchedule")) {
            if (!resumeSchedule())
                sender.sendMessage("No schedule is currently paused");
        }

        return false;
    }
}
