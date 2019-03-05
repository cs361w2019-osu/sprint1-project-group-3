var oldListener;
function registerCellListener(f, elementId) {
    let el = document.getElementById(elementId);
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

function place(size) {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
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

function sonarSweep() {
    return function() {
        let row = this.parentNode.rowIndex;
        let col = this.cellIndex;
        let table = document.getElementById("opponent");
        for(let i = 0; i < 5; i++) {
            let width = 1 + 2 * ((i < 2) ? i : 4 - i);
			for(let j = -Math.floor(width/2); j < Math.floor(width/2)+1; j++) {
                let cell;
                let tableRow = table.rows[row - 2 + i];
                if(tableRow === undefined) {
                    break;
                }
                cell = tableRow.cells[col + j];
                if(cell === undefined) {
                    break;
                }
                cell.classList.toggle("revealed");
			}
		}
    }
}
