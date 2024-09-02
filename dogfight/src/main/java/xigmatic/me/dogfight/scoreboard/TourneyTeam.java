package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
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
    private final Team deadTeam;


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

            case "black":
                this.color = Color.BLACK;
                this.chatColor = ChatColor.BLACK;
                break;

            default:
                this.color = Color.WHITE;
                this.chatColor = ChatColor.WHITE;
                break;
        }

        Scoreboard mainScoreboard = Bukkit.getServer().getScoreboardManager().getMainScoreboard();

        if(mainScoreboard.getTeam(teamName + "a") != null)
            mainScoreboard.getTeam(teamName + "a").unregister();
        if(mainScoreboard.getTeam(teamName + "b") != null)
            mainScoreboard.getTeam(teamName + "b").unregister();
        if(mainScoreboard.getTeam(teamName + "c") != null)
            mainScoreboard.getTeam(teamName + "c").unregister();
        if(mainScoreboard.getTeam(teamName + "d") != null)
            mainScoreboard.getTeam(teamName + "d").unregister();
        if(mainScoreboard.getTeam(teamName + "e") != null)
            mainScoreboard.getTeam(teamName + "e").unregister();


        // Creates spacing before team entry
        Team tablistSpacingTeam = mainScoreboard.registerNewTeam(teamName + "e");
        tablistSpacingTeam.setColor(ChatColor.GRAY);
        for(int i = 0; i < 4; i++)
            tablistSpacingTeam.addEntry(i + teamName);

        // Creates Tab-list team entry that diplays the logo only
        Team tablistTeamLogo = mainScoreboard.registerNewTeam(teamName + "a");
        tablistTeamLogo.setColor(ChatColor.GRAY);
        tablistTeamLogo.setPrefix(ChatColor.WHITE + "⓿⓿⓿⓿⓿⓿⓿⓿⓿" + logoChar);
        tablistTeamLogo.addEntry("_" + teamName);

        // Creates Tab-list team entry that displays the team name only
        Team tablistTeamName = mainScoreboard.registerNewTeam(teamName + "b");
        tablistTeamName.setColor(ChatColor.GRAY);
        tablistTeamName.setPrefix("⑿" + ChatColor.WHITE + ChatColor.ITALIC + ChatColor.BOLD + teamName);
        tablistTeamName.addEntry(teamName);

        // Creates a team on the in-game scoreboard (ONLY FOR COLORS)
        this.scoreboardTeam = mainScoreboard.registerNewTeam(teamName + "c");
        this.scoreboardTeam.setColor(chatColor);
        this.scoreboardTeam.setPrefix(this.chatColor + "●" + ChatColor.WHITE + " ");
        this.scoreboardTeam.addEntry(player1);
        this.scoreboardTeam.addEntry(player2);

        this.deadTeam = mainScoreboard.registerNewTeam(teamName + "d");
        this.deadTeam.setColor(ChatColor.DARK_GRAY);
        this.deadTeam.setPrefix(ChatColor.DARK_GRAY + "⑵☠⑵");
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
        return Bukkit.getPlayerExact(this.p1) != null;
    }


    /**
     * Returns a boolean value stating if player 2 is online
     * @return If player 2 is online
     */
    public boolean isPlayer2Online() {
        return Bukkit.getPlayerExact(this.p2) != null;
    }


    /**
     * Adds player to dead team (does not have to be an actual player)
     * @param playerName Player to add to dead team
     */
    public void addToDeadTeam(String playerName) {
        this.deadTeam.addEntry(playerName);
    }


    /**
     * Clears dead team
     */
    public void clearDeadTeam() {
        for(String playerName : this.deadTeam.getEntries())
            this.deadTeam.removeEntry(playerName);
    }


    /**
     * Returns the team of dead players
     * @return Score team of dead players on the team
     */
    public Team getDeadTeam() {
        return this.deadTeam;
    }

}
