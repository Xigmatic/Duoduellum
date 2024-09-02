package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

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
       }
    }


    /**
     * Gives necessary equipment for the "sniper" role to the player specified
     * @param player Player to receive equipment
     */
    private void giveSniper(Player player) {
        // Creates the base crossbow item
        ItemStack crossbow = new ItemStack(Material.CROSSBOW);

        // Adds quick charge V
        crossbow.addUnsafeEnchantment(Enchantment.QUICK_CHARGE, 5);

        // Hides any enchantment data (both appearance and text)
        crossbow.getItemMeta().addItemFlags(ItemFlag.HIDE_ENCHANTS);

        // Gives player final item
        player.getInventory().setItem(0, crossbow);
    }


    /**
     * Gives necessary equipment for the "sniper" role to the player specified
     * @param player Player to receive equipment
     */
    private void giveGlider(Player player) {
        // Creates the base elytra item
        ItemStack elytra = new ItemStack(Material.ELYTRA);

        // Gives player final item
        player.getInventory().setChestplate(elytra);
    }


    /**
     * Gives equipment to all players based on the role found in the roleMap
     */
    public void distributeEquipment() {
        for(String playerName : roleMap.keySet()) {
            // Needs a try in case a player is offline
            try {
                if (roleMap.get(playerName) == DogfightRole.SNIPER) {
                    // Cannot be null
                    giveSniper(Bukkit.getPlayer(playerName));
                } else {
                    // Cannot be null
                    giveGlider(Bukkit.getPlayer(playerName));
                }
            } catch (Exception ignored) {
                // Checks if either player is offline and sends a message accordingly
                Bukkit.getConsoleSender().sendMessage("Could not give equipment to " + playerName +
                        " because they are not online");
            }
        }
    }


    public void mountPlayerToPlayer() {
        for(String playerName : roleMap.keySet()) {
            // Needs a try in case a player is offline
            try {
                if (roleMap.get(playerName) == DogfightRole.GLIDER) {
                    // Spawns a bee at the player's feet that the shooting player will be bound to
                    Entity sniperSeat = Bukkit.getPlayer(playerName).getWorld().spawnEntity(Bukkit.
                            getPlayer(playerName).getLocation(), EntityType.BEE);

                    // Silences the bee entity on top of the flying player
                    sniperSeat.setSilent(true);

                    // Makes the seat bee invisible
                    ((LivingEntity) sniperSeat).setInvisible(true);

                    // Binds the shooting player on top of the bee
                    sniperSeat.addPassenger(Bukkit.getPlayer(TeamManager.getOtherPlayerOfTeam(playerName)));

                    // Binds the bee to the flying player
                    Bukkit.getPlayer(playerName).addPassenger(sniperSeat);
                }
            } catch(Exception ignored) {
                // Checks if either player is offline and sends a message accordingly
                Bukkit.getConsoleSender().sendMessage("Could not mount " + TeamManager.
                        getOtherPlayerOfTeam(playerName) + " because either they or " + playerName + " are not online");
            }
        }
    }
}
