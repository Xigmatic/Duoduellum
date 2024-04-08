package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.camera.CameraFunctions;
import xigmatic.me.dogfight.inventory.InventoryManager;
import xigmatic.me.dogfight.scoreboard.*;
import xigmatic.me.dogfight.tasks.CountdownTask;
import xigmatic.me.dogfight.tasks.DisplayCountdownTask;
import xigmatic.me.dogfight.tasks.SelectingTask;
import xigmatic.me.dogfight.tasks.gameplay.PlayerLifeManager;
import xigmatic.me.dogfight.tasks.gameplay.RoundOneTask;

import java.io.IOException;
import java.util.ArrayList;

public class GameManager implements CommandExecutor {
    private GameState gameState;
    private final Plugin plugin;
    private final ScoreManager scoreManager;
    private final InventoryManager inventoryManager;
    private final RoleManager roleManager;
    private CountdownTask currentTask;
    private final PlayerLifeManager playerLifeManager;

    /**
     * Creates new GameManager (Not singleton)
     * Game State is set to PENDING by default
     */
    public GameManager(Dogfight plugin, ScoreManager scoreManager, InventoryManager inventoryManager, RoleManager roleManager, PlayerLifeManager playerLifeManager) {
        this.plugin = plugin;
        this.scoreManager = scoreManager;
        this.inventoryManager = inventoryManager;
        this.roleManager = roleManager;
        this.gameState = GameState.PENDING;
        this.playerLifeManager = playerLifeManager;

        // Pairs inventoryManager's gameState
        this.inventoryManager.setGameState(this.gameState);
    }


    /**
     * Tests timing between events (Timer is displayed on actionbar to check accurate timing)
     */
    private void testSchedule() {
        this.gameState = GameState.PENDING;
        this.nextGameState();
    }


    /**
     * Pauses current schedule by stopping time in the current task
     * @return Returns if any task was paused (null task returns false)
     */
    private boolean pauseSchedule() {
        return currentTask != null && this.currentTask.pause();
    }


    /**
     * Resumes current schedule by resuming time in the current task
     * @return Returns if any task was resumed (null task returns false)
     */
    private boolean resumeSchedule() {
        return currentTask != null && this.currentTask.resume();
    }


    /**
     * Changes gameState variable (DOES NOT RUN TRANSITION CODE)
     * @param newGameState New game section to be changed to
     */
    private void changeGameState(GameState newGameState) {
        this.gameState = newGameState;
    }


    /**
     * Sets the current task running as well as the current runnable being run
     * WILL CANCEL ANY TASK CURRENTLY RUNNING
     * @param runnable Segment of code that will run AFTER the countdown has completed
     */
    private void setTask(CountdownTask runnable) {
        if(this.currentTask != null && !this.currentTask.isCancelled()) this.currentTask.kill();

        this.currentTask = runnable;
        runnable.start();
    }


    /**
     * Advances gameState to the next sequential state (found in GameState enum) and executes the necessary actions defined by each state
     */
    private void nextGameState() {
        switch(gameState) {
            default:
                break;
            case PENDING:
                // WAITING1 timer and transition event
                setTask(new CountdownTask(() -> {
                    Bukkit.getConsoleSender().sendMessage("erm");

                    // Moves to next state after completion
                    this.nextGameState();
                },5));

                changeGameState(GameState.WAITING1);
                break;
            case WAITING1:
                // SELECTING1 timer and transition event
                setTask(new SelectingTask(() -> {
                    Bukkit.broadcastMessage("Done selecting");

                    // Moves to next state after completion
                    this.nextGameState();
                }, 20, inventoryManager));

                // Opens inventory on the first tick of the countdown for all PLAYER ONES
                for(Player player : TeamManager.getAllPlayer1AsEntity()) {
                    inventoryManager.openSelectionScreen(player);
                }

                changeGameState(GameState.SELECTING1);
                break;
            case SELECTING1:
                // PREROUND1 timer and transition event
                changeGameState(GameState.PREROUND1);

                // Fills all empty roles for players
                this.roleManager.autofillRoles();

                // Closes all hopper inventories
                for(Player player : TeamManager.getAllPlayer1AsEntity()) {
                    if(player.getOpenInventory().getType() == InventoryType.HOPPER) {
                        player.closeInventory();
                    }
                }

                // Gives all players necessary equipment
                this.roleManager.distributeEquipment();

                // Mounts one teammate to another
                this.roleManager.mountPlayerToPlayer();

                // Moves to next state after completion
                this.nextGameState();
                break;
            case PREROUND1:
                // ROUND1 timer and transition event
                setTask(new RoundOneTask(() -> {
                    // Moves to next state after completion
                    //this.nextGameState();
                }, 30, new ArrayList<>(Bukkit.getOnlinePlayers())));

                // Sets all player's health
                this.playerLifeManager.setPlayerHealth();

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

        // Sets InventoryManager's gameState to the current one (NECESSARY FOR PARITY)
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
