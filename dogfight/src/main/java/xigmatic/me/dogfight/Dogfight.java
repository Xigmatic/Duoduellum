package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import xigmatic.me.dogfight.inventory.InventoryManager;

public final class Dogfight extends JavaPlugin {

    @Override
    public void onEnable() {
        // Startup Message
        Bukkit.getConsoleSender().sendMessage("DOGFIGHT PLUGIN IS NOW ENABLED");

        // EquipmentManager
        EquipmentManager equipmentManager = new EquipmentManager();
        getServer().getPluginManager().registerEvents(equipmentManager, this);
        this.getCommand("testRider").setExecutor(equipmentManager);

        // RoleManager
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

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
