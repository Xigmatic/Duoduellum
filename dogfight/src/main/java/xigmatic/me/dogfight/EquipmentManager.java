package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class EquipmentManager implements CommandExecutor, Listener {
    public EquipmentManager() {

    }


    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        // Checks if entity is an arrow and ends code execution if this test fails
        if(event.getEntity().getType() != EntityType.ARROW)
            return;

        // Gets location
        Location loc = event.getEntity().getLocation();

        // Necessary data
        Entity arrow = event.getEntity();
        World world = loc.getWorld();

        // Creates explosion
        world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 5,0,0,0,0.1);

        // Kills entity
        arrow.remove();
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // testRider
        if(label.equalsIgnoreCase("testRider")) {
            Bukkit.getPlayer("Aristratus").addPassenger(Bukkit.getPlayer("Xigmatic"));
        }

        return false;
    }
}
