var isSetup = true;
var placedShips = 0;
var game;
var shipType;
var vertical;
var gameIsOver = false;

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

function shipTracking(board){
    //if(game.opponentsBoard.shipTrack("MINESWEEPER") == false)
    game.opponentsBoard.ships.forEach((ship) => {
        if(ship.kind === "MINESWEEPER" && ship.Sunk === true)
            document.getElementById("mine").classList.add("striked");
        if(ship.kind === "BATTLESHIP" && ship.Sunk === true)
            document.getElementById("batt").classList.add("striked");
        if(ship.kind === "DESTROYER" && ship.Sunk === true)
            document.getElementById("dest").classList.add("striked");
    });
    game.playersBoard.ships.forEach((ship) => {
        if(ship.kind === "MINESWEEPER" && ship.Sunk === true)
            document.getElementById("mine2").classList.add("striked");
        if(ship.kind === "BATTLESHIP" && ship.Sunk === true)
            document.getElementById("batt2").classList.add("striked");
        if(ship.kind === "DESTROYER" && ship.Sunk === true)
            document.getElementById("dest2").classList.add("striked");
    });
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS")
            className = "miss";
        else if (attack.result === "HIT")
            className = "hit";
        else if (attack.result === "SUNK"){
            shipTracking(board);
            className = "sink";
            }
        else if (attack.result === "SURRENDER") {
            className = "sink";
            document.getElementById("player_prompt").textContent=surrenderText+"!";
            if(!gameIsOver) alert(surrenderText);
            gameIsOver = true;
        }
        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);
    });
}

function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());

    if(placedShips==3) {
        makeGrid(document.getElementById("opponent"), false, newBig());
        makeGrid(document.getElementById("player"), true, newSmall());
    } else {
        makeGrid(document.getElementById("opponent"), false, newSmall());
        makeGrid(document.getElementById("player"), true, newBig());
    }

    if (game === undefined) {
        return;
    }
    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        document.getElementById("player").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    }));
    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");
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

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);
    if (isSetup) {
        sendXhr("POST", "/place", {game: game, shipType: shipType, x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            placedShips++;
            if (placedShips == 3) {
                document.getElementById("player_prompt").textContent="Select Your Next Attack";
                document.getElementById("play_board").classList.toggle("small");
                document.getElementById("opp_board").classList.toggle("small");
                document.getElementById("play_board").classList.toggle("big");
                document.getElementById("opp_board").classList.toggle("big");
                isSetup = false;
                registerCellListener((e) => {});
            }
            redrawGrid();
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        })
    }
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
        for (let i=0; i<size; i++) {
            let cell;
            if(vertical) {
                let tableRow = table.rows[row+i];
                if (tableRow === undefined) {
                    // ship is over the edge; let the back end deal with it
                    break;
                }
                cell = tableRow.cells[col];
            } else {
                cell = table.rows[row].cells[col+i];
            }
            if (cell === undefined) {
                // ship is over the edge; let the back end deal with it
                break;
            }
            cell.classList.toggle("placed");
        }
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

function newBig() {
    return Math.floor(Math.min(document.documentElement.clientWidth, document.documentElement.clientHeight) * .85 / 10);
}

function newSmall() {
    return Math.floor(Math.min(document.documentElement.clientWidth, document.documentElement.clientHeight) * .40 / 10);
}

function initGame() {
    makeGrid(document.getElementById("opponent"), false, newSmall());
    makeGrid(document.getElementById("player"), true, newBig());
    window.addEventListener("resize", function(e) {
            redrawGrid();
    });

    // implement keypress "R" to rotate
    document.addEventListener('keypress', function(e) {
        if (e.which == 82 || e.which == 114) { // guard against CAPS LOCK
          console.log(e.which);
          document.getElementById("is_vertical").click();
          redrawGrid();
          if (shipType=="MINESWEEPER") registerCellListener(place(2));
          else if (shipType=="DESTROYER") registerCellListener(place(3));
          else if (shipType=="BATTLESHIP") registerCellListener(place(4));
        }
      });
    document.getElementById("place_minesweeper").addEventListener("click", function(e) {
        shipType = "MINESWEEPER";
       registerCellListener(place(2));
    });
    document.getElementById("place_destroyer").addEventListener("click", function(e) {
        shipType = "DESTROYER";
       registerCellListener(place(3));
    });
    document.getElementById("place_battleship").addEventListener("click", function(e) {
       shipType = "BATTLESHIP";
       registerCellListener(place(4));
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
        var surrenderText = "You forfeited the game. Victory goes to the opponent!"
        document.getElementById("player_prompt").textContent = surrenderText;
        if(!gameIsOver) alert(surrenderText);
        gameIsOver = true;
    });

};