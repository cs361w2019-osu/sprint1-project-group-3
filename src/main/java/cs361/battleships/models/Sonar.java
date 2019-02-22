package cs361.battleships.models;

import java.util.ArrayList;
import java.util.List;

public class Sonar {

    private Square center;
    private List<Square> revealedShips;                     //list of squares with ships
    private List<Square> revealedSquares;                   //list of squares without ships


    public Sonar(){
        //this.center = new Square();
        this.revealedSquares = new ArrayList<Square>();
        this.revealedShips = new ArrayList<Square>();
    }

    public Square getCenter(){
        return this.center;
    }

    public void setCenter(Square newSquare){
        this.center = newSquare;
    }

    public List<Square> getRevealedShips() {
        return this.revealedShips;
    }

    public void setRevealedShips(List<Square> revealedShips) {
        this.revealedShips = revealedShips;
    }

    public List<Square> getRevealedSquares() {
        return this.revealedSquares;
    }

    public void setRevealedSquares(List<Square> revealedSquares) {
        this.revealedSquares = revealedSquares;
    }

    public void addShipSquare(Square ship){
        revealedShips.add(ship);
    }

    public void addEmptySquare(Square junk){
        revealedSquares.add(junk);
    }

}