package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class Dogfight extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getConsoleSender().sendMessage("DOGFIGHT PLUGIN IS NOW ENABLED");

        GameManager gm = new GameManager();

        gm.testSchedule();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
