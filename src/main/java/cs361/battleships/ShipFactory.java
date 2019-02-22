package cs361.battleships;

import cs361.battleships.models.*;

public class ShipFactory {


    public static Ship Build(String kind) {
        switch(kind) {
            case "BATTLESHIP":
                return new Battleship();
            case "MINESWEEPER":
                return new Minesweeper();
            case "DESTROYER":
                return new Destroyer();
            default:
                return new Ship("INVALID");
        }
    }

}