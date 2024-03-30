package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.camera.CameraFunctions;
import xigmatic.me.dogfight.inventory.InventoryManager;
import xigmatic.me.dogfight.scoreboard.*;
import xigmatic.me.dogfight.tasks.gameplay.PlayerLifeManager;

import java.io.IOException;

public final class Dogfight extends JavaPlugin {

    @Override
    public void onEnable() {
        // Startup Message
        Bukkit.getConsoleSender().sendMessage("DOGFIGHT PLUGIN IS NOW ENABLED");

        // ScoreManager Setup
        ScoreManager scoreManager;
        scoreManager = new ScoreManager();
        this.getCommand("getScore").setExecutor(scoreManager);

        // JsonHandler Setup
        JsonHandler jsonHandler;

        // Resets Score file to match teamList
        try {
            jsonHandler = new JsonHandler();

            jsonHandler.resetFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Writes whatever scores are in the json file to the map in the ScoreManager
        jsonHandler.updatePlayerPointHashMap(scoreManager.getPlayerPointMap());

        // Test to check if changing/writing scores works correctly
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

        // PlayerLifeManager Setup
        PlayerLifeManager playerLifeManager = new PlayerLifeManager(3);
        getServer().getPluginManager().registerEvents(playerLifeManager, this);

        // Game Manager Setup
        GameManager gameManager = new GameManager(this, scoreManager, inventoryManager, roleManager, playerLifeManager);
        this.getCommand("testSchedule").setExecutor(gameManager);
        this.getCommand("pauseSchedule").setExecutor(gameManager);
        this.getCommand("resumeSchedule").setExecutor(gameManager);

        // ChatManager Setup
        ChatManager chatManager = new ChatManager();
        getServer().getPluginManager().registerEvents(chatManager, this);


        // Prints the working directory (where server root directory of plugin is found)
        Bukkit.getConsoleSender().sendMessage("CURRENTLY RUNNING IN --> " + System.getProperty("user.dir"));

        // Tests CameraFunctions
        //CameraFunctions.linearPan(TeamManager.getAllPlayersAsEntity(), new Location(Bukkit.getPlayer("Xigmatic").getWorld(), 10,-55,0, -90, 14), new Location(Bukkit.getPlayer("Xigmatic").getWorld(), 0,-55,0, -90, 0), 20);
        /*
        CameraFunctions.radialPan(TeamManager.getAllPlayersAsEntity(),
                new Location(Bukkit.getPlayer("Xigmatic").getWorld(), 10,-55,0),
                50, 0, 90, true, 0, -30, 0 ,0, 20);
        */

        // Tests tablist update
        for(Player player : Bukkit.getOnlinePlayers())
            ChatManager.updateTablist(player);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
