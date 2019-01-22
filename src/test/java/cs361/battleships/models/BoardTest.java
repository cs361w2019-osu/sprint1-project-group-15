package cs361.battleships.models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;

import static org.junit.Assert.assertTrue;

public class BoardTest {
/*
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
        assertTrue(board.placeShip(new Ship("DESTROYER"), 6, 'D', true));
        assertFalse(board.placeShip(new Ship("MINESWEEPER"), 7, 'D', true));
    }

    @Test
    public void testSecondValidPlacement(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("DESTROYER"), 1, 'D', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 9, 'F', true));
    }

    @Test
    public void testCornerPlacement(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 1, 'A', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 10, 'I', false));
    }

    @Test
    public void testSameKindPlacement(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true));
        assertFalse(board.placeShip(new Ship("BATTLESHIP"), 7, 'D', true));
    }

    @Test
    public void testThreeValidPlacements(){
        Board board = new Board();
        assertTrue(board.placeShip(new Ship("BATTLESHIP"), 2, 'G', false));
        assertTrue(board.placeShip(new Ship("DESTROYER"), 6, 'D', true));
        assertTrue(board.placeShip(new Ship("MINESWEEPER"), 1, 'B', false));
    }
*/

    @Test
    public void testInvalidResult() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(16, 'B');
        Result res2 = board.attack(5, 'Z');

        System.out.println(res2.getResult());
        System.out.println(res.getResult());
    }

    @Test
    public void testHitAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(6, 'B');

        System.out.println(res.getResult());
    }

    @Test
    public void testMissAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(1, 'B');

        System.out.println(res.getResult());
    }

    @Test
    public void testSunkAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 4, 'D', true);
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        Result res = board.attack(1, 'B');
        Result res2 = board.attack(2, 'B');

        System.out.println(res2.getResult());
    }

    @Test
    public void testSurrenderAttack() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        Result res = board.attack(1, 'B');
        Result res2 = board.attack(2, 'B');

        System.out.println(res2.getResult());
    }
}

