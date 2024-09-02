package xigmatic.me.dogfight.connection;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import xigmatic.me.dogfight.NPCManager;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;
import xigmatic.me.dogfight.text.ChatManager;

public class ConnectionHandler implements Listener {
    public ConnectionHandler() {

    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Checks if whoever joined is competing in the tourney
        if(TeamManager.playerIsInTourney(player.getName())) {
            event.setJoinMessage(TeamManager.getChatNameCombo(player.getName()) + ChatColor.WHITE + " has joined the server");
            // Removes offline player representation for all players
            NPCManager.removeNPC(player.getName());
        } else {
            event.setJoinMessage(ChatColor.DARK_GRAY + player.getName() + " has joined the server as a spectator");
            ChatManager.sendActionbar(player.getName(), "" + ChatColor.DARK_RED + ChatColor.UNDERLINE + "ʏᴏᴜ ʜᴀᴠᴇ ʙᴇᴇɴ ᴘᴜᴛ ɪɴ ꜱᴘᴇᴄᴛᴀᴛᴏʀ ᴍᴏᴅᴇ ʙᴇᴄᴀᴜꜱᴇ ʏᴏᴜ ᴀʀᴇ ɴᴏᴛ ʀᴇɢɪꜱᴛᴇʀᴇᴅ ꜰᴏʀ ᴛʜᴇ ᴛᴏᴜʀɴᴇʏ",20 );
            player.setGameMode(GameMode.SPECTATOR);
        }


        // If a player joins in non-spectator mode, they will be set adventure by default
        if(player.getGameMode() != GameMode.SPECTATOR)
            player.setGameMode(GameMode.ADVENTURE);

        // Updates tab-list header for the player
        ChatManager.updateTablistHeader(player);

        // Spawns all existing NPCs for the player
        NPCManager.spawnAllNPCs(player);

        // Re-adds dead players (for some reason teams aren't saved so I have to re-add them)
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            for(String playerName : team.getDeadTeam().getEntries()) {
                team.getDeadTeam().addEntry(playerName);
            }
        }
      }
}