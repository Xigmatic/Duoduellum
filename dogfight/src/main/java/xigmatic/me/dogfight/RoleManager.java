package xigmatic.me.dogfight;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.management.relation.Role;
import java.util.HashMap;

public class RoleManager {
    private Plugin plugin;
    private HashMap<Player, DogfightRole> roleMap;
    public RoleManager(Plugin plugin) {
        this.plugin = plugin;
        this.roleMap = new HashMap<>();

        // Sets hashMap with all players
        // THE WAY THIS IS INITIALIZED IS TEMPORARY AND IS NOT PERMANENT
        for(Player p : Bukkit.getOnlinePlayers()) {
            roleMap.put(p, DogfightRole.GLIDER);
        }
    }


    /**
     * Sets specified player's role to sniper in the hashmap
     * @param player Player to change role
     */
    public void setSniper(Player player) {
        roleMap.replace(player, DogfightRole.SNIPER);
        player.sendMessage("Role set to Sniper");
    }


    /**
     * Sets specified player's role to glider in the hashmap
     * @param player Player to change role
     */
    public void setGlider(Player player) {
        roleMap.replace(player, DogfightRole.GLIDER);
        player.sendMessage("Role set to Glider");
    }


    /**
     * Gives necessary equipment for the "sniper" role to the player specified
     * @param player Player to receive equipment
     */
    private void giveSniper(Player player) {
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);


        // Gives player final item
        player.getInventory().setItem(0, crossbow);
    }


    /**
     * Gives necessary equipment for the "sniper" role to the player specified
     * @param player Player to receive equipment
     */
    private void giveGlider(Player player) {
        ItemStack elytra = new ItemStack(Material.ELYTRA);


        // Gives player final item
        player.getInventory().setChestplate(elytra);
    }


    /**
     * Gives equipment to all players based on the role found in the roleMap
     */
    public void distributeEquipment() {
        for(Player player : roleMap.keySet()) {
            if(roleMap.get(player) == DogfightRole.SNIPER) {
                giveSniper(player);
            } else {
                giveGlider(player);
            }
        }
    }
}
