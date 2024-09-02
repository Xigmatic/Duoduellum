package xigmatic.me.dogfight.scoreboard;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class JsonHandler {
    private File scoreFile;
    private FileReader fileReader;
    private JSONParser jsonParser;
    private JSONObject jsonObject;
    private static final String teamListURL = System.getProperty("user.dir") + "/plugins/teamList.json";


    /*
        FORMAT FOR scores.json
        ------------------------
        {"-Insert player1 name": __score__, "-Insert player2 name": ___, "-Insert player3 name": ___, ...}

        FORMAT FOR teamList.json
        ----------------------
        {
            "teams": [
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"},
                {"name": "___", "player1": "___", "player2": "___", "color": "___", "logo": "___"}
            ]
        }
     */


    /**
     * Creates the writer/reader necessary for distributing scores across all game servers
     */
    public JsonHandler() throws IOException, ParseException {
        // Gets server directory
        String workingDirectory = System.getProperty("user.dir");

        // Gets file object
        this.scoreFile = new File(workingDirectory + "/plugins/scores.json");

        // Checks if json file exists
        if(!this.scoreFile.exists()) {
            this.scoreFile.createNewFile();
            Bukkit.getConsoleSender().sendMessage("Score.json DOES NOT EXIST! CREATING ONE IN " +
                    this.scoreFile.getAbsolutePath());

            // Resets newly-created file
            resetFile();
        }

        // Creates temporary FileReader
        this.fileReader = new FileReader(this.scoreFile);

        // Initializes jsonParser
        this.jsonParser = new JSONParser();

        // Initializes/creates JsonObject
        this.jsonObject = (JSONObject) this.jsonParser.parse(this.fileReader);

        // Closes fileReader
        this.fileReader.close();
    }


    /**
     * Recreates jsonObject so it is up-to-date
     * @throws IOException If file does not exist
     * @throws ParseException Failed to parse json file
     */
    private void reparseJson() throws IOException, ParseException {
        // Remakes fileReader
        this.fileReader = new FileReader(this.scoreFile);

        // Creates new Parser
        this.jsonParser = new JSONParser();

        // Re-parses json file
        this.jsonObject = (JSONObject) this.jsonParser.parse(this.fileReader);
    }


    /**
     * Clears all text from json file
     */
    private void clearFile() {
        // Checks if jsonObject exists and creates a new instance if necessary
        if(jsonObject == null) jsonObject = new JSONObject();

        // Clears pre-existing jsonObject
        this.jsonObject.clear();
    }


    /**
     * Clears and reformats the score json file
     */
    public void resetFile() throws IOException, ParseException {
        // Clears current or creates a new one to prepare for reformatting
        clearFile();

        // Prepares jsonObject with all players and a default score of 0
        for(TourneyTeam team : TeamManager.getAllTeams()) {
            this.jsonObject.put(team.getPlayer1(), 0);
            this.jsonObject.put(team.getPlayer2(), 0);
        }

        // Writes all json text to the json file and closes the writer
        try {
            FileWriter tempWriter = new FileWriter(this.scoreFile, false);
            tempWriter.write(this.jsonObject.toJSONString());
            tempWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Uses the passed HashMap and formats it into json then is written into the scores.json file
     * @param scores HashMap to be converted and written
     */
    public void writeScores(HashMap<String, Integer> scores) {
        // Adds players/scores to jsonObject
        for(String player : scores.keySet()) {
            this.jsonObject.put(player, scores.get(player));
        }

        // Writes all json text to the json file and closes the writer
        try {
            FileWriter tempWriter = new FileWriter(this.scoreFile, false);
            tempWriter.write(this.jsonObject.toJSONString());
            tempWriter.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Reads the teamList json file and returns an array of teams of found in the json file
     * CREATES ALL TOURNEY TEAMS AND DOES ALL STARTUP PROCESSES (LIKE CREATE SCOREBOARD TEAMS)
     * @return List of teams found in the json file
     */
    public static TourneyTeam[] refreshTeamList() throws IOException, ParseException {
        // Creates an empty set of TourneyTeams
        TourneyTeam[] teams = new TourneyTeam[10];

        // Creates file if not already present
        File teamList = new File(teamListURL);
        if(!teamList.exists()) {
            teamList.createNewFile();
            Bukkit.getConsoleSender().sendMessage("teamList.json DOES NOT EXIST! CREATING ONE IN " +
                    teamList.getAbsolutePath());
        }

        // Creates temporary fileReader
        FileReader tempReader = new FileReader(teamList);

        // Creates temporary JsonObject
        JSONObject tempJsonObject = (JSONObject) new JSONParser().parse(tempReader);

        // Iterates through array of teams found in json file
        JSONArray teamArray = (JSONArray) tempJsonObject.get("teams");
        for(int i = 0; i < 10; i++) {
            JSONObject team = (JSONObject) teamArray.get(i);

            // Sets the index of the teams array to a new TourneyTeam instance from the data of the "team" JSON object
            // (See formatting above for proper formatting)
            teams[i] = new TourneyTeam(team.get("name").toString(), team.get("player1").toString(),
                    team.get("player2").toString(), team.get("color").toString(), team.get("logo").toString());

            // Sends test message to confirm each team is being initialized
            Bukkit.getConsoleSender().sendMessage(teams[i].getChatColor() + teams[i].getTeamName());
        }

        // Closes fileReader
        tempReader.close();

        // Returns final array of newly-created TourneyTeams
        return teams;
    }


    /**
     * Clears and rewrites the current PlayerPointMap so that it matches the scores.json file
     * @param currentMap HashMap object to be modified
     */
    public void updatePlayerPointHashMap(HashMap<String, Integer> currentMap) {
        // Resets current HashMap
        currentMap.clear();

        // Recreates jsonObject
        try {
            reparseJson();
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }

        // Rewrites to HashMap according to current scores.json
        for(Object player : jsonObject.keySet()) {
            currentMap.put(player.toString(), ((Long) jsonObject.get(player)).intValue());
        }
    }
}
