package xigmatic.me.dogfight.scoreboard;

import org.json.simple.parser.ParseException;

import java.io.IOException;

public class TeamManager {
    private TourneyTeam[] teams;

    /**
     * Creates TeamManager Object
     */
    public TeamManager() throws IOException, ParseException {
        // Reads teamList.json and parses it into the "teams" variable
        this.teams = JsonHandler.readTeamList();
    }


    /**
     * Finds the TourneyTeam object when given a team name
     * @param teamName Name of team to find TourneyTeam
     * @return TourneyTeam object
     */
    public TourneyTeam findTeamFromTeamName(String teamName) {
        // Checks all team names
        for(TourneyTeam team : teams) {
            if(team.getTeamName().equalsIgnoreCase(teamName))
                return team;
        }

        // Returns null if no such team exists
        return null;
    }


    /**
     * Finds the team name when given a player's name
     * @param playerName Name of player to find team
     * @return Team name of specified player
     */
    public TourneyTeam findTeamFromPlayerName(String playerName) {
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

        // Returns null if no such team exists
        return null;
    }

}
