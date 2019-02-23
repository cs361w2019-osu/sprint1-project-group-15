package cs361.battleships.models;

import org.hibernate.jpa.criteria.expression.NullLiteralExpression;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OpponentBoardTest {
    @Test
    public void testInvalidAttackCoords() {
        Board board = new OpponentBoard();

        //intentionally give it invalid coords
        //to check if it detects and randomizes them
        Result res = board.attack(11, 'Z');
        Result res2 = board.attack(-12, 'O');

        assertFalse(res.getResult() == AttackStatus.INVALID);
        assertFalse(res2.getResult() == AttackStatus.INVALID);

        //one invalid coord and one valid one
        Result res3 = board.attack(4, 'Z');
        Result res4 = board.attack(200, 'C');

        assertFalse(res3.getResult() == AttackStatus.INVALID);
        assertFalse(res4.getResult() == AttackStatus.INVALID);

    }

    @Test
    public void testInvalidPlacementCoords() {
        Board board = new OpponentBoard();
        Ship ship = new Ship("MINESWEEPER");

        boolean res = board.placeShip(ship, 100, 'Z', true);
        assertTrue(res);
        board.clearShips();

        res = board.placeShip(ship, 4, 'Z', true);
        assertTrue(res);
        board.clearShips();

        res = board.placeShip(ship, 400, 'C', true);
        assertTrue(res);
        board.clearShips();

        res = board.placeShip(ship, 4, 'C', true);
        assertTrue(res);
        board.clearShips();
    }
}
