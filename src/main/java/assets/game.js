
var isSetup = true;
var sonar = false;
var pulsesRemaining = 2;
var placedShips = 0;
var game;
var shipType = [];

var vertical;

function getCellAt(elementId, row, col) {
    let element = document.getElementById(elementId);
    return element.rows[row-1].cells[col  - 'A'.charCodeAt(0)];
}

function makeGrid(table) {
    for (i=0; i<10; i++) {
        let row = document.createElement('tr');
        for (j=0; j<10; j++) {
            let column = document.createElement('td');
            column.addEventListener("click", cellClick);
            row.appendChild(column);
        }
        table.appendChild(row);
    }
}

function markHits(board, elementId, surrenderText) {
    board.attacks.forEach((attack) => {
        let className;
        if (attack.result === "MISS"){
            className = "miss";
        } else if (attack.result === "HIT"){
                className = "hit";
        } else if (attack.result === "SUNK") {

            // reveal the sonar button
            if(pulsesRemaining === 2) {
                document.getElementById("use_sonar").classList.remove("hidden");
            }

            document.getElementById(elementId + "-" + attack.ship.shipType.toLowerCase()).classList.add("crossed-out");                                  //if sunken, cross out ship name
            document.getElementById(elementId + "-" + attack.ship.shipType.toLowerCase()).classList.add("secondary-color");                              //also changes color of said ship name
            className = "sink";
            attack.ship.occupiedSquares.forEach((square) => {                                                                                           //if ship sunk, grab all occupied squares of ship
                getCellAt(elementId, square.row, square.column.charCodeAt(0)).classList.remove("miss");  //set all ship elements to sink class name
                getCellAt(elementId, square.row, square.column.charCodeAt(0)).classList.add("sink");
            });
        } else if (attack.result === "SURRENDER") {      
            className = "sink";
            attack.ship.occupiedSquares.forEach((square) => {                                                                                           //if ship sunk, grab all occupied squares of ship
                getCellAt(elementId, square.row, square.column.charCodeAt(0)).classList.remove("miss");  //set all ship elements to sink class name
                getCellAt(elementId, square.row, square.column.charCodeAt(0)).classList.add("sink");
            });                            
            showError(surrenderText);
            document.getElementById("error-ok").removeEventListener("click", closeError);
            document.getElementById("error-ok").addEventListener("click", (e) => {
                location.reload();
            });
        }

        document.getElementById(elementId).rows[attack.location.row-1].cells[attack.location.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add(className);

         if (elementId === "opponent"){                                         
            document.getElementById("results-text").innerHTML = attack.result;
            document.getElementById("results-text").classList = className;      //changes background of status board

        }
    });

}

function markSonar(board){
    board.sonarpulses.forEach((pulse) => {
       pulse.revealedShips.forEach((square) => {
            getCellAt("opponent", square.row, square.column.charCodeAt(0)).classList.add("occupied");
       });

        pulse.revealedSquares.forEach((square) => {
            getCellAt("opponent", square.row, square.column.charCodeAt(0)).classList.add("revealed");
        });
    });
}


function redrawGrid() {
    Array.from(document.getElementById("opponent").childNodes).forEach((row) => row.remove());
    Array.from(document.getElementById("player").childNodes).forEach((row) => row.remove());
    makeGrid(document.getElementById("opponent"), false);
    makeGrid(document.getElementById("player"), true);
    if (game === undefined) {
        return;
    }

    game.playersBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
        getCellAt("player", square.row, square.column.charCodeAt(0)).classList.add("occupied");
    }));

    // TOGGLE FOR HACKS
    // game.opponentsBoard.ships.forEach((ship) => ship.occupiedSquares.forEach((square) => {
    //     document.getElementById("opponent").rows[square.row-1].cells[square.column.charCodeAt(0) - 'A'.charCodeAt(0)].classList.add("occupied");
    // }));

    markHits(game.opponentsBoard, "opponent", "You won the game");
    markHits(game.playersBoard, "player", "You lost the game");

    markSonar(game.opponentsBoard);
}

function cellClick() {
    let row = this.parentNode.rowIndex + 1;
    let col = String.fromCharCode(this.cellIndex + 65);

    if (isSetup) {

        sendXhr("POST", "/place", {game: game, shipType: shipType[0], x: row, y: col, isVertical: vertical}, function(data) {
            game = data;
            redrawGrid();
            updateShipList();
            placedShips++;
            if (placedShips == 3) {
                isSetup = false;
                registerCellListener((e) => {}, "player");
                document.getElementById("is_vertical").style.visibility = "hidden";
            }
        });

    } else if(sonar && pulsesRemaining > 0 && !isSetup){
        sendXhr("POST", "/sonar", {game: game, x: row, y: col}, function(data) {
           game = data;
           console.log("Sonar has been used!");
           redrawGrid();
           toggleSonar();

           pulsesRemaining--;
           if(pulsesRemaining === 0) {
               document.getElementById("use_sonar").classList.add("hidden");
           }
           
        });
    } else {
        sendXhr("POST", "/attack", {game: game, x: row, y: col}, function(data) {
            game = data;
            redrawGrid();
        });
    }
}

function updateShipList() {
    shipType.shift();
    if(shipType.length === 2) {
        registerCellListener(place(3), "player")
    } else if(shipType.length === 1) {
        registerCellListener(place(2), "player")
    }
}

function sendXhr(method, url, data, handler) {
    var req = new XMLHttpRequest();
    req.addEventListener("load", function(event) {
        if (req.status != 200) {
            if (placedShips == 3) {
                showError("Cannot target that spot");
                return;
            } else {
                showError("Out of bounds");
                return;
            }
        }
        handler(JSON.parse(req.responseText));
    });
    req.open(method, url);
    req.setRequestHeader("Content-Type", "application/json");
    req.send(JSON.stringify(data));
}

function toggleVertical() {
    document.getElementById("is_vertical").classList.toggle("button-depressed");
    vertical = !vertical;
}

function toggleSonar() {
    document.getElementById("use_sonar").classList.toggle("button-depressed");
    sonar = !sonar;

    if(sonar) {
        registerCellListener(sonarSweep(), "opponent");
    } else {
        registerCellListener((e) => {}, "opponent");
    }
}

function initGame() {
    document.getElementById("error-ok").addEventListener("click", closeError);
    document.getElementById("is_vertical").addEventListener("click", toggleVertical);
    document.getElementById("use_sonar").addEventListener("click", toggleSonar);

    makeGrid(document.getElementById("opponent"));
    makeGrid(document.getElementById("player"));


    shipType = ["BATTLESHIP", "DESTROYER", "MINESWEEPER"];
    registerCellListener(place(4), "player");

    sendXhr("GET", "/game", {}, function(data) {
        game = data;
    });
};