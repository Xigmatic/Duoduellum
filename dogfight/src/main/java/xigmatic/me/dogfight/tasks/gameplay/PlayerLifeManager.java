package xigmatic.me.dogfight.tasks.gameplay;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.plugin.Plugin;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.Dogfight;
import xigmatic.me.dogfight.scoreboard.JsonHandler;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerLifeManager implements Listener {
    private ArrayList<String> alivePlayers;
    private HashMap<String, Integer> playerLivesMap;
    private int maxHealth;


    /**
     * Creates new PlayerLifeManager, which is necessary for keeping track of players still alive in a round
     */
    public PlayerLifeManager(int maxHealth) {
        this.maxHealth = maxHealth;

        // Creates and fills the list of alive players with all players on a team
        this.alivePlayers = new ArrayList<>();
        this.playerLivesMap = new HashMap<>();
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            this.alivePlayers.add(team.getPlayer1());
            this.alivePlayers.add(team.getPlayer2());
            this.playerLivesMap.put(team.getPlayer1(), this.maxHealth);
            this.playerLivesMap.put(team.getPlayer2(), this.maxHealth);
        }
    }


    /**
     * Refills all player health in the hash map
     */
    public void resetManager(int maxHealth) {
        this.maxHealth = maxHealth;

        // Creates and fills the list of alive players with all players on a team
        this.alivePlayers = new ArrayList<>();
        this.playerLivesMap = new HashMap<>();
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            this.alivePlayers.add(team.getPlayer1());
            this.alivePlayers.add(team.getPlayer2());
            this.playerLivesMap.put(team.getPlayer1(), this.maxHealth);
            this.playerLivesMap.put(team.getPlayer2(), this.maxHealth);
        }
    }


    /**
     * Sets all player's health to the max health
     */
    public void setPlayerHealth() {
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            // Sets every player's health to the max number of hearts
            Bukkit.getPlayer(team.getPlayer1()).setHealthScale(maxHealth * 2.0);
            Bukkit.getPlayer(team.getPlayer1()).setHealth(20.0);
            Bukkit.getPlayer(team.getPlayer2()).setHealthScale(maxHealth * 2.0);
            Bukkit.getPlayer(team.getPlayer2()).setHealth(20.0);
        }
    }


    /**
     * Returns the list of all player names still around in the round
     * @return List of player names
     */
    public ArrayList<String> getAlivePlayers() {
        return this.alivePlayers;
    }


    /**
     * Returns the max number of hearts the players will have above their hotbar
     * @return Max number of hearts above hotbar
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }


    @EventHandler
    public void onProjectileLand(ProjectileHitEvent event) {
        // Checks if the
        Bukkit.broadcastMessage("PlayerLifeManager");

        // Checks if an entity was hit rather than a block and if the entity was a player
        if (event.getHitEntity() == null || event.getHitEntity().getType() != EntityType.PLAYER)
            return;

        // Gets hit player's name
        String hitPlayerName = event.getHitEntity().getName();

        // Checks if the player had 1 life left and removes them from the alivePlayers list if they had only 1 left
        if (this.playerLivesMap.get(hitPlayerName) == 1) {
            this.alivePlayers.remove(hitPlayerName);

            // Kills player
            ((Player) event.getHitEntity()).setHealth(0.0);

            return;
        }

        // Subtracts one life from the player and updates their health bar
        this.playerLivesMap.replace(hitPlayerName, this.playerLivesMap.get(hitPlayerName) - 1);
        ((Player) event.getHitEntity()).setHealth(this.playerLivesMap.get(hitPlayerName) * 20.0 / this.maxHealth);
    }
}