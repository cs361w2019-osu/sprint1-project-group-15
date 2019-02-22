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

        Result result = board.attack(9, 'C');

        Square duplicateLocation = new Square(9, 'C');
        assertTrue(board.isDuplicateAttack(duplicateLocation));

        Square nonDuplicateLocation1 = new Square(8, 'C');
        assertFalse(board.isDuplicateAttack(nonDuplicateLocation1));

        Square nonDuplicateLocation2 = new Square(9, 'D');
        assertFalse(board.isDuplicateAttack(nonDuplicateLocation2));

        Square nonDuplicateLocation3 = new Square(6, 'B');
        assertFalse(board.isDuplicateAttack(nonDuplicateLocation3));
    }

    /* No longer needed since the implementation of captain's quarters
    @Test
    public void SunkenShipTest() {
        Board board = new Board();
        Ship testShip = new Ship("MINESWEEPER");
        board.placeShip(testShip, 9, 'C', true);

        Result firstAttack = board.attack(9, 'C');
        assertFalse(testShip.isSunk(board.getAttacks()));

        //this should be the second and last space the ship occupies. Once this is hit, it should sink the ship
        Result secondAttack = board.attack(10, 'C');
        assertTrue(testShip.isSunk(board.getAttacks()));
    }
    */
}
