package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

public class BoardTest {

    @Test
    public void testInvalidPlacement() {
        Board board = new Board();
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 11, 'C', true));

    }

    @Test
    public void testFirstValidPlacement() {
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 9, 'C', true));
    }

    @Test
    public void testOverlapPlacement() {
        Board board = new Board();
        board.placeShip(new Ship("DESTROYER"), 6, 'D', true);
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 7, 'D', true));
    }
}
