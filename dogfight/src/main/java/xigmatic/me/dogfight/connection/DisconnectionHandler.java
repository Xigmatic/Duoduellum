package xigmatic.me.dogfight.connection;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import xigmatic.me.dogfight.NPCManager;

public class DisconnectionHandler implements Listener {
    public DisconnectionHandler() {}


    @EventHandler
    public void onPlayerDisconnect(PlayerQuitEvent event) {
        event.setQuitMessage(event.getPlayer().getDisplayName() + " has disconnected");

        if(event.getPlayer().getGameMode() != GameMode.SPECTATOR)
            // Adds disconnected NPC to map and updates it for all players
            NPCManager.addNPC(event.getPlayer().getName(), NPCManager.DISCONNECTED_TEXTURE, NPCManager.DISCONNECTED_SIGNATURE);
    }
}
