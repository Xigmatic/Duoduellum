package xigmatic.me.dogfight.text;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.authlib.GameProfile;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.TextComponentSerializer;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.server.network.ServerPlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_20_R1.CraftServer;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xigmatic.me.dogfight.NPCManager;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

public class ChatManager implements Listener {
    // No shadow text color [RGB: (227, 255, 234)]
    public static final String NO_SHADOW_COLOR = customRGBToChatString(227, 255, 234);

    public ChatManager() {}


    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {

        event.setFormat(TeamManager.getChatNameCombo(event.getPlayer().getName()) + customRGBToChatString(180,180,180) + " ▶ " + ChatColor.WHITE + "%2$s");
    }


    /**
     * Converts json text into chat-friendly text
     * @param jsonStrings List of /tellraw json strings
     * @return Chat-friendly String
     */
    public static String jsonToChatString(String... jsonStrings) {
        StringBuilder result = new StringBuilder();
        Gson gson = new GsonBuilder().registerTypeAdapter(TextComponent.class, new TextComponentSerializer()).create();

        for(String json : jsonStrings) {
            result.append(BaseComponent.toLegacyText(gson.fromJson(json, TextComponent.class)));

        }

        return result.toString();
    }


    /**
     * Converts and RGB value into Chat-friendly text to make any color available in chat
     * @param r Red value
     * @param g Green value
     * @param b Blue value
     * @return Chat-friendly string that changes the chat color to the specified RGB values
     */
    public static String customRGBToChatString(int r, int g, int b) {
        String rgbString = Integer.toHexString(r) + Integer.toHexString(g) + Integer.toHexString(b);

        Gson gson = new GsonBuilder().registerTypeAdapter(TextComponent.class, new TextComponentSerializer()).create();
        return BaseComponent.toLegacyText(gson.fromJson("{text:\"\",color:\"#" + rgbString + "\"}", TextComponent.class));
    }


    public static void updateTablistHeader(Player player) {

        // Duoduellum Logo
        StringBuilder tablistString = new StringBuilder(NO_SHADOW_COLOR + "\n\n\n\n\n⒟");

        // Subtitle
        tablistString.append(NO_SHADOW_COLOR).append("\nbeh");

        /*
        // Adds all teams and their respective players in the tablist
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            // Team name line
            String team1String = "\n" + ChatColor.WHITE + team.getLogoChar() + " " + ChatColor.BOLD + team.getTeamName() + ChatColor.RESET;

            // Creates the builder that will eventually build the line for both players
            StringBuilder playerNamesLine = new StringBuilder(team.getChatColor().toString());

            // Adds Player 1 name with proper pixel width (100)
            playerNamesLine.append(TextFunctions.forceTextToWidth(team.getPlayer1(), 100, true));

            // Adds dot separator
            playerNamesLine.append(" ◆ ");

            // Adds Player 1 name with proper pixel width (106)
            playerNamesLine.append(TextFunctions.forceTextToWidth(team.getPlayer2(), 106, false));

            // Adds team line
            tablistString.append(team1String);

            // Adds player line
            tablistString.append("\n").append(playerNamesLine).append("\n");
        }
         */

        // Sets tab-list heading for current player
        player.setPlayerListHeader(tablistString.toString());
    }
}
