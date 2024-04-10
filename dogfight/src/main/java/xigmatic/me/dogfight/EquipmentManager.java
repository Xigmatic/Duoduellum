package xigmatic.me.dogfight;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;
import org.bukkit.inventory.meta.FireworkMeta;

public class EquipmentManager implements CommandExecutor, Listener {
    public EquipmentManager() {

    }


    @EventHandler
    public void onArrowShoot(ProjectileLaunchEvent event) {
        // Check if entity is an arrow ends execution if test fails
        if(event.getEntity().getType() != EntityType.ARROW)
            return;

        // Gets the player who shot the arrow
        Player player = (Player) event.getEntity().getShooter();

        // Runs the shot before full
        if(Math.round(player.getExp() * 100) == 95)
            // Sets it to almost full so the bar looks full without switching to next level
            player.setExp(0.99f);
        // Runs when full
        else if(Math.round(player.getExp() * 100) >= 99) {
            // Resets the xp
            player.setExp(0.0f);

            // Automatically loads a firework into the player's crossbow
            if(player.getInventory().getItem(0) != null && player.getInventory().getItem(0).getType() == Material.CROSSBOW) {
                // Creates firework item
                ItemStack firework = new ItemStack(Material.FIREWORK_ROCKET);

                // Creates the firework effect (red to gray large ball with trail)
                FireworkEffect effect = FireworkEffect.builder().withColor(Color.RED).withFade(Color.GRAY).trail(true).with(FireworkEffect.Type.BALL_LARGE).build();

                // Creates meta and applies it to item
                FireworkMeta fireworkMeta = (FireworkMeta) firework.getItemMeta();
                fireworkMeta.addEffect(effect);
                fireworkMeta.setPower(2);
                firework.setItemMeta(fireworkMeta);

                // Loads crossbow with firework
                ((CrossbowMeta) player.getInventory().getItem(0).getItemMeta()).addChargedProjectile(firework);
            }
        // Runs all shots before the last two
        } else
            // Increments the xp bar by 1/20
            player.setExp(player.getExp() + (1.0f/20));
    }


    @EventHandler
    public void onArrowLand(ProjectileHitEvent event) {
        // Checks if entity is an arrow and ends execution if test fails
        if(event.getEntity().getType() != EntityType.ARROW)
            return;

        // Gets location
        Location loc = event.getEntity().getLocation();

        // Necessary data
        Entity arrow = event.getEntity();
        World world = loc.getWorld();

        // Creates explosion
        assert world != null;
        world.spawnParticle(Particle.FIREWORKS_SPARK, loc, 5,0,0,0,0.1);

        if(event.getHitEntity() != null)
            GlowManager.setGlow((Player) event.getEntity().getShooter(), event.getHitEntity(), ((Player) event.getEntity().getShooter()).isSneaking());

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
