package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.lang.Exception;

public class Ship {

	public static enum ShipType {
		MINESWEEPER(2),
		BATTLESHIP(4),
		DESTROYER(3),
		INVALID(0);

		private final int value;

		private ShipType(int value) {
			this.value = value;
		}

		public int getValue() {
			return this.value;
		}
	}

	@JsonProperty 
	private List<Square> occupiedSquares;

	private ShipType shipType;
	private int health;


	public Ship() {
		this("INVALID");
	}
	
	public Ship(String kind) {
		this.occupiedSquares = new ArrayList<Square>();
		try {
			this.shipType = ShipType.valueOf(kind);
		} catch(Exception e) {
			e.printStackTrace();
			this.shipType = ShipType.INVALID;
		}
		this.health = this.shipType.getValue();
	}

	public Ship(Ship other) {
		this.shipType = other.getShipType();
		this.occupiedSquares = new ArrayList<Square>();
		for(int i = 0; i < other.getOccupiedSquares().size(); i++) {
			Square s = other.getOccupiedSquares().get(i);
			s = new Square(s.getRow(), s.getColumn());
			this.occupiedSquares.add(s);
		}
		this.health = other.health;
	}

	public boolean collidesWith(Ship other) {
		for(Square s1 : this.occupiedSquares) {
			for(Square s2 : other.getOccupiedSquares()) {
				if(s1.getRow() == s2.getRow() && s1.getColumn() == s2.getColumn()) {
					return true;
				}
			}
		}
		return false;
	}

	public ShipType getShipType() {
		return this.shipType;
	}

	public int getHealth(){ 
		return this.health;
	}

	private void takeDamage(int x, char y){
		this.health-- ;
	}

	public void setOccupiedSquaresByOrientation(int row, char col, boolean verticle) {
		this.occupiedSquares.clear();

		for(int i = 0; i < this.shipType.getValue(); i++) {
			Square s;
			if(verticle) {
				// rows increase going down the page
				s = new Square(row + i, col);
			} else {
				// columns increase going to the right
				s = new Square(row, (char)(col + i));
			}
			this.occupiedSquares.add(s);
		}
	}

	public Result processAttack(int x, char y) {
		Result res = new Result();
		res.setLocation(new Square(x, y));
		res.setShip(this);

		// check if the ship was hit
		for(Square s : this.occupiedSquares) {
			if(s.getRow() == x && s.getColumn() == y) {
				// check for captains quarters

				res.setResult(AtackStatus.HIT);

				// handle capitans quarters
				if(true) {
					this.health--;
					if(this.health <= 0) {
						res.setResult(AtackStatus.SUNK);
					}
				} else {
					res.setResult(AtackStatus.SUNK);
				}

				
				return res;
			}
		}

		return null;
	}

	public List<Square> getOccupiedSquares() {
		return this.occupiedSquares;
	}
}
