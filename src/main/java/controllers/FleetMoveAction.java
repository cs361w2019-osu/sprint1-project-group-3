package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Game;

public class FleetMoveAction {

    @JsonProperty
    private Game game;

    @JsonProperty
    private int dx;

    @JsonProperty
    private int dy;


    public Game getGame() {
        return this.game;
    }

    public int getDX() {
        return this.dx;
    }

    public int getDY() {
        return this.dy;
    }
}
