package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import java.util.Random;

import static cs361.battleships.models.AtackStatus.*;

public class Game {


    Logger logger = LoggerFactory.getLogger(Game.class);

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new Board();

    private Random random = new Random();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(ship, x, y, isVertical);
        if (!successful)
            return false;

        boolean opponentPlacedSuccessfully;
        do {
            // AI places random ships, so it might try and place overlapping ships
            // let it try until it gets it right
            opponentPlacedSuccessfully = opponentsBoard.placeShip(ship, randRow(), randCol(), randVertical());
        } while (!opponentPlacedSuccessfully);

        return true;
    }


    public boolean sonar(int x, char y){
        boolean successful = opponentsBoard.placeSonar(x, y);
        if(!successful)
            return false;

        return true;
    }
    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }

        Result opponentAttackResult;
        do {
            // AI does random attacks, so it might attack the same spot twice
            // let it try until it gets it right
            opponentAttackResult = playersBoard.attack(randRow(), randCol());
        } while(opponentAttackResult.getResult() == INVALID);

        return true;
    }

    public void moveFleet(int dx, int dy) {
        playersBoard.moveFleet(dx, dy);
    }

    private char randCol() {
        return (char)(65 + random.nextInt(75));
    }

    private int randRow() {
        return 1 + random.nextInt(10);
    }

    private boolean randVertical() {
        return random.nextBoolean();
    }
}
