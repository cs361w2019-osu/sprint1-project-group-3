package cs361.battleships.models;


import cs361.battleships.ShipFactory;
import java.util.ArrayList;
import java.util.List;

public class Board {


	private List<Ship> ships;               		//List of current boards ships
	private List<Result> attacks;   				// List of all previous attack attempts
//	private List<Square> sonarpulses;				//locations of sonar pulses
	private List<Sonar>  sonarpulses;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<Ship>();
		this.attacks = new ArrayList<Result>();
		//this.sonarpulses = new ArrayList<Square>();
		this.sonarpulses = new ArrayList<Sonar>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		Ship toAdd = ShipFactory.Build(ship);
		toAdd.setOccupiedSquaresByOrientation(x, y, isVertical);	

		if(toAdd.getShipType() == Ship.ShipType.INVALID) {
			return false;
		}

		// check that each occupied square is valid
		for(Square s : toAdd.getOccupiedSquares()) {
			if(0 > s.getRow() || s.getRow() > 10) {
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
		//Sonar toSonarPulse = new Sonar(x, y);
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
			if(othercenter.getRow() == x || othercenter.getColumn() == y){
				return false;
			}
		}

		// upper half of diamond
		for(int i = 0; i < 3; i++) {
			int width = 1 + 2*i;
			for(int j = -(width/2)+1; j < (width/2); j++) {
				Square target = new Square(x - 3 + i, (char)(y+j));
				if(target.getRow() < 1 || target.getColumn() > 10 || target.getRow() < 'A' || target.getColumn() > 'J')
					continue;


				boolean found = false;
				for(Ship s : ships) {
					for(Square sq : s.getOccupiedSquares()) {
						if (sq.getRow() == target.getRow() && sq.getColumn() == target.getColumn()) {
							toSonarPulse.addShipSquare(sq);
							found = true;
						}
					}
				}

				if(!found) {
					toSonarPulse.addEmptySquare(target);
				}
				// check that the square is still on the board
			}
		}

		// go through ships
		// check if the sonar pulse touches the ship
		// add it to the with junk list
		// the without junk list is a list of squares
		// that are touched by the pulse, but do not have
		// any ships in them

		this.sonarpulses.add(toSonarPulse);

		return true;
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
			Result res = s.processAttack(x, y);
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

	/*public List<Square> getSonarpulses(){
		return this.sonarpulses;
	}*/
	public List<Sonar> getSonarpulses() {
		return this.sonarpulses;
	}
	//public void setSonarpulses(List<Square> pulses){
	//	this.sonarpulses = pulses;
	//}
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

