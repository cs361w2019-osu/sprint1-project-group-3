package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class SonarGameAction {

    @JsonProperty private Game game;
    @JsonProperty private int x;            //sonar center x and y
    @JsonProperty private char y;

    public Game getGame() {
        return game;                        //use game to get board to set sonar center and check surrounding squares
    }

    public int getActionRow() {
        return x;
    }

    public char getActionColumn() {
        return y;
    }
}