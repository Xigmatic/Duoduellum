package xigmatic.me.dogfight;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.TextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xigmatic.me.dogfight.scoreboard.TeamManager;
import xigmatic.me.dogfight.scoreboard.TourneyTeam;

import java.util.ArrayList;
import java.util.Arrays;

public class ChatManager implements Listener {
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


    public static void updateTablist(Player player) {
        StringBuilder tablistString = new StringBuilder(ChatColor.BLUE + " " + ChatColor.BOLD + "Duoduellum");
        tablistString.append(ChatColor.WHITE).append("\nbeh");

        // Adds all teams and their respective players in the tablist
        for(int i = 0; i < 5; i++) {
            TourneyTeam team = TeamManager.getAllTeams()[i];
            // Team name line
            String team1String = "|\n" + ChatColor.WHITE + team.getLogoChar() + " " + ChatColor.BOLD + team.getTeamName() + ChatColor.RESET;

            // Creates the builder that will eventually build the line for both players
            StringBuilder playerNamesLine = new StringBuilder(team.getChatColor().toString());

            // Variables to determine how many pixels need to be added on the left and right
            int player1PixelLength = 0;
            int player2PixelLength = 0;

            // Checks and sums pixel length for player 1
            for(char letter : team.getPlayer1().toCharArray()) {
                // 1 pixel
                if(letter == 'i')
                    player1PixelLength++;
                // 2 pixels
                else if(letter == 'l')
                    player1PixelLength += 2;
                // 3 pixels
                else if(Arrays.asList('t','I').contains(letter))
                    player1PixelLength += 3;
                // 4 pixels
                else if(Arrays.asList('k','f').contains(letter))
                    player1PixelLength += 4;
                // 5 pixels
                else
                    player1PixelLength += 5;
            }

            // Checks and sums pixel length for player 3
            for(char letter : team.getPlayer2().toCharArray()) {
                // ONE MORE PIXEL FOR EACH LETTER DUE TO SPACING IN-GAME
                // 1 pixel
                if(letter == 'i')
                    player2PixelLength += 2;
                    // 2 pixels
                else if(letter == 'l')
                    player2PixelLength += 3;
                    // 3 pixels
                else if(Arrays.asList('t','I').contains(letter))
                    player2PixelLength += 4;
                    // 4 pixels
                else if(Arrays.asList('k','f').contains(letter))
                    player2PixelLength += 5;
                    // 5 pixels
                else
                    player2PixelLength += 6;
            }

            // Adds Player 1 pixels
            for(int j = 0; j < 100 - player1PixelLength; j++)
                playerNamesLine.append("≡");

            // Adds names and dot separator
            playerNamesLine.append(team.getPlayer1()).append(" ● ").append(team.getPlayer2());

            // Adds Player 2 pixels (ONE LESS PIXEL BECAUSE OF THE ONE AFTER THE LAST LETTER)
            for(int j = 0; j < 99 - player2PixelLength; j++)
                playerNamesLine.append("≡");

            // Adds team line
            tablistString.append(team1String);

            // Adds player line
            tablistString.append("\n").append(playerNamesLine).append("\n");
        }

        player.setPlayerListHeader(tablistString.toString());
    }
}
