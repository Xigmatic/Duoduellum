const teamURL = "./StatsWebsite/teamList.json";
let data = require(teamURL);
//require("dotenv").config();

const fs = require("fs");

const cors = require("cors");
const corsOptions = {
   origin:'*',
   allowedHeaders: ['User-Agent', 'Accept', 'Access-Control-Allow-Origin', 'Content-Type', 'Origin', 'X-Requested-With', 'Accept', 'x-client-key', 'x-client-token', 'x-client-secret', 'Authorization'],
   credentials:true,            //access-control-allow-credentials:true
   optionSuccessStatus:200,
   methods: ['GET', 'POST', 'PUT', 'DELETE', 'PATCH', 'HEAD', 'OPTIONS'],
};

const express = require('express'),
      app = express(),
      bodyParser = require('body-parser');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors(corsOptions)); // Use this after the variable declaration


app.post('/', (req, res) => {
    console.log(req.body.message);
    fs.writeFile(teamURL, JSON.stringify(req.body), (err) => {
        if(err) {
            console.error(err);
        } else {
            console.log("Successfully saved to file");
        }
    });
    return res.send("went well");
});

app.get('/', (req, res) => {
    console.log(req.ip);
    console.log("Loaded webpage at " + getClockTime());
    res.sendFile(__dirname + "/StatsWebsite/index.html");
});

app.get('/StatsWebsite/*', (req, res) => {
    console.log("Loaded " + req.url);
    res.sendFile(__dirname + req.url);
});

app.get('/skins/*', async (req, res) => {
    const name = req.url.substring(7);
    res.end(await getSkin(name));
})


// Opens up port 3000
app.listen(3000, () => {
    console.log("Running on port 3000");
});

// Opens up port 80
app.listen(80, () => {
    console.log("Running on port 80 as well");
});



function getClockTime() {
    const date = new Date();
    const hours = date.getHours();
    const minutes = date.getMinutes();
    const seconds = date.getSeconds();
  
    // Convert the hours to a 12-hour format.
    const hours12 = hours % 12;
  
    // Add a leading zero to the minutes and seconds if they are less than 10.
    const minutesString = minutes < 10 ? `0${minutes}` : minutes;
    const secondsString = seconds < 10 ? `0${seconds}` : seconds;
  
    // Return the time as a string in the format "hh:mm:ss AM/PM".
    return `${hours12}:${minutesString}:${secondsString} ${hours >= 12 ? 'PM' : 'AM'}`;
}

/**
 * Gets the skin texture of a player from online as a base64 string
 * @param {string} playerName Player name
 * @returns {*} Base64 string of player skin
 */
async function getSkin(playerName) {
    let response;
    try {
        response = await fetch(`https://skins.danielraybone.com/v1/head/${playerName}`);
    } catch (err) {
        return "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAYAAACOEfKtAAAAAXNSR0IArs4c6QAAAOpJREFUeJzt2sENwjAQAEGCqDSFpVVoIdIemMBMAY60ukdO9nYbtu/7c/rMScdxbJPn3ScP+0cCRgJGAkYCRgJGAkYCRgJGp//Kv33DmHZ2YzGBkYCRgJGAkYCRgJGAkYCRgNFj1Yen7yZWbUomMBIwEjASMBIwEjASMBIwEjAa3QbeYdWG4U7kQwSMBIwEjASMBIwEjASMBIyWbSLfvmGcZQIjASMBIwEjASMBIwEjASMBo2Wvs6ZNbxhnmcBIwEjASMBIwEjASMBIwEhAAACAy/mZ11nuRC5KwEjASMBIwEjASMBIwEjA6AWy6RpnvXF1WQAAAABJRU5ErkJggg==";
    }
    
    let blob = await response.blob();
    let buffer = Buffer.from(await blob.arrayBuffer());
    let skin = "data:image/png;base64," + buffer.toString('base64');
    return skin.toString();
}