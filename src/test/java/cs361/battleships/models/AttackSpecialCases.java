package cs361.battleships.models;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;
public class AttackSpecialCases {

    @Test
    public void DuplicateAttackTest() {
        //This test sets up a board with one ship that has been attacked once. The attack on the board is a HIT on the ship
        //The test then goes on to make sure the board.isDuplicateAttack() function is properly checking for duplicates
        //by testing with a duplicate attack, and multiple non duplicate attacks that either have the same row or column
        //or neither.

        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 9, 'C', true));

        //TODO: replace with the attack function to tidy up the test
        //Once Attack has been implemented:
        //board.attack(9, 'C');

        AtackStatus status = AtackStatus.HIT;
        Square location = new Square(9, 'C');
        Ship target = board.getShips().get(0);

        Result result = new Result();
        result.setResult(status);
        result.setLocation(location);
        result.setShip(target);
        List<Result> list = new ArrayList<>();
        list.add(result);

        board.setAttacks(list);
        //End of TODO

        Square duplicateLocation = new Square(9, 'C');

        assertTrue(result.getLocation().getColumn() == duplicateLocation.getColumn());
        assertTrue(result.getLocation().getRow() == duplicateLocation.getRow());

        assertTrue(board.isDuplicateAttack(duplicateLocation));

        Square nonDuplicateLocation1 = new Square(8, 'C');

        assertFalse(board.isDuplicateAttack(nonDuplicateLocation1));

        Square nonDuplicateLocation2 = new Square(9, 'D');

        assertFalse(board.isDuplicateAttack(nonDuplicateLocation2));

        Square nonDuplicateLocation3 = new Square(6, 'B');

        assertFalse(board.isDuplicateAttack(nonDuplicateLocation3));
    }

    @Test
    public void SunkenShipTest() {
        Board board = new Board();
        Ship testShip = new Ship("MINESWEEPER");
        assertTrue(board.placeShip(testShip, 9, 'C', true));
        //ship coords are {(9, C) , (10, C)}

        //TODO: replace with the attack function to tidy up the test
        //Once Attack has been implemented:
        //board.attack(10, 'C');

        Result firstAttack = generateAttack(9, 'C', AtackStatus.HIT, testShip);

        assertFalse(testShip.isSunk(board.getAttacks()));

        //this should be the second and last space the ship occupies. Once this is hit, it should sink the ship
        Result secondAttack = generateAttack(10, 'C', AtackStatus.HIT, testShip);

        //assertTrue(testShip.isSunk(board.getAttacks()));
    }

    public Result generateAttack(int row, char col, AtackStatus attackResult, Ship targetShip) {
        AtackStatus status = attackResult;
        Square location = new Square(row, col);
        Ship target = targetShip;

        Result result = new Result();
        result.setResult(status);
        result.setLocation(location);
        result.setShip(target);
        return result;
    }

}
