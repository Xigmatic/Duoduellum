package xigmatic.me.dogfight.connection;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xigmatic.me.dogfight.NPCManager;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.text.ChatManager;

public class ConnectionHandler implements Listener {
    public ConnectionHandler() {

    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        try {
            event.setJoinMessage(TeamManager.getChatNameCombo(event.getPlayer().getName()) + ChatColor.WHITE + " has joined the server");
            // Removes offline player representation for all players
            NPCManager.removeNPC(event.getPlayer().getName());
        } catch(Exception ignored) {
            event.setJoinMessage(ChatColor.DARK_GRAY + event.getPlayer().getName() + " has joined the server as a spectator");
            event.getPlayer().setGameMode(GameMode.SPECTATOR);
        }

        if(event.getPlayer().getGameMode() == GameMode.SPECTATOR)
            event.getPlayer().setGameMode(GameMode.ADVENTURE);

        // Updates tab-list header for the player
        ChatManager.updateTablistHeader(event.getPlayer());

        // Spawns all existing NPCs for the player
        NPCManager.spawnAllNPCs(event.getPlayer());
    }
}
