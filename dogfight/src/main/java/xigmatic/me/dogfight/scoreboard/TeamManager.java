package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;
import org.json.simple.parser.ParseException;
import xigmatic.me.dogfight.text.ChatManager;

import java.io.IOException;
import java.util.ArrayList;

public class TeamManager {
    private static final TourneyTeam[] teams;

    static {
        try {
            teams = JsonHandler.readTeamList();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    ;

    /**
     * Creates TeamManager Object
     */
    public TeamManager() {}


    public static TourneyTeam[] getAllTeams() {
        return teams;
    }


    /**
     * Returns the list of players that are playing in the tourney
     * @return List of the players in the tourney
     */
    public static ArrayList<String> getAllPlayers() {
        ArrayList<String> players = new ArrayList<>();

        for(TourneyTeam team : teams) {
            players.add(team.getPlayer1());
            players.add(team.getPlayer2());
        }

        return players;
    }


    /**
     * Returns the list of all tourney players as Player entities
     * @return All players that are in the tourney as a Player entity
     */
    public static ArrayList<Player> getAllPlayersAsEntity() {
        ArrayList<Player> players = new ArrayList<>();

        for(String player : getAllPlayers()) {
            if(Bukkit.getPlayer(player) != null) {
                players.add(Bukkit.getPlayer(player));
            } else {
                Bukkit.getConsoleSender().sendMessage(player + " is not online. Could not be added to player list in TeamManager.");
            }
        }

        return players;
    }


    /**
     * Returns all player 1 name in the tourney
     * @return List of player 1 names in the tourney
     */
    public static ArrayList<String> getAllPlayer1() {
        ArrayList<String> players = new ArrayList<>();

        for(TourneyTeam team : teams) {
            players.add(team.getPlayer1());
        }

        return players;
    }


    /**
     * Returns all player 2 name in the tourney
     * @return List of player 2 names in the tourney
     */
    public static ArrayList<String> getAllPlayer2() {
        ArrayList<String> players = new ArrayList<>();

        for(TourneyTeam team : teams) {
            players.add(team.getPlayer2());
        }

        return players;
    }


    /**
     * Returns all player 1 in the tourney as Player entities
     * @return List of player 1 in the tourney as Player entities
     */
    public static ArrayList<Player> getAllPlayer1AsEntity() {
        ArrayList<Player> players = new ArrayList<>();

        for(String player : getAllPlayer1()) {
            if(Bukkit.getPlayer(player) != null) {
                players.add(Bukkit.getPlayer(player));
            } else {
                Bukkit.getConsoleSender().sendMessage(player + " is not online. Could not be added to player list in TeamManager.");
            }
        }

        return players;
    }


    /**
     * Returns all player 1 in the tourney as Player entities
     * @return List of player 1 in the tourney as Player entities
     */
    public static ArrayList<Player> getAllPlayer2AsEntity() {
        ArrayList<Player> players = new ArrayList<>();

        for(String player : getAllPlayer2()) {
            if(Bukkit.getPlayer(player) != null) {
                players.add(Bukkit.getPlayer(player));
            } else {
                Bukkit.getConsoleSender().sendMessage(player + " is not online. Could not be added to player list in TeamManager.");
            }
        }

        return players;
    }


    /**
     * Finds the TourneyTeam object when given a team name
     * @param teamName Name of team to find TourneyTeam
     * @return TourneyTeam object
     */
    public static TourneyTeam getTeamFromTeamName(String teamName) {
        // Checks all team names
        for(TourneyTeam team : teams) {
            if(team.getTeamName().equalsIgnoreCase(teamName))
                return team;
        }

        // Throws a NullPointerException stating that no team could be found
        throw new NullPointerException("No team with the team name \"" + teamName + "\" could be found");
    }


    /**
     * Finds the TourneyTeam with given a player's name
     * @param playerName Name of player to find team
     * @return TourneyTeam of specified player
     */
    public static TourneyTeam getTeamFromPlayerName(String playerName) {
        // Checks all player 1s
        for(TourneyTeam team : teams) {
            if(team.getPlayer1().equalsIgnoreCase(playerName))
                return team;
        }

        // Checks all player 2s
        for(TourneyTeam team : teams) {
            if(team.getPlayer2().equalsIgnoreCase(playerName))
                return team;
        }

        // Throws NullPointerException stating that no team could be found
        throw new NullPointerException("No team with the player \"" + playerName + "\" could be found");
    }


    /**
     * Finds the other player's name by using the other team's player name
     * @param playerName Name of player
     * @return The name of the other player on the specified player's team
     */
    public static String getOtherPlayerOfTeam(String playerName) {
        // Creates temporary team variable
        TourneyTeam team = getTeamFromPlayerName(playerName);

        // Returns the opposite player's name
        if(team.getPlayer1().equalsIgnoreCase(playerName))
            return team.getPlayer2();
        else if(team.getPlayer2().equalsIgnoreCase(playerName))
            return team.getPlayer1();

        // Throws a NullPointerException stating that no team could be found
        throw new NullPointerException("No team with the player \"" + playerName + "\" could be found");
    }


    /**
     * Returns the ChatColor of a player's team
     * @param playerName Name of player
     * @return ChatColor of player's team
     */
    public static ChatColor getPlayerChatColor(String playerName) {
        return getTeamFromPlayerName(playerName).getChatColor();
    }


    /**
     * Returns the Color of a player's team
     * @param playerName Name of player
     * @return Color of player's team
     */
    public static Color getPlayerColor(String playerName) {
        return getTeamFromPlayerName(playerName).getColor();
    }


    public static String getPlayerChatString(String playerName) {
        /*
        Gson gson = new GsonBuilder().registerTypeAdapter(TextComponent.class, new TextComponentSerializer()).create();
        TextComponent textComponent = gson.fromJson("{text:\" ① \",color:\"#4e5c24\"}", TextComponent.class);
        String legacy = BaseComponent.toLegacyText(textComponent);
         */

        String logo = ChatManager.jsonToChatString("{text:\" " + getTeamFromPlayerName(playerName).getLogoChar() + " \",color:\"#4e5c24\"}");
        return getPlayerChatColor(playerName) + "●" + logo + ChatColor.WHITE + playerName;
    }


    /**
     * Returns team prefix as a chat-friendly string
     * @param playerName Player of team to get prefix
     * @return Prefix string
     */
    public static String getTeamPrefix(String playerName) {
        return getTeamFromPlayerName(playerName).getTeamPrefix();
    }


    /**
     * Returns team prefix and player name if for whatever reason it doesn't work any other way
     * STRICTLY USED FOR CHAT ONLY
     * @param playerName Player name to be converted
     * @return Full player chat text
     */
    public static String getChatNameCombo(String playerName) {
        return getTeamPrefix(playerName) + getPlayerChatColor(playerName) + playerName;
    }


    public static void deleteAllTeams() {
        for(Team team : Bukkit.getServer().getScoreboardManager().getMainScoreboard().getTeams())
            team.getScoreboard();
    }
}
