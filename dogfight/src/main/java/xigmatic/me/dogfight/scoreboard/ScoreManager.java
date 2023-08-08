package xigmatic.me.dogfight.scoreboard;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;

public class ScoreManager implements CommandExecutor {
    private HashMap<String, Integer> playerPointMap;

    /**
     * Creates the manager that links scores and players together
     */
    public ScoreManager() throws IOException, ParseException {
        // Initializes playerPointMap
        this.playerPointMap = new HashMap<>();

        // Adds all players to playerPointMap and assigns them with a score of 0
        for(TourneyTeam team : JsonHandler.readTeamList()) {
            this.playerPointMap.put(team.getPlayer1(), 0);
            this.playerPointMap.put(team.getPlayer2(), 0);
        }
    }


    /**
     * Adds points to specified player
     * @param player Player to receive points
     * @param points Number of points to be added
     */
    public void addPoints(String player, int points) {
        int originalScore = this.playerPointMap.get(player);
        this.playerPointMap.replace(player, originalScore + points);
    }


    /**
     * Sets a specified player's points
     * @param player Player to set points
     * @param points Number of point to set player to
     */
    public void setPoints(String player, int points) {
        this.playerPointMap.replace(player, points);
    }


    /**
     * Gets number of points with the associated player
     * @param player Player to find points of
     * @return Number of points of the given player
     */
    public int getPoints(String player) {
        try {
            return this.playerPointMap.get(player);
        } catch(Exception e) {
            throw new NullPointerException();
        }
    }


    /**
     * Gets the current HashMap of players and their points
     * @return HashMap of the players and their respective
     */
    public HashMap<String, Integer> getPlayerPointMap() {
        return this.playerPointMap;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // getScore
        if(label.equalsIgnoreCase("getScore")) {
            try {
                sender.sendMessage(args[0] + "'s current score is " + getPoints(args[0]));
            } catch(NullPointerException e) {
                sender.sendMessage("This player does not exist");
            } catch(ArrayIndexOutOfBoundsException e) {
                sender.sendMessage("Please specify a player");
            }

            return true;
        }


        return false;
    }
}
