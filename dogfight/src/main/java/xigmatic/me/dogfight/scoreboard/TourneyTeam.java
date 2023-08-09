package xigmatic.me.dogfight.scoreboard;

import java.util.UUID;

public class TourneyTeam {
    // Team Info
    private final String teamName;

    // Player Info
    private String p1;
    private String p2;
    private UUID p1ID;
    private UUID p2ID;


    /**
     * Creates a new duo with a team name and both players
     */
    public TourneyTeam(String teamName, String player1, String player2) {
        this.teamName = teamName;
        this.p1 = player1;
        this.p2 = player2;
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
}
