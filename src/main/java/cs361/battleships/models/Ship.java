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
		SUBMARINE(5),
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
	protected List<Square> occupiedSquares;
	protected ShipType shipType;
	protected int health;
	protected boolean cqHit;
	protected Square captainsQuarters;
	protected boolean canMove;

	public Ship() {
		this("INVALID");
	}
	
	public Ship(String kind) {
		this.cqHit = false;
		this.canMove = true;
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
		this.cqHit = other.getcqHit();
		this.canMove = other.getCanMove();
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
				// are the squares occupining the same location and at the same depth
				if(s1.equals(s2)) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean getcqHit() {
		return this.cqHit;
	}

	public void setcqHit(boolean cq) {
		this.cqHit = cq;
	}

	public ShipType getShipType() {
		return this.shipType;
	}

	public int getHealth(){ 
		return this.health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public void setOccupiedSquaresByOrientation(int row, char col, boolean vertical) {
		this.occupiedSquares.clear();

		for(int i = 0; i < this.shipType.getValue(); i++) {
			Square s;
			if(vertical) {
				// rows increase going down the page
				s = new Square(row + i, col);
			} else {
				// columns increase going to the right
				s = new Square(row, (char)(col + i));
			}
			this.occupiedSquares.add(s);
		}
		if(occupiedSquares.size() > 0) {
			this.captainsQuarters = new Square(
					occupiedSquares.get(occupiedSquares.size() - 2).getRow(),
					occupiedSquares.get(occupiedSquares.size() - 2).getColumn());
		}
	}

	public Result processAttack(int x, char y, Weapon weapon) {
		Result res = new Result();
		res.setLocation(new Square(x, y));
		res.setShip(this);

		// check if the ship was hit
		for(Square s : this.occupiedSquares) {
			if(s.getRow() == x && s.getColumn() == y) {

				// if the weapon is a space laser, a hit counts as death
				if(weapon == Weapon.LASER) {
					res.setResult(AtackStatus.SUNK);
					this.health = 0;
					for(Square sq : occupiedSquares)
						sq.setHit(true);
					return res;
				}
				
				if(s.getHit()) {
					res.setResult(AtackStatus.INVALID);
					return res;
				}

				res.setResult(AtackStatus.HIT);
				s.setHit(true);

				Square cq = getCaptainsQuarters();
				if(s.getRow() == cq.getRow() && s.getColumn() == cq.getColumn()) {
					if(!cqHit) {
						res.setResult(AtackStatus.MISS);
						s.setHit(false);
						this.cqHit = true;
						return res;
					} else {
						res.setResult(AtackStatus.SUNK);
						this.health = 0;
						for(Square sq : occupiedSquares) 
                        	sq.setHit(true);
						return res;
					}
				}
				
				if(!s.getHit())
					this.health--;
				if(this.health <= 0) {
					res.setResult(AtackStatus.SUNK);
				}

				return res;
			}
		}

		return null;
	}

	public void move(int dx, int dy) {
		for(Square s : this.occupiedSquares) {
			s.setRow(s.getRow() + dx);
			s.setColumn((char)(s.getColumn() + dy));
		}
	}

	public Square getCaptainsQuarters() {
		return this.captainsQuarters;
	}

	public void setCaptainsQuarters(Square s) {
		this.captainsQuarters = s;
	}

	public List<Square> getOccupiedSquares() {
		return this.occupiedSquares;
	}

	public boolean isSubmerged() {
		boolean result = true;
		// likely sufficient to just check 1, but be thorough
		for(Square s : this.occupiedSquares) {
			result &= s.getSubmerged();
		}
		return result;
	}

	public void setSubmerged(boolean b) {
		for(Square s : this.occupiedSquares)
			s.setSubmerged(b);
	}

	public void setCanMove(boolean b) {
		this.canMove = b;
	}

	public boolean getCanMove() {
		return this.canMove;
	}
}
