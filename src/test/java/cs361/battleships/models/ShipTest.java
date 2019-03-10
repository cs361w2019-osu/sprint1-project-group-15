package cs361.battleships.models;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class ShipTest {

    @Test
    public void testIsVertical()
    {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");
        testBoard.placeShip(test, 3, 'C', true);

        assertTrue(test.isVertical());
        testBoard.clearShips();

        Ship test2 = new Ship("MINESWEEPER");
        testBoard.placeShip(test2, 3, 'C', false);
        assertFalse(test2.isVertical());
    }

    @Test
    public void getMove()
    {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");
        testBoard.placeShip(test, 5, 'E', false);

        var proposedMove = test.getMove("north");
        assertFalse(proposedMove.get(0).equals(test.getOccupiedSquares().get(0)));
        assertFalse(proposedMove.get(1).equals(test.getOccupiedSquares().get(1)));

        proposedMove = test.getMove("west");
        assertFalse(proposedMove.get(0).equals(test.getOccupiedSquares().get(0)));
        assertFalse(proposedMove.get(1).equals(test.getOccupiedSquares().get(1)));

        proposedMove = test.getMove("east");
        assertFalse(proposedMove.get(0).equals(test.getOccupiedSquares().get(0)));
        assertFalse(proposedMove.get(1).equals(test.getOccupiedSquares().get(1)));

        proposedMove = test.getMove("south");
        assertFalse(proposedMove.get(0).equals(test.getOccupiedSquares().get(0)));
        assertFalse(proposedMove.get(1).equals(test.getOccupiedSquares().get(1)));
    }

    @Test
    public void testMoveShip() {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");
        testBoard.placeShip(test, 5, 'E', false);

        var proposedMove = test.getMove("north");
        test.move("north");
        assertTrue(test.getOccupiedSquares().get(0).getRow() == proposedMove.get(0).getRow());
        assertTrue(test.getOccupiedSquares().get(1).getRow() == proposedMove.get(1).getRow());

        proposedMove = test.getMove("south");
        test.move("south");
        assertTrue(test.getOccupiedSquares().get(0).getRow() == proposedMove.get(0).getRow());
        assertTrue(test.getOccupiedSquares().get(1).getRow() == proposedMove.get(1).getRow());

        proposedMove = test.getMove("east");
        test.move("east");
        assertTrue(test.getOccupiedSquares().get(0).getColumn() == proposedMove.get(0).getColumn());
        assertTrue(test.getOccupiedSquares().get(1).getColumn() == proposedMove.get(1).getColumn());

        proposedMove = test.getMove("west");
        test.move("west");
        assertTrue(test.getOccupiedSquares().get(0).getColumn() == proposedMove.get(0).getColumn());
        assertTrue(test.getOccupiedSquares().get(1).getColumn() == proposedMove.get(1).getColumn());

    }
}