var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var gameIsOver = false;
var shipsSunk = 0;
var sonarPulse = 2;
var sonarAvailable = false;
var actionIsSonar = false;
var isSubmerged = false;
var spaceLaserEngaged = false;

function makeGrid(table, isPlayer, gridSize) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        row.style.height=gridSize+"px";
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.style.width=gridSize+"px";
            if(gridSize == newBig() && !gameIsOver) column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function shipTracking(){
    var oppShipCount = 0;
    game.opponentsBoard.ships.forEach((ship) => {
        if(ship.kind === "MINESWEEPER" && ship.sunk === true){
            document.getElementById("mine").classList.add("striked");
            oppShipCount++;
        }
        if(ship.kind === "BATTLESHIP" && ship.sunk === true){
            document.getElementById("batt").classList.add("striked");
            oppShipCount++;
        }
        if(ship.kind === "DESTROYER" && ship.sunk === true){
            document.getElementById("dest").classList.add("striked");
            oppShipCount++;
        }
        if(ship.kind === "SUBMARINE" && ship.sunk === true){
            document.getElementById("sub").classList.add("striked");
            oppShipCount++;
        }
    });
    game.playersBoard.ships.forEach((ship) => {
        if(ship.kind === "MINESWEEPER" && ship.sunk === true)
            document.getElementById("mine2").classList.add("striked");
        if(ship.kind === "BATTLESHIP" && ship.sunk === true)
            document.getElementById("batt2").classList.add("striked");
        if(ship.kind === "DESTROYER" && ship.sunk === true)
            document.getElementById("dest2").classList.add("striked");
        if(ship.kind === "SUBMARINE" && ship.sunk === true)
            document.getElementById("sub2").classList.add("striked");
    });
    shipsSunk=oppShipCount;
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        if (attack.result === "HIT")
            className = "hit";
        if (attack.result === "SUNK"){
            className = "sink";
        }
        else if (attack.result === "SURRENDER") {
            className = "sink";
            document.getElementById("player_prompt").textContent=surrenderText+"!";
            if(!gameIsOver) alert(surrenderText);
            gameIsOver = true;
        }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].className = '';
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].className = className;
    });
}

function redrawGrid() {

    if(spaceLaserEngaged == false && game.opponentsBoard.remainingShips < 3 && !isSetup) {
        spaceLaserEngaged = true;
        var laserSpan = document.createElement("span");
        laserSpan.style.color = "red";
        laserSpan.style.fontSize = "20px";
        laserSpan.appendChild(document.createTextNode(" - SPACE LASER ENGAGED"));
        document.querySelector("h1").appendChild(laserSpan);
        alert("Warning: Space Laser has been engaged.\n\n You may now attack ANY square.");
    }

    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());

    document.getElementById("player_prompt").style.width=(newSmall()*10+40)+"px";

    if(placedShips==4) {
        makeGrid(document.getElementById("opponent"), false, newBig());
        makeGrid(document.getElementById("player"), true, newSmall());
    } else {
        makeGrid(document.getElementById("opponent"), false, newSmall());
        makeGrid(document.getElementById("player"), true, newBig());
    }

    shipTracking();

    sonarPulseUpdate();

    if (game === undefined) {
        return;
    }
    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    // mark captain's quarters
    game.playersBoard.ships.forEach((ship) => {
        document.getElementById("player").rows[ship.captainsQuarters.row-1].cells[ship.captainsQuarters.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("captainsQuarter");
    });

    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
}

function sonarPulseUpdate(){
    if(!sonarAvailable && shipsSunk > 0 && sonarPulse > 0 && !gameIsOver){
        sonarAvailable = true;
    }

    if(sonarAvailable && !gameIsOver){
        var prompt = document.getElementById("player_prompt");
        var sonarKey = document.createElement("kbd");
        sonarKey.textContent = "s";
        prompt.textContent = "Select Your Next Attack OR Deploy Sonar Pulse: ";
        prompt.appendChild(sonarKey);
    }
}

var oldListener;
function registerCellListener(f) {
    let el = document.getElementById("player");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldListener = f;
}

var oldOppListener;
function registerOppCellListener(f) {
    let el = document.getElementById("opponent");
    for (i=0; i<10; i++) {
        for (j=0; j<10; j++) {
            let cell = el.rows[i].cells[j];
            cell.removeEventListener("mouseover", oldListener);
            cell.removeEventListener("mouseout", oldListener);
            cell.addEventListener("mouseover", f);
            cell.addEventListener("mouseout", f);
        }
    }
    oldOppListener = f;
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup && !gameIsOver) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            placedShips++;
            if (placedShips == 4) {
                document.getElementById("player_prompt").textContent="Select Your Next Attack";
                document.getElementById("play_board").classList.toggle("small");
                document.getElementById("opp_board").classList.toggle("small");
                document.getElementById("play_board").classList.toggle("big");
                document.getElementById("opp_board").classList.toggle("big");
                isSetup = false;
                registerCellListener((e) => {});
            }
            redrawGrid();
            doShipPlacement();
        });
    } else if (!gameIsOver && !actionIsSonar) {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();

        });
    } else if (!gameIsOver && actionIsSonar) {
        actionIsSonar=false;
        sonarPulse--;
        if(sonarPulse==0) sonarAvailable=false;
        redrawGrid();

        drawSonar(row-1, this.cellIndex);
        document.getElementById("player_prompt").textContent="Select Your Next Attack";

        sonarPulseUpdate();
    }
}

function drawSonar (row, col) {

    let table = document.getElementById("opponent");

    gameRow = row + 1;
    gameCol = String.fromCharCode(col + 65);

    game.opponentsBoard.ships.forEach((ship) => {
        ship.occupiedSquares.forEach((square) => {
            for (let x=-2; x<3; x++) {
                let cell;
                try {
                    let tableRow = table.rows[row+x];
                    cell = tableRow.cells[col];
                    if (!cell.classList.contains("occupied")) cell.className="placed";
                    if(square.row === gameRow+x && square.column === gameCol) {
                        cell.classList.remove("placed");
                        cell.classList.add("occupied");
                    }
                } catch(error) {}
            }
            for (let y=-2; y<3; y++) {
                let cell;
                try {
                    cell = table.rows[row].cells[col+y];
                    if (!cell.classList.contains("occupied")) cell.className="placed";
                    if(square.row === gameRow && square.column === String.fromCharCode(col+65+y) ){
                        cell.classList.remove("placed");
                        cell.classList.add("occupied");
                    }
                } catch(error) {}
            }
            for (let x=-1; x<2; x+=2) {
                try {
                    let tableRow = table.rows[row+x];
                    for(let y =-1; y<2; y+=2) {
                        cell = tableRow.cells[col+y];
                        if (!cell.classList.contains("occupied")) cell.className="placed";
                        if (square.row === gameRow+x && square.column === String.fromCharCode(col+65+y) ) {
                            cell.classList.remove("placed");
                            cell.classList.add("occupied");
                        }
                    }
                } catch (error) {}
            }
        });
    });
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            //alert("Cannot complete the action");
            show_modal();
            return;
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        vertical = document.getElementById("is_vertical").checked;
        let table = document.getElementById("player");
        //Using a flag for submarine highlighting.
        if(shipType == "submarine" || shipType == "SUBMARINE") addNub = true;
        else addNub = false;
        rowOffset=0, colOffset=0;
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                //if this is the third square (i=2) and the flag is set, we'll shift the col +1, highlight that cell instead
                if(addNub && i == 2) colOffset = 1;
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                //prevents breaking when the nub is out of bounds
                try{ cell = tableRow.cells[col+colOffset]; } catch(error) {}
                //with the nub highlighted, we zero the offset, remove the flag and backtrack the loop with i--
                if(colOffset>0){
                    colOffset = 0;
                    addNub = false;
                    i--;
                }
            } else {
                //same as above, but for a horizontal ship
                if(addNub && i == 2) rowOffset = 1;
                //prevents breaking when the nub is out of bounds
                try{ cell = table.rows[row-rowOffset].cells[col+i]; } catch (error) {}
                //this zeros the offset and repeats this loop, now without the flag set
                if(rowOffset>0) {
                    rowOffset=0;
                    addNub = false;
                    i--;
                }
            }
            if(cell === undefined) {}
            else if (isSubmerged) {
                cell.classList.toggle("placed");
                cell.classList.toggle("submerged")
            } else cell.classList.toggle("placed");
        }
    }
}

function sonar() {
    return function () {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        let table = document.getElementById("opponent");

        for (let x=-2; x<3; x++) {
            let cell;
            try {
                let tableRow = table.rows[row+x];
                cell = tableRow.cells[col];
                cell.classList.toggle("placed");
            } catch(error) {}
        }

        for (let y=-2; y<3; y++) {
            let cell;
            try {
                cell = table.rows[row].cells[col+y];
                cell.classList.toggle("placed");
            } catch(error) {}
        }

        for (let x=-1; x<2; x+=2) {
            let tableRow = table.rows[row+x];
                for(let y =-1; y<2; y+=2) {
                    try {
                    cell = tableRow.cells[col+y];
                    cell.classList.toggle("placed");
                    } catch (error) {}
                }
        }

        table.rows[row].cells[col].classList.toggle("placed");
    }
}


function set_modal_text() {
    modal_text = document.getElementById("modal-main-text");
    if(isSetup) {
        //invalid ship placement
        modal_text.innerText = "Warning: Invalid ship placement detected. Please click the ship you would like to place, and select a valid location.";
    } else {
        //invalid attack
        modal_text.innerText = "The attack you just tried to make is invalid. Please try again.";
    }
}

function close_modal(){
    document.getElementById("modal-backdrop").classList.add("hidden");
    document.getElementById("error-modal").classList.add("hidden");
}

function show_modal() {
    set_modal_text();
    document.getElementById("modal-backdrop").classList.remove("hidden");
    document.getElementById("error-modal").classList.remove("hidden");
}

//calculates size for the grids, based on the smallest of the viewport dimensions (client width and height)
function newBig() {
    return Math.floor(Math.min(document.documentElement.clientWidth, document.documentElement.clientHeight) * .85 / 10);
}

function newSmall() {
    return Math.floor(Math.min(document.documentElement.clientWidth, document.documentElement.clientHeight) * .40 / 10);
}

//this does the automatically started ship placement
function doShipPlacement() {
    var prompt = document.getElementById("player_prompt");
    var rotateKey = document.createElement("kbd");
    rotateKey.textContent = "r";
    var submergeKey = document.createElement("kbd");
    submergeKey.textContent = "u";
    if (placedShips==0){
        shipType = "MINESWEEPER";
        registerCellListener(place(2));
        prompt.textContent = "Place your "+shipType+". \nRotate: ";
        prompt.appendChild(rotateKey);
    } else if (placedShips==1){
        shipType = "DESTROYER";
        registerCellListener(place(3));
        prompt.textContent = "Place your "+shipType+". \nRotate: ";
        prompt.appendChild(rotateKey);
    } else if (placedShips==2){
        shipType = "BATTLESHIP";
        registerCellListener(place(4));
        prompt.textContent = "Place your "+shipType+". \nRotate: ";
        prompt.appendChild(rotateKey);
    } else if (placedShips==3){
        shipType = "submarine";
        registerCellListener(place(4));
        document.addEventListener('keypress', function(e) {
            if (e.which == 85 || e.which == 117) {
                isSubmerged = !isSubmerged;
                redrawGrid();

                // "SUBMARINE" indicates a submersed sub; "submarine" means a surfaced sub
                if (isSubmerged) {
                    shipType = "SUBMARINE";
                } else {
                    shipType = "submarine";
                }

                registerCellListener(place(4));
            }

        });
            prompt.textContent="Place your "+shipType.toUpperCase()+". \nRotate: ";
            prompt.appendChild(rotateKey);
            var submergeText = document.createTextNode("\nSubmerge: ");
            prompt.appendChild(submergeText);
            prompt.appendChild(submergeKey);
    }
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false, newSmall());
    makeGrid(document.getElementById("player"), true, newBig());

    //listener to resize the grids when the window changes sizes
    window.addEventListener("resize", function(e) {
            redrawGrid();
    });

    // implement keypress "R" to rotate
    document.addEventListener('keypress', function(e) {
        if (e.which == 82 || e.which == 114) { // guard against CAPS LOCK
          document.getElementById("is_vertical").click();
          redrawGrid();
          if (shipType=="MINESWEEPER" && isSetup) registerCellListener(place(2));
          else if (shipType=="DESTROYER" && isSetup) registerCellListener(place(3));
          else if (shipType=="BATTLESHIP" && isSetup) registerCellListener(place(4));
          else if ((shipType=="SUBMARINE" || shipType=="submarine") && isSetup) registerCellListener(place(4));

        }

        //keypress 's' or 'S' to activate sonar pulse, if available
        else if (e.which == 83 || e.which == 115){
          if(sonarAvailable && shipsSunk > 0 && sonarPulse > 0 && !gameIsOver){
            redrawGrid();
            registerOppCellListener(sonar());
            document.getElementById("player_prompt").textContent = "Select the Location to Reveal";
            actionIsSonar=true;
          }
        }
    });

    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });

    //event handlers for the modal
    document.getElementById("modal-close").addEventListener("click", close_modal);
    document.getElementById("modal-ok-button").addEventListener("click", close_modal);

    // event handler for the surrender button
    document.getElementById("surrender").addEventListener("click", function() {
        game.playersBoard.attacks.result = "SURRENDER";
        var surrenderText = "You forfeited the game.";
        if(!gameIsOver && confirm("Do you want to surrender?")) {
            alert(surrenderText);
            gameIsOver = true;
            document.getElementById("player_prompt").textContent = surrenderText;
        }
    });

    //event to start new game (reload the page)
    document.getElementById("new_game").addEventListener("click", function() {
        if(confirm("Do you want to start a new game?")) location.reload();
    });

    //begins auto ship placement
    doShipPlacement();
};