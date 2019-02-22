package cs361.battleships.models;

import cs361.battleships.ShipFactory;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class ShipTest {

    @Test
    public void ShipsHaveTheCorrectType() {
        Ship s1 = ShipFactory.Build("BATTLESHIP");
        assertEquals(s1.getShipType(), Ship.ShipType.BATTLESHIP);

        Ship s2 = ShipFactory.Build("DESTROYER");
        assertEquals(s2.getShipType(), Ship.ShipType.DESTROYER);

        Ship s3 = ShipFactory.Build("MINESWEEPER");
        assertEquals(s3.getShipType(), Ship.ShipType.MINESWEEPER);
    }

    @Test
    public void ShipsHandleJunkType() {
        Ship s1 = ShipFactory.Build("asdfasdf");

        assertEquals(s1.getShipType(), Ship.ShipType.INVALID);
    } 

    @Test
    public void ShipsOccupyTheCorrectSquares() {
        Ship s1 = ShipFactory.Build("BATTLESHIP");
        s1.setOccupiedSquaresByOrientation(0, 'A', false);
        assertTrue(s1.getOccupiedSquares().size() == 4);
        assertTrue(s1.getOccupiedSquares().get(0).getColumn() == 'A');
        assertTrue(s1.getOccupiedSquares().get(1).getColumn() == 'B');
        assertTrue(s1.getOccupiedSquares().get(2).getColumn() == 'C');
        assertTrue(s1.getOccupiedSquares().get(3).getColumn() == 'D');

        Ship s2 = ShipFactory.Build("DESTROYER");
        s2.setOccupiedSquaresByOrientation(3, 'D', true);
        assertTrue(s2.getOccupiedSquares().size() == 3);
        assertTrue(s2.getOccupiedSquares().get(0).getRow() == 3);
        assertTrue(s2.getOccupiedSquares().get(1).getRow() == 4);
        assertTrue(s2.getOccupiedSquares().get(2).getRow() == 5);

        Ship s3 = ShipFactory.Build("MINESWEEPER");
        s3.setOccupiedSquaresByOrientation(2, 'C', false);
        assertTrue(s3.getOccupiedSquares().size() == 2);
        assertTrue(s3.getOccupiedSquares().get(0).getColumn() == 'C');
        assertTrue(s3.getOccupiedSquares().get(1).getColumn() == 'D');
    }

    @Test
    public void ShipsDetectCollsionsWithOtherShips() {
        Ship s1 = ShipFactory.Build("BATTLESHIP");
        s1.setOccupiedSquaresByOrientation(4, 'D', false);
        
        Ship s2 = ShipFactory.Build("MINESWEEPER");
        s2.setOccupiedSquaresByOrientation(3, 'D', true);

        Ship s3 = ShipFactory.Build("DESTROYER");
        s3.setOccupiedSquaresByOrientation(9, 'J', true);

        assertTrue(s1.collidesWith(s2));
        assertTrue(s2.collidesWith(s1));

        assertFalse(s2.collidesWith(s3));
    }

    @Test
    public void ShipCopyConstructorFunctionsCorrectly() {
        Ship s1 = ShipFactory.Build("BATTLESHIP");
        s1.setOccupiedSquaresByOrientation(3, 'C', true);

        Ship s2 = ShipFactory.Build(s1);

        assertEquals(s2.getShipType(), s1.getShipType());

        

    }
}