package xigmatic.me.dogfight.text;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.TextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xigmatic.me.dogfight.Dogfight;
import xigmatic.me.dogfight.scoreboard.TeamManager;

import java.util.HashMap;

public class ChatManager implements Listener {
    // No shadow text color [RGB: (227, 255, 234)]
    public static final String NO_SHADOW_COLOR = customRGBToChatString(227, 255, 234);
    public static int actionbarTaskID = -1;
    public static HashMap<String, Object[]> playerTimeLeftMap = new HashMap<>();

    public ChatManager() {}


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
        return BaseComponent.toLegacyText(gson.fromJson("{text:\"\",color:\"#" + rgbString + "\"}",
                TextComponent.class));
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
            String team1String = "\n" + ChatColor.WHITE + team.getLogoChar() + " " + ChatColor.BOLD +
            team.getTeamName() + ChatColor.RESET;

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


    public static void sendActionbar(String playerName, String message, int duration) {
        if(actionbarTaskID == -1) {
            actionbarTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Dogfight.getPlugin(Dogfight.class), () -> {
                for(String tempPlayerName : playerTimeLeftMap.keySet()) {
                    if(Bukkit.getPlayer(tempPlayerName) != null) {
                        Bukkit.getPlayer(tempPlayerName).spigot().sendMessage(ChatMessageType.ACTION_BAR,
                                new TextComponent((String) playerTimeLeftMap.get(tempPlayerName)[0]));

                        // Decrements the time left on the actionbar by 1 tick
                        if((int) playerTimeLeftMap.get(tempPlayerName)[1] > 0)
                            playerTimeLeftMap.replace(tempPlayerName, new Object[] {playerTimeLeftMap.get(tempPlayerName)[0],
                                    (int) playerTimeLeftMap.get(tempPlayerName)[1] - 1});

                        // Removes actionbar string-player relationship
                        else
                            playerTimeLeftMap.remove(tempPlayerName);


                    }
                }
            }, 1, 0);
        }

        // Adds new entry to HashMap
        playerTimeLeftMap.put(playerName, new Object[] {message, duration});
    }


    @EventHandler
    public void onChatMessage(AsyncPlayerChatEvent event) {
        if(TeamManager.playerIsInTourney(event.getPlayer().getName()))
            event.setFormat(TeamManager.getChatNameCombo(event.getPlayer().getName()) +
                    customRGBToChatString(180,180,180) + " ▶ " + ChatColor.WHITE + "%2$s");

        else
            event.setFormat(customRGBToChatString(140,140,140) + ChatColor.ITALIC +
                    event.getPlayer().getName() + customRGBToChatString(90,90,90) + " ▶ " +
                    customRGBToChatString(140,140,140) + ChatColor.ITALIC + "%2$s");
    }
}