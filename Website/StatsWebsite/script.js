export const serverURL = "http://localhost:3000/";

const rankCanvas = document.getElementById("rankCanvas");
const ctx = rankCanvas.getContext("2d", { willReadFrequently: true });
const resScale = 2;
rankCanvas.width *= resScale;
rankCanvas.height *= resScale;
rankCanvas.style.width = rankCanvas.width/resScale + 'px';
rankCanvas.style.height = rankCanvas.height/resScale + 'px';


import { updateScores, getColorMap, getTeam } from "./scoresHandler.js";

var mcFont = new FontFace('Minecraft', 'url(./StatsWebsite/fonts/minecraft.ttf)');

mcFont.load().then(function(font) {

  // with canvas, if this is ommited won't work
  document.fonts.add(font);
  console.log('Font loaded');
});


// Mouse Positions
var mousePosX = -1;
var mousePoxY = -1;

// Number of skins loaded (for loading bar)
var skinsLoaded = 0;

// Time for bar animation (negative value is now many milliseconds before the bars move)
var barMoveTime = -100;
// Bar reload animation time (in milliseconds)
const maxBarMoveTime = 1000;

// Eased hover anim. time
var easedTime = 0;
// Bar hover animation time
var hoverMoveTime = 0;
// Hover animation time (in milliseconds)
const maxHoverMoveTime = 500;
// Hover text
var hoverText = "";
// Hover team
var hoverTeam = [];
// Bar height multiplier
const maxBarHeight = 200;
// Bar start coordinates
const barPos = [380, 300];
// Bar width
const barWidth = 14;
// Bar spacing
const barSpacing = 0;
// Max bar depress
const maxBarDepress = -600;


// Previous mouse hover color
var currentHoverColor = new Uint8ClampedArray([0,0,0,0]);
// Last selected bar color
var lastBarColor = new Uint8ClampedArray([0,0,0,0]);

// Team Score Map
var teamMap = await updateScores();

// Color-Team map
var colorMap = await getColorMap();


/**
 * 
 * @param {number} delta Frame time
 * @returns Doesn't return anything???
 */
function resetFrame(delta) {

    // Resets frame
    ctx.clearRect(0, 0, rankCanvas.width, rankCanvas.height);

    if(isNaN(delta)) {
        return;
    }

    if(skinsLoaded < 20) {
        drawLoadingBar(0);
        return;
    }

    // Changes bar animation time constant
    if(barMoveTime < maxBarMoveTime) {
        barMoveTime += delta;
    }

    //Changes hover animation time constant
    if(hoverMoveTime < maxHoverMoveTime && currentHoverColor[3] == 255) {
        hoverMoveTime += delta;
    } else if(hoverMoveTime > 0 && currentHoverColor[3] != 255) {
        hoverMoveTime -= delta;
    } else if (hoverMoveTime < 0) {
        hoverMoveTime = 0;
    }

    if(hoverMoveTime > maxHoverMoveTime) {
        hoverMoveTime = maxHoverMoveTime;
    }

    easedTime = 0.5 - 0.5*Math.cos(Math.PI*hoverMoveTime/maxHoverMoveTime);

    

    /// Bar height multiplier (for animation, default is 0)
    let barHeightMoveMultiplier = 0;

    // Height-changing code (animated)
    if(barMoveTime > 0) {
        barHeightMoveMultiplier = (-0.5*Math.cos(Math.PI*barMoveTime/maxBarMoveTime) + 0.5);
    }

    // Draws bar backgrounds (for mouse tracking)
    teamMap.forEach((team, i) => {
        if(team[3] <= maxBarDepress) {
            drawBarBackground(0, resScale*(barPos[0] + (barWidth*1.7 + barSpacing*1.95)*i), resScale*(barPos[1] + (barWidth + barSpacing)*i), team[4][0], team[4][1], team[4][2], 1);
        } else if(maxBarDepress < 0) {
            drawBarBackground(barHeightMoveMultiplier*maxBarHeight*((team[3])/teamMap[0][3]), resScale*(barPos[0] + (barWidth*1.7 + barSpacing*1.95)*i), resScale*(barPos[1] + (barWidth + barSpacing)*i), team[4][0], team[4][1], team[4][2], 1); 
        } else {
            drawBarBackground(barHeightMoveMultiplier*maxBarHeight*((team[3] - maxBarDepress)/teamMap[0][3]), resScale*(barPos[0] + (barWidth*1.7 + barSpacing*1.95)*i), resScale*(barPos[1] + (barWidth + barSpacing)*i), team[4][0], team[4][1], team[4][2], 1);
        }
        
    });

    // Checks if mouse hovered over a new color
    let imageData = ctx.getImageData(mousePosX, mousePoxY, 1, 1).data;
    if(imageData[3] == 255 && !arrayEqualToArray(imageData, currentHoverColor) && colorMap.has(imageData.toString())) {
        let team = getTeam(colorMap.get(imageData.toString()));
        hoverMoveTime = 0;
        easedTime = 0;
        hoverTeam = team;
        hoverText = `${team[0]} (${team[1]} & ${team[2]}): ${team[3]}`;
        
        currentHoverColor = imageData;
        lastBarColor = currentHoverColor;
    }

    // Draws bars
    teamMap.forEach((team, i) => {
        let drawOutline = false;
        let barDepress = 0;
        if(colorMap.has(currentHoverColor.toString()) && getTeam(colorMap.get(currentHoverColor.toString())) == team || 
        colorMap.has(lastBarColor.toString()) && getTeam(colorMap.get(lastBarColor.toString())) == team) {
            if(currentHoverColor[3] == 255) {
                lastBarColor = currentHoverColor;
            }
            drawOutline = currentHoverColor[3] == 255;
            barDepress = maxBarDepress*easedTime;
            if(team[3] < maxBarDepress) {
                barDepress = team[3];
            }
        }
        drawBar(barHeightMoveMultiplier*maxBarHeight*((team[3] - barDepress)/teamMap[0][3]), resScale*(barPos[0] + (barWidth*1.7 + barSpacing*1.95)*i), resScale*(barPos[1] + (barWidth + barSpacing)*i), team[4][0], team[4][1], team[4][2], 1, drawOutline);
    });
    

    // Draws text
    if(hoverMoveTime > 0) {
        drawTeamDisplay(hoverTeam, hoverText, mousePosX, mousePoxY);
    }

    if(imageData[3] != 255) {
        currentHoverColor = imageData;
    }
}


/**
 * Determines if two arrays are equal to each other in values
 * @param {*} a Array 1
 * @param {*} b Array 2
 * @returns Array's equality (boolean)
 */
function arrayEqualToArray(a, b) {
    for(let i = 0; i < a.length; i++) {
        if(a[i] != b[i]) {
            return false;
        }
    }
    return true;
}


/**
 * Creates a 3D bar in the canvas where the coordinates are the bottom point of the bar
 * @param {number} height Height of bar in pixels
 * @param {number} x X coordinate (0 is left)
 * @param {number} y Y coordinate (0 is top)
 * @param {number} r Red color value
 * @param {number} g Green color value
 * @param {number} b Blue color value
 * @param {number} a Transperency value
 * @param {boolean} lines Determines if the outlines will be drawn
 */
function drawBar(height, x, y, r, g, b, a, lines) {
    const halfBarWidth = barWidth*resScale*1.7;
    const barTipOffset = barWidth*resScale;

    // Creates right side of bar
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x + halfBarWidth, y - barTipOffset);
    ctx.lineTo(x + halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x, y - resScale*height);
    ctx.lineTo(x, y);
    ctx.fillStyle = `rgba(${r/1.5},${g/1.5},${b/1.5},${a})`;
    ctx.fill();

    // Creates left side of bar
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x - halfBarWidth, y - barTipOffset);
    ctx.lineTo(x - halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x, y - resScale*height);
    ctx.lineTo(x, y);
    ctx.fillStyle = `rgba(${r/1.25},${g/1.25},${b/1.25},${a})`;
    ctx.fill();

    // Creates top side of bar
    ctx.beginPath();
    ctx.moveTo(x, y - resScale*height);
    ctx.lineTo(x + halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x, y - barTipOffset*2 - resScale*height);
    ctx.lineTo(x - halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x, y - resScale*height);
    ctx.fillStyle = `rgba(${r},${g},${b},${a})`;
    ctx.fill();

    // Lines
    if(lines) {
        // Creates right side lines of bar
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(x + halfBarWidth, y - barTipOffset);
        ctx.lineTo(x + halfBarWidth, y - barTipOffset - resScale*height);
        ctx.lineTo(x, y - resScale*height);
        ctx.lineTo(x, y);
        ctx.lineWidth = 1*resScale;
        ctx.strokeStyle = `rgba(0,0,0,${a})`;
        ctx.stroke();

        // Creates left side lines of bar
        ctx.beginPath();
        ctx.moveTo(x, y);
        ctx.lineTo(x - halfBarWidth, y - barTipOffset);
        ctx.lineTo(x - halfBarWidth, y - barTipOffset - resScale*height);
        ctx.lineTo(x, y - resScale*height);
        ctx.lineTo(x, y);
        ctx.stroke();

        // Creates top side lines of bar
        ctx.beginPath();
        ctx.moveTo(x, y - resScale*height);
        ctx.lineTo(x + halfBarWidth, y - barTipOffset - resScale*height);
        ctx.lineTo(x, y - barTipOffset*2 - resScale*height);
        ctx.lineTo(x - halfBarWidth, y - barTipOffset - resScale*height);
        ctx.lineTo(x, y - resScale*height);
        ctx.stroke();
    }
}


/**
 * Creates a 2D bar background in the canvas where the coordinates are the bottom point of the bar
 * @param {number} height Height of bar in pixels
 * @param {number} x X coordinate (0 is left)
 * @param {number} y Y coordinate (0 is top)
 * @param {number} r Red color value
 * @param {number} g Green color value
 * @param {number} b Blue color value
 * @param {number} a Transperency value
 */
function drawBarBackground(height, x, y, r, g, b, a) {
    const halfBarWidth = (barWidth*1.6)*resScale;
    const barTipOffset = barWidth*resScale;

    // Creates right side of bar
    ctx.beginPath();
    ctx.moveTo(x, y);
    ctx.lineTo(x + halfBarWidth, y - barTipOffset);
    ctx.lineTo(x + halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x, y - barTipOffset*2 - resScale*height);
    ctx.lineTo(x - halfBarWidth, y - barTipOffset - resScale*height);
    ctx.lineTo(x - halfBarWidth, y - barTipOffset);
    ctx.lineTo(x, y);
    ctx.fillStyle = `rgba(${r},${g},${b},${a})`;
    ctx.fill();
}



/**
 * Draws text display (necessary for hovering over a bar)
 * @param {Array} team Team array representation
 * @param {string} text Text to be shown
 * @param {number} x X coordinate of bottom left corner
 * @param {number} y Y coordinate of bottom left corner
 */
function drawTeamDisplay(team, text, x, y) {
    // Draws lines point toward text
    ctx.beginPath();
    ctx.moveTo(x,y);

    // Needs to establish font beforehand to get correct calculations
    ctx.font = `${resScale*30}px Minecraft`;

    const textOffset = [5,6];
    const diagonalLineWidth = 10;
    const linePastTextWidth = 10;

    const textWidth = ctx.measureText(text).width/resScale;
    const diagonalLineTimeRatio = diagonalLineWidth/(diagonalLineWidth+textWidth+textOffset[0]+linePastTextWidth);

    if(easedTime <= diagonalLineTimeRatio) {
        ctx.lineTo(x+resScale*(diagonalLineWidth*easedTime/diagonalLineTimeRatio),y-resScale*(diagonalLineWidth*easedTime/diagonalLineTimeRatio));
    } else {
        ctx.lineTo(x+resScale*(diagonalLineWidth),y-resScale*(diagonalLineWidth));
    }

    if(easedTime > diagonalLineTimeRatio) {
        ctx.lineTo(x+resScale*(diagonalLineWidth+((easedTime-diagonalLineTimeRatio)/(1-diagonalLineTimeRatio))*(linePastTextWidth+textWidth)), y-resScale*(diagonalLineWidth));
        ctx.fillStyle = `rgba(${team[4][0]*0.95},${team[4][1]*0.95},${team[4][2]*0.95},${2*(easedTime-diagonalLineTimeRatio)/(1-diagonalLineTimeRatio)})`;
        ctx.fillText(text,x+resScale*(diagonalLineWidth+textOffset[0]),y-resScale*(diagonalLineWidth+textOffset[1]));
        drawPlayerSkin(team[1], x+resScale*(diagonalLineWidth+textOffset[0]), y-resScale*(diagonalLineWidth+textOffset[1]-10), 2*(easedTime-diagonalLineTimeRatio)/(1-diagonalLineTimeRatio));
        drawPlayerSkin(team[2], x+resScale*(diagonalLineWidth+textOffset[0]+40), y-resScale*(diagonalLineWidth+textOffset[1]-10), 2*(easedTime-diagonalLineTimeRatio)/(1-diagonalLineTimeRatio));
    }
    ctx.lineWidth = resScale*2;
    ctx.strokeStyle = `rgba(${team[4][0]*0.95},${team[4][1]*0.95},${team[4][2]*0.95},1)`;
    ctx.stroke();
}


/**
 * Draws the player's head on the canvas with a specified position and opacity
 * @param {*} playerName Name of player's skin to add
 * @param {*} x X position of top left corner
 * @param {*} y Y position of top left corner 
 * @param {*} a Alpha value of image
 */
function drawPlayerSkin(playerName, x, y, a) {
    ctx.globalAlpha = a;
    ctx.drawImage(document.getElementById(playerName), x, y, 30*resScale, 30*resScale);
    ctx.globalAlpha = 1;
}



var fps;
var previousTime;

export function refreshLoop() {
    window.requestAnimationFrame(function() {
        // FPS Calculations
        const now = performance.now();
        fps = 1000 / (now - previousTime);
        resetFrame(now - previousTime);
        previousTime = now;


        // Recalls frame generation
        refreshLoop();
    });
}


export function startBarGraph() {
    refreshLoop();
}


function drawLoadingBar(numLoaded) {
    const halfLoadingBarWidth = 400;
    const halfLoadingBarHeight = 3;
    ctx.fillStyle = "#00ff37";
    ctx.fillRect(resScale*(500-halfLoadingBarWidth),resScale*(250-halfLoadingBarHeight),resScale*halfLoadingBarWidth*(skinsLoaded/10),resScale*halfLoadingBarHeight*2);
    ctx.fillStyle = "white";
    ctx.beginPath(resScale*(500-halfLoadingBarWidth),resScale*(250-halfLoadingBarHeight));
    ctx.lineTo(resScale*(500+halfLoadingBarWidth),resScale*(250-halfLoadingBarHeight));
    ctx.lineTo(resScale*(500+halfLoadingBarWidth),resScale*(250+halfLoadingBarHeight));
    ctx.lineTo(resScale*(500-halfLoadingBarWidth),resScale*(250+halfLoadingBarHeight));
    ctx.lineTo(resScale*(500-halfLoadingBarWidth),resScale*(250-halfLoadingBarHeight));
    ctx.lineTo(resScale*(500+halfLoadingBarWidth),resScale*(250-halfLoadingBarHeight));
    ctx.strokeStyle = "#000000";
    ctx.lineWidth = resScale;
    ctx.stroke();
}

export function addNumLoaded(numLoaded) {
    skinsLoaded++;
}


rankCanvas.addEventListener("mousemove", function(evt) {
    //console.log(ctx.getImageData(evt.offsetX, evt.offsetY, 1, 1).data[0]);
    mousePosX = resScale*evt.offsetX;
    mousePoxY = resScale*evt.offsetY;
});


rankCanvas.addEventListener("mousedown", function(evt) {
    teamMap.forEach((team, i) => {
        if(colorMap.has(currentHoverColor.toString()) && getTeam(colorMap.get(currentHoverColor.toString())) == team) {
            console.log(team[0]);
            let teamDiv = document.getElementById(`${team[0]}Div`);
            teamDiv.scrollIntoView({behavior: "smooth", block: "center"});
        }
    });
});



document.getElementById("backToTop").addEventListener("mousedown", function(evt) {
    window.scrollTo({top: 0, behavior: "smooth"});
    mousePosX = 0;
    mousePoxY = 0;
    hoverMoveTime = 0;
    currentHoverColor = new Uint8ClampedArray([0,0,0,0]);
    lastBarColor = currentHoverColor;
});



// ######## Begins framerate loop ########
//refreshLoop();