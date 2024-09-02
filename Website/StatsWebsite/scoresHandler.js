import { getRGBFromColor } from "./colorFunctions.js";
import { startBarGraph, serverURL, addNumLoaded } from "./script.js";


var playerScoreMap = new Map();
var teamMap = new Array();
var numSkinsLoaded = 0;

async function getJson(urlExtension) {
    // Tests GET request
    return fetch(serverURL + urlExtension, {
        method: "GET"
    })
        .then((response) => response.json())
        .then((json) => {
            return json;
        });
}


/**
 * Updates the player-score map, team-player-score map, and sorts the teams by score
 * @returns {Array} Sorted team-score array
 */
export async function updateScores() {

    const teamListJson = await getJson("StatsWebsite/teamList.json");
    const scoresJson = await getJson("StatsWebsite/scores.json");
    const skinsJson = await getJson("StatsWebsite/skins.json");

    teamMap = new Array();
    playerScoreMap.clear();


    // Creates player-score hashMap
    let numLoaded = 0;
    for(const key in scoresJson) {
        playerScoreMap.set(key, scoresJson[key]);

        addSkinToPage(key);
    }


    // Creates team-player hashMap (format is "teamName: [player1, player2, totalScore, color]")
    teamListJson.teams.forEach(team => {
        console.log(team.name);
        //teamMap.push([team.name, team.player1, team.player2, scoresJson[team.player1] + scoresJson[team.player2], getRGBFromColor(team.color)]);
        teamMap.push([team.name, team.player1, team.player2, Math.round(Math.random() * 10000), getRGBFromColor(team.color)]);

        
    });

    teamMap.sort((a, b) => b[3] - a[3]);

    teamMap.forEach(team => {
        let teamDiv = document.createElement('div');
        teamDiv.id = `${team[0]}Div`;
        teamDiv.classList.add("teamDiv");
        teamDiv.style = `background-color: rgb(${team[4][0]},${team[4][1]},${team[4][2]}); height: ${Math.round(window.innerHeight*1.5)}px`;
        document.body.appendChild(teamDiv);
    })

    console.log(teamMap);

    startBarGraph();
    
    return teamMap;
}


/**
 * Returns a map relating team color to name
 * @returns {Map} Map of Color-team
 */
export async function getColorMap() {
    let colorMap = new Map();
    teamMap.forEach((team) => {
        colorMap.set(team[4].toString() + ",255", team[0]);
    });

    return colorMap;
}


/**
 * Returns the array of a team with the specificed name
 * @param {string} name Name of team
 * @returns {*} Array representation of team 
 */
export function getTeam(name) {
    let result = undefined;

    teamMap.forEach((team) => {
        if(team[0].toString().valueOf() == name.toString().valueOf()) {
            result = team;
        }
    });

    return result;
}


async function addSkinToPage(playerName) {
    const response = (await fetch(serverURL + `skins/${playerName}`));
    const skin = await response.text();
    console.log(`${playerName}:\n${skin}`);

    const img = new Image();
    img.height = 30;
    img.id = playerName;
    img.onload = function() {
        numSkinsLoaded++;
        addNumLoaded();
        if(numSkinsLoaded == 20) {
            console.log("All skins have been loaded");
        }
    }
    

    document.getElementById("image-container").appendChild(img);

    img.src = skin;
}