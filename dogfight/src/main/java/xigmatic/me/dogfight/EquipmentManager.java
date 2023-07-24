package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.inventory.ItemStack;

public class EquipmentManager implements CommandExecutor, Listener {
    public EquipmentManager() {

    }


    @EventHandler
    public void onShot(EntityShootBowEvent event) {
        event.getProjectile().setVelocity(event.getProjectile().getVelocity().subtract(event.getEntity().getVelocity()));
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("testRider")) {
            Bukkit.getPlayer("Aristratus").addPassenger(Bukkit.getPlayer("Xigmatic"));
        }

        return false;
    }
}
