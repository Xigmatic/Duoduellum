package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.inventory.InventoryManager;
import xigmatic.me.dogfight.scoreboard.JsonHandler;
import xigmatic.me.dogfight.scoreboard.RoleManager;
import xigmatic.me.dogfight.scoreboard.ScoreManager;

import java.io.IOException;

public final class Dogfight extends JavaPlugin {

    @Override
    public void onEnable() {
        // Startup Message
        Bukkit.getConsoleSender().sendMessage("DOGFIGHT PLUGIN IS NOW ENABLED");

        // ScoreManager Setup
        ScoreManager scoreManager;
        try {
            scoreManager = new ScoreManager();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        this.getCommand("getScore").setExecutor(scoreManager);

        // JsonHandler Setup
        JsonHandler jsonHandler;
        try {
            jsonHandler = new JsonHandler();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        //jsonHandler.resetFile();
        jsonHandler.updatePlayerPointHashMap(scoreManager.getPlayerPointMap());
        scoreManager.addPoints("Xigmatic",4);
        jsonHandler.writeScores(scoreManager.getPlayerPointMap());

        // EquipmentManager Setup
        EquipmentManager equipmentManager = new EquipmentManager();
        getServer().getPluginManager().registerEvents(equipmentManager, this);
        this.getCommand("testRider").setExecutor(equipmentManager);

        // RoleManager Setup
        RoleManager roleManager = new RoleManager(this);

        // InventoryManager Setup
        InventoryManager inventoryManager = new InventoryManager(this, roleManager);
        getServer().getPluginManager().registerEvents(inventoryManager, this);
        this.getCommand("openTestInv").setExecutor(inventoryManager);
        this.getCommand("giveEquipment").setExecutor(inventoryManager);

        // Game Manager Setup
        GameManager gameManager = new GameManager(this, inventoryManager, roleManager);
        this.getCommand("testSchedule").setExecutor(gameManager);
        this.getCommand("pauseSchedule").setExecutor(gameManager);
        this.getCommand("resumeSchedule").setExecutor(gameManager);


        // Prints the working directory (where server root directory of plugin is found)
        Bukkit.getConsoleSender().sendMessage("CURRENTLY RUNNING IN --> " + System.getProperty("user.dir"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
