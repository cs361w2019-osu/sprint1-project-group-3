package cs361.battleships.models;

import org.junit.Test;

import cs361.battleships.ShipFactory;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;



public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(ShipFactory.Build("MINESWEEPER"), 11, 'C', true));

        Board board2 = new Board();
        board2.placeShip(ShipFactory.Build("MINESWEEPER"), 0, 'D', true);
        assertFalse(board2.placeShip(ShipFactory.Build("DESTROYER"), 1, 'B', false));

        Board board3 = new Board();
        board3.placeShip(ShipFactory.Build("BATTLESHIP"), 0, 'A', true);
        assertFalse(board3.placeShip(ShipFactory.Build("BATTLESHIP"), 0, 'B', true));

        Board board4 = new Board();
        assertTrue(board4.placeShip(ShipFactory.Build("MINESWEEPER"), 9, 'A', false));

        Board board5 = new Board();
        assertFalse(board5.placeShip(ShipFactory.Build("asdsdfasdf"), 9, 'B', false));
    }

    @Test
    public void testValidPlacement() {
        Board b = new Board();
        assertTrue(b.placeShip(ShipFactory.Build("MINESWEEPER"), 1, 'A', true));
        assertTrue(b.placeShip(ShipFactory.Build("BATTLESHIP"), 1, 'B', true));
        assertTrue(b.placeShip(ShipFactory.Build("DESTROYER"), 1, 'C', true));
    }

    @Test
    public void testInvalidAttack(){
        Board board = new Board();
        board.placeShip(ShipFactory.Build("MINESWEEPER"), 5, 'C', true);

        Result r = board.attack(11, 'C');   //INVALID

        assertEquals(r.getResult(), AtackStatus.INVALID);

    }

    @Test
    public void testHitAttack(){
        Board board = new Board();
        board.placeShip(ShipFactory.Build("BATTLESHIP"), 4, 'D', true);

        Result r = board.attack(4, 'D'); //HIT
        assertEquals(AtackStatus.HIT, r.getResult());
    }


    @Test
    public void testSunkAttack(){
        Board board = new Board();
        board.placeShip(ShipFactory.Build("MINESWEEPER"), 4, 'A', false);
        board.placeShip(ShipFactory.Build("DESTROYER"), 5, 'A', false);
        board.placeShip(ShipFactory.Build("BATTLESHIP"), 6, 'A', false);

        Result r = board.attack(4, 'A');
        assertEquals(AtackStatus.SUNK, r.getResult());


        board.attack(5, 'A');
        board.attack(5, 'B');
        board.attack(5, 'C');
        Result r1 = board.attack(5, 'B');
        assertEquals(AtackStatus.SUNK, r1.getResult());

    }

    @Test
    public void testSurrenderAttack(){
        Board board = new Board();
        board.placeShip(ShipFactory.Build("MINESWEEPER"), 4, 'A', false);
        board.placeShip(ShipFactory.Build("DESTROYER"), 5, 'A', false);

        board.attack(4, 'A');
        board.attack(4, 'B');

        board.attack(5, 'A');
        board.attack(5, 'B');
        board.attack(5, 'C');
        Result r = board.attack(5, 'B');

        assertEquals(AtackStatus.SURRENDER, r.getResult());
    }

    @Test
    public void testMissAttack(){
        Board board = new Board();
        board.placeShip(ShipFactory.Build("MINESWEEPER"), 4, 'A', false);

        Result r = board.attack(1, 'A');

        assertEquals(AtackStatus.MISS, r.getResult());
    }

    @Test
    public void testInvalidMove() {
        Board board = new Board();
        board.placeShip(ShipFactory.Build("MINESWEEPER"), 1, 'A', false);
        board.placeShip(ShipFactory.Build("DESTROYER"), 2, 'A', false);

        //move north
        board.moveFleet(-1, 0);


    }
}
