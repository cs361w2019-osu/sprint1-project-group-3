package cs361.battleships.models;

//import cs361.battleships.ShipFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class SonarTest {

    @Test
    public void testInvalidPlacement(){
        Board board = new Board();
        assertFalse(board.placeSonar(11, 'C'));

        Board board2 = new Board();
        board2.placeSonar(5, 'C');
        assertFalse(board2.placeSonar(5, 'C'));
    }
}