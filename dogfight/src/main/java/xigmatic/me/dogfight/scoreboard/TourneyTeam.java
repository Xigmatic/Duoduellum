package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Team;
import xigmatic.me.dogfight.text.ChatManager;

import java.util.UUID;

public class TourneyTeam {
    // Team Info
    private final String teamName;

    // Player Info
    private final String p1;
    private final String p2;
    private UUID p1ID;
    private UUID p2ID;
    private final Color color;
    private final ChatColor chatColor;
    private final String logoChar;
    private final Team scoreboardTeam;


    /**
     * Creates a new duo with a team name and both players
     */
    public TourneyTeam(String teamName, String player1, String player2, String color, String logoChar) {
        this.teamName = teamName;
        this.p1 = player1;
        this.p2 = player2;
        this.logoChar = logoChar;

        // Sets team color
        switch(color) {
            case "red":
                this.color = Color.RED;
                this.chatColor = ChatColor.RED;
                break;

            case "orange":
                this.color = Color.ORANGE;
                this.chatColor = ChatColor.GOLD;
                break;

            case "yellow":
                this.color = Color.YELLOW;
                this.chatColor = ChatColor.YELLOW;
                break;

            case "lime":
                this.color = Color.LIME;
                this.chatColor = ChatColor.GREEN;
                break;

            case "green":
                this.color = Color.GREEN;
                this.chatColor = ChatColor.DARK_GREEN;
                break;

            case "light_blue":
                this.color = Color.AQUA;
                this.chatColor = ChatColor.AQUA;
                break;

            case "cyan":
                this.color = Color.TEAL;
                this.chatColor = ChatColor.DARK_AQUA;
                break;

            case "blue":
                this.color = Color.BLUE;
                this.chatColor = ChatColor.DARK_BLUE;
                break;

            case "purple":
                this.color = Color.PURPLE;
                this.chatColor = ChatColor.DARK_PURPLE;
                break;

            case "pink":
                this.color = Color.FUCHSIA;
                this.chatColor = ChatColor.LIGHT_PURPLE;
                break;

            case "white":
                this.color = Color.WHITE;
                this.chatColor = ChatColor.WHITE;
                break;

            case "gray":
                this.color = Color.GRAY;
                this.chatColor = ChatColor.DARK_GRAY;
                break;

            case "black":
                this.color = Color.BLACK;
                this.chatColor = ChatColor.BLACK;
                break;

            default:
                this.color = Color.WHITE;
                this.chatColor = ChatColor.WHITE;
                break;
        }

        // Removes any existing team with the passed team name to prepare for creating a new one
        try {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName.substring(0,teamName.length()) + "c").unregister();
        } catch(Exception ignore) {
            // Confirms that no team had the team name
            Bukkit.getConsoleSender().sendMessage("No scoreboard team conflict with " + teamName + "c");
        }
        try {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName + "b").unregister();
        } catch(Exception ignore) {
            // Confirms that no team had the team name
            Bukkit.getConsoleSender().sendMessage("No scoreboard team conflict with " + teamName + "b");
        }
        try {
            Bukkit.getScoreboardManager().getMainScoreboard().getTeam(teamName.substring(0,teamName.length()) + "a").unregister();
        } catch(Exception ignore) {
            // Confirms that no team had the team name
            Bukkit.getConsoleSender().sendMessage("No scoreboard team conflict with " + teamName + "a");
        }



        // Creates a team on the in-game scoreboard (ONLY FOR COLORS)
        this.scoreboardTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName + "c");
        this.scoreboardTeam.setColor(chatColor);
        this.scoreboardTeam.setPrefix(this.chatColor + "●" + ChatColor.WHITE + " ");
        this.scoreboardTeam.addEntry(player1);
        this.scoreboardTeam.addEntry(player2);

        // Creates Tab-list team entry that displays the team name only
        Team tablistTeamName = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName + "b");
        tablistTeamName.setColor(ChatColor.GRAY);
        tablistTeamName.setPrefix("⑿" + ChatColor.WHITE + ChatColor.ITALIC + ChatColor.BOLD + teamName);
        tablistTeamName.addEntry(teamName);

        // Creates Tab-list team entry that diplays the logo only
        Team tablistTeamLogo = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(teamName + "a");
        tablistTeamLogo.setColor(ChatColor.GRAY);
        tablistTeamLogo.setPrefix(ChatColor.WHITE + "⓿⓿⓿⓿⓿⓿⓿⓿⓿" + logoChar);
        tablistTeamLogo.addEntry("_" + teamName);

        // Sets the player's chat name to the formatted one (dot logo name)
        //Bukkit.getPlayer(player1).
    }


    /**
     * Returns team name as a string
     * @return Team name
     */
    public String getTeamName() {
        return this.teamName;
    }


    /**
     * Returns player1 name as a string
     * @return Team name
     */
    public String getPlayer1() {
        return this.p1;
    }


    /**
     * Returns player 2 name as a string
     * @return Team name
     */
    public String getPlayer2() {
        return this.p2;
    }


    /**
     * Returns color as org.bukkit.Color
     * @return Color
     */
    public Color getColor() {
        return this.color;
    }


    /**
     * Returns color as a ChatColor
     * @return Chat color
     */
    public ChatColor getChatColor() {
        return this.chatColor;
    }


    /**
     * Returns logo character
     * @return Logo character
     */
    public String getLogoChar() {
        return this.logoChar;
    }


    /**
     * Returns the String of team prefix
     * @return Team prefix String
     */
    public String getTeamPrefix() {
        return this.scoreboardTeam.getPrefix();
    }


    /**
     * Returns a boolean value stating if player 1 is online
     * @return If player 1 is online
     */
    public boolean isPlayer1Online() {
        return Bukkit.getPlayer(this.p1) != null;
    }


    /**
     * Returns a boolean value stating if player 2 is online
     * @return If player 2 is online
     */
    public boolean isPlayer2Online() {

        return Bukkit.getPlayer(this.p2) != null;
    }


    /**
     * Diagnostic method to test how chat colors look in-game
     */
    public static void testChatColors() {
        Bukkit.broadcastMessage(ChatColor.RED + "This is red");
        Bukkit.broadcastMessage(ChatColor.GOLD + "This is orange");
        Bukkit.broadcastMessage(ChatColor.YELLOW + "This is yellow");
        Bukkit.broadcastMessage(ChatColor.GREEN + "This is lime");
        Bukkit.broadcastMessage(ChatColor.DARK_GREEN + "This is green");
        Bukkit.broadcastMessage(ChatColor.AQUA + "This is light blue");
        Bukkit.broadcastMessage(ChatColor.DARK_AQUA + "This is cyan");
        Bukkit.broadcastMessage(ChatColor.DARK_BLUE + "This is blue");
        Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "This is purple");
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "This is pink");
        Bukkit.broadcastMessage(ChatColor.WHITE + "This is white");
        Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "This is gray");
        Bukkit.broadcastMessage(ChatColor.BLACK + "This is black");
    }
}
