package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class RoleManager {
    private final Plugin plugin;
    private final HashMap<String, DogfightRole> roleMap;
    public RoleManager(Plugin plugin) {
        this.plugin = plugin;
        this.roleMap = new HashMap<>();

        // Sets hashMap with all players
        // PLAYERS WILL NOT HAVE A ROLE ASSIGNED
        for(String player : TeamManager.getAllPlayers()) {
            roleMap.put(player, null);
        }
    }


    /**
     * Sets specified player's role to sniper in the hashmap
     * @param playerName Player to change role (by name)
     */
    public void setSniper(String playerName) {
        roleMap.replace(playerName, DogfightRole.SNIPER);
        Bukkit.broadcastMessage(playerName + " is now the Sniper");
    }


    /**
     * Sets specified player's role to glider in the hashmap
     * @param playerName Player to change role (by name)
     */
    public void setGlider(String playerName) {
        this.roleMap.replace(playerName, DogfightRole.GLIDER);
        Bukkit.broadcastMessage(playerName + " is now the Glider");
    }


    /**
     * Returns the DogfightRole of the player specified
     * @param playerName Name of player
     * @return The role that is assigned to the player
     */
    public DogfightRole getPlayerRole(String playerName) {
        return this.roleMap.get(playerName);
    }


    /**
     * Automatically fills the role for all unselected players and adds a "[G] or [S]" tag next to the player's name
     */
    public void autofillRoles() {
       for(TourneyTeam team : TeamManager.getAllTeams()) {
           // Sets all players without a selected role to sniper (player1) or glider (player2)
           if(roleMap.get(team.getPlayer1()) == null)
               roleMap.replace(team.getPlayer1(), DogfightRole.SNIPER);
           if(roleMap.get(team.getPlayer2()) == null)
               roleMap.replace(team.getPlayer2(), DogfightRole.GLIDER);

           try {
               // Player 1 Display Name Change
               if (roleMap.get(team.getPlayer1()) == DogfightRole.SNIPER) {
                   Bukkit.getPlayer(team.getPlayer1()).setDisplayName(team.getPlayer1() + "[S]");
                   Bukkit.getPlayer(team.getPlayer1()).setPlayerListName(team.getPlayer1() + "[S]");
               } else {
                   Bukkit.getPlayer(team.getPlayer1()).setDisplayName(team.getPlayer1() + "[G]");
                   Bukkit.getPlayer(team.getPlayer1()).setPlayerListName(team.getPlayer1() + "[G]");
               }
           } catch (Exception ignored) {}

           try {
               // Player 1 Display Name Change
               if (roleMap.get(team.getPlayer2()) == DogfightRole.SNIPER) {
                   Bukkit.getPlayer(team.getPlayer2()).setDisplayName(team.getPlayer2() + "[S]");
                   Bukkit.getPlayer(team.getPlayer2()).setPlayerListName(team.getPlayer2() + "[S]");
               } else {
                   Bukkit.getPlayer(team.getPlayer2()).setDisplayName(team.getPlayer2() + "[G]");
                   Bukkit.getPlayer(team.getPlayer2()).setPlayerListName(team.getPlayer2() + "[G]");
               }
           } catch (Exception ignored) {}

       }
    }


    /**
     * Gives necessary equipment for the "sniper" role to the player specified
     * @param player Player to receive equipment
     */
    private void giveSniper(Player player) {
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);
        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);
        crossbow.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

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
        for(String playerName : roleMap.keySet()) {
            if(roleMap.get(playerName) == DogfightRole.SNIPER) {
                giveSniper(Bukkit.getPlayer(playerName));
            } else {
                giveGlider(Bukkit.getPlayer(playerName));
            }
        }
    }
}
