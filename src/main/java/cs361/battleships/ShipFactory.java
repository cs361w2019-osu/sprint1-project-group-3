package cs361.battleships;

import cs361.battleships.models.*;

public class ShipFactory {


	public static Ship Duplicate(Ship ship) {
        if(ship == null)
            return null;
        
		Ship result = ShipFactory.Build(ship);

        if(ship.getShipType() == Ship.ShipType.INVALID) 
            return result;

		result.setHealth(ship.getHealth());
		result.setcqHit(ship.getcqHit());

		Square base = ship.getOccupiedSquares().get(0);

		result.setOccupiedSquaresByOrientation(base.getRow(), base.getColumn(), base.getRow() != ship.getOccupiedSquares().get(1).getRow());


		return result;
	}

    public static Ship Build(Ship ship) {
        if(ship == null)
            return null;

        return Build(ship.getShipType().name());
    }

    public static Ship Build(String kind) {
        switch(kind) {
            case "BATTLESHIP":
                return new Battleship();
            case "MINESWEEPER":
                return new Minesweeper();
            case "DESTROYER":
                return new Destroyer();
            default:
                return new InvalidShip();
        }
    }

}