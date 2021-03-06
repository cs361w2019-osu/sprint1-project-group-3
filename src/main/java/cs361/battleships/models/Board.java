package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.ShipFactory;
import java.util.ArrayList;
import java.util.List;

public class Board {


	private List<Ship> ships;               		
	private List<Result> attacks;   				
	private List<Sonar>  sonarpulses;

	@JsonProperty
	private Weapon currentWeapon;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
		this.sonarpulses = new ArrayList<Sonar>();

		this.currentWeapon = Weapon.CANNON;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		Ship toAdd = ShipFactory.Build(ship);
		toAdd.setSubmerged(ship.isSubmerged());
		toAdd.setOccupiedSquaresByOrientation(x, y, isVertical);

		if(toAdd.getShipType() == Ship.ShipType.INVALID) {
			return false;
		}

		// check that each occupied square is valid
		for(Square s : toAdd.getOccupiedSquares()) {
			if(0 >= s.getRow() || s.getRow() > 10) {
				return false;
			}
			if('A' > s.getColumn() || s.getColumn() > 'J') {
				return false;
			}
		}



		// check that there are no shipwise collisions
		// also check that the same board cannot have two of 
		// the same piece
		for(Ship incident : this.ships) {
			if(toAdd.collidesWith(incident))
				return false;
			if(toAdd.getShipType().equals(incident.getShipType()))
				return false;
		}

		
		this.ships.add(toAdd);

		return true;
	}

	public boolean placeSonar(int x, char y){
		Square sonarCenter = new Square(x,y);
		Sonar toSonarPulse = new Sonar();
		toSonarPulse.setCenter(sonarCenter);


		//check if it's being placed on the board in a valid manner
		if(1 > x || x > 10)
			return false;
		if('A' > y || y > 'J')
			return false;


		//check that it's not placed on top of any other Sonar Pulse
		for(Sonar other : this.sonarpulses){
			Square othercenter = other.getCenter();
			if(othercenter.equals(sonarCenter)){
				return false;
			}
		}

		// upper half of diamond
		for(int i = 0; i < 5; i++) {
			int width = 1 + 2 * ((i < 2) ? i : 4 - i);
			for(int j = -(width/2); j < (width/2)+1; j++) {
				Square target = new Square(x - 2 + i, (char)(y+j));
				if(target.getRow() < 1 || target.getRow() > 10 || target.getColumn() < 'A' || target.getColumn() > 'J')
					continue;


				boolean found = false;
				for(Ship s : ships) {
					for(Square sq : s.getOccupiedSquares()) {
						if (sq.equals(target) && !s.getCaptainsQuarters().equals(target)) {
							toSonarPulse.addShipSquare(sq);
							found = true;
						}
					}
				}

				if(!found) {
					toSonarPulse.addEmptySquare(target);
				}
			}
		}


		this.sonarpulses.add(toSonarPulse);

		return true;
	}

	public void moveShips(String direction) {
		for(int ii = 0; ii < this.getShips().size(); ii ++) {

			int x = this.getShips().get(0).getOccupiedSquares().get(0).getRow();
			char y = this.getShips().get(0).getOccupiedSquares().get(0).getColumn();

			boolean vert = false;
			if(x < this.getShips().get(0).getOccupiedSquares().get(1).getRow()) {
				vert = true;
			}

			switch (direction) {
				case "NORTH":
					if(x != 1) {
						if(!placeShip(this.getShips().get(ii),x - 1, y, vert)) {

						} else {
							this.getShips().remove(0);
						}
					}
					break;
				case "EAST":
					if(y != 'J') {
						if(!placeShip(this.getShips().get(ii),x, (char)(y + 1), vert)) {

						} else {
							this.getShips().remove(0);
						}
					}
					break;
				case "SOUTH":
					if(x != 10) {
						if(!placeShip(this.getShips().get(ii),x + 1, y, vert)) {

						} else {
							this.getShips().remove(0);
						}
					}
					break;
				case "WEST":
					if(y != 'A') {
						if(!placeShip(this.getShips().get(ii),x, (char)(y - 1), vert)) {

						} else {
							this.getShips().remove(0);
						}
					}
					break;
				default: System.out.println("you dun fucked up kid");
					break;
			}

		}
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	Function handles all attacking , so should
	- See if attack location is valid
	- if we've attacked there before
	- if we hit anything there, and set result accordingly = HIT
	- if that hit sunk the ship =  sunk
	- if that hit sunk the last ship = Surrender
	 */
	public Result attack(int x, char y) {

		Result attackResult = new Result();
		attackResult.setLocation(new Square(x, y));		//
		attackResult.setResult(AtackStatus.MISS);				//should default to Miss if not changed by any others
		
		//check if the player or computer targeted something on the screen
		if(x < 0 || x >10 || y < 'A' || y > 'J'){			//if attack attempt is outside bounds of board, set status to invalid
			attackResult.setResult(AtackStatus.INVALID);
			return attackResult;
		}

		for(Ship s : this.ships) {
			Result res = s.processAttack(x, y, this.currentWeapon);
			if(res != null) {
				attackResult = res;
				break;
			}
		}



		int totalHealth = 0;
		for(Ship s : ships){
			totalHealth+= s.getHealth();
		}
		if(totalHealth<=0){
			attackResult.setResult(AtackStatus.SURRENDER);
		}

		if(attackResult.getResult() == AtackStatus.SUNK){
			this.currentWeapon = Weapon.LASER;
		}

		this.attacks.add(attackResult);
		return attackResult;
	}

	public List<Ship> getShips() {
		return this.ships;
	}


	public void setShips(List<Ship> ships) {
		this.ships.clear();

		// ninja doesn't support inheritance very well(it doesn't support it period)
		for(Ship s : ships) {
			this.ships.add(ShipFactory.Duplicate(s));
		}

	}

	public void moveFleet(int dx, int dy) {

		//assume every ship is capable of moving at start
		for(Ship s : this.ships) {
			s.setCanMove(true);
		}

		//check if ship is moving out of bounds
		for (Ship s : this.ships) {
			s.move(dx, dy);
			if (!containsShip(s)) {
				s.setCanMove(false);
			}
			s.move(-dx, -dy);
		}

		//check if ship will collide with any ship that cannot move
		for (Ship s : this.ships) {
			if(s.getCanMove()) {
				s.move(dx, dy);
				for (Ship other : this.ships) {
					// only one of each ship
					if (!s.getShipType().equals(other.getShipType())) {
						if (s.collidesWith(other) && !other.getCanMove()) {
							s.setCanMove(false);
							break;
						}
					}
				}
				s.move(-dx, -dy);
			}
		}

		//move all ships that can move
		for(Ship s : this.ships) {
			if(s.getCanMove()) {
				s.move(dx,dy);
			}
		}
	}

	//checks if ship is in-bounds
	private boolean containsShip(Ship ship) {
		for(Square s : ship.getOccupiedSquares()) {
			if(1 > s.getRow() || s.getRow() > 10) {
				return false;
			}
			if('A' > s.getColumn() || s.getColumn() > 'J') {
				return false;
			}
		}
		return true;
	}

	/*public List<Square> getSonarpulses(){
		return this.sonarpulses;
	}*/
	public List<Sonar> getSonarpulses() {
		return this.sonarpulses;
	}

	public void setSonarpulses(List<Sonar> pulses){
		this.sonarpulses = pulses;
	}

	public List<Result> getAttacks() {
	    //should return all previous attacks
		return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}
}

