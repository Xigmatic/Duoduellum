package xigmatic.me.dogfight.tasks.gameplay;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;
import org.checkerframework.checker.units.qual.A;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.Dogfight;
import xigmatic.me.dogfight.NPCManager;
import xigmatic.me.dogfight.scoreboard.JsonHandler;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerLifeManager implements Listener {
    public static ArrayList<String> alivePlayers;
    private static HashMap<String, Integer> playerLivesMap;
    private int maxHealth;


    /**
     * Creates new PlayerLifeManager, which is necessary for keeping track of players still alive in a round
     */
    public PlayerLifeManager(int maxHealth) {
        this.maxHealth = maxHealth;

        // Creates and fills the list of alive players with all players on a team
        alivePlayers = new ArrayList<>();
        playerLivesMap = new HashMap<>();
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            alivePlayers.add(team.getPlayer1());
            alivePlayers.add(team.getPlayer2());
            playerLivesMap.put(team.getPlayer1(), this.maxHealth);
            playerLivesMap.put(team.getPlayer2(), this.maxHealth);
        }
    }


    /**
     * Refills all player health in the hash map
     */
    public void resetManager(int maxHealth) {
        this.maxHealth = maxHealth;

        // Creates and fills the list of alive players with all players on a team
        alivePlayers = new ArrayList<>();
        playerLivesMap = new HashMap<>();
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            alivePlayers.add(team.getPlayer1());
            alivePlayers.add(team.getPlayer2());
            playerLivesMap.put(team.getPlayer1(), this.maxHealth);
            playerLivesMap.put(team.getPlayer2(), this.maxHealth);
        }
    }


    /**
     * Sets all player's health to the max health
     */
    public void setPlayerHealth() {
        for(Player player : TeamManager.getAllPlayersAsEntity()) {
            // Sets every player's health to the max number of hearts
            player.setHealthScale(maxHealth * 2.0);
            player.setHealth(20.0);
        }
    }


    /**
     * Returns the list of all player names still around in the round
     * @return List of player names
     */
    public static ArrayList<String> getAlivePlayers() {
        return alivePlayers;
    }


    /**
     * Gets all players (as entities) that are alive in the round
     * @return List of player entities that are alive in the round
     */
    public static ArrayList<Player> getAlivePlayersAsEntities() {
        ArrayList<Player> result = new ArrayList<>();

        for(String playerName : alivePlayers) {
            if(Bukkit.getPlayerExact(playerName) != null) {
                result.add(Bukkit.getPlayerExact(playerName));
            }
        }

        return result;
    }


    /**
     * Returns the max number of hearts the players will have above their hotbar
     * @return Max number of hearts above hotbar
     */
    public int getMaxHealth() {
        return this.maxHealth;
    }


    /**
     * Corrects all data when a player has died in the round
     * @param player Player to be killed in the round
     */
    public static void killPlayer(Player player) {
        // Removes player from alivePlayers list
        alivePlayers.remove(player.getName());

        // Sets player's lives to zero if not there already
        playerLivesMap.replace(player.getName(), 0);

        // Kills player
        player.setHealth(0.0);

        // Adds dead NPC representation to tablist
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        TeamManager.getTeamFromPlayerName(player.getName()).addToDeadTeam(player.getName().substring(0,3) + "[" + alivePlayers.size() + "]");
        Property skinProperty = (Property) serverPlayer.getGameProfile().getProperties().asMap().get("textures").toArray()[0];
        NPCManager.addNPC(player.getName().substring(0,3) + "[" + alivePlayers.size() + "]", skinProperty.getValue(), skinProperty.getSignature());

        player.sendMessage("beh");
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
        if (playerLivesMap.get(hitPlayerName) == 1) {
            // Removes all alive attributes of player
            killPlayer((Player) event.getHitEntity());

            return;
        }

        // Subtracts one life from the player and updates their health bar
        playerLivesMap.replace(hitPlayerName, playerLivesMap.get(hitPlayerName) - 1);
        ((Player) event.getHitEntity()).setHealth(playerLivesMap.get(hitPlayerName) * 20.0 / this.maxHealth);
    }


    @EventHandler
    public void onPlayerRespawnAsSpectator(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        // Checks if the player is in the tournament or not
        if(!TeamManager.playerIsInTourney(player.getName()) || player.getGameMode() != GameMode.SPECTATOR)
            return;

        player.sendTitle("You lost oh nawr!", "this is a subtitle", 5, 60, 10);
    }
}