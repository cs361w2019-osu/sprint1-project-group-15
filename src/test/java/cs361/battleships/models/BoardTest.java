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

    @Test
    public void testInvalidResult() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(16, 'B');
        Result res2 = board.attack(5, 'Z');

        assertTrue(res2.getResult() == AttackStatus.INVALID);
        assertTrue(res.getResult() == AttackStatus.INVALID);
    }

    @Test
    public void testHitAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(6, 'B');

        assertTrue(res.getResult() == AttackStatus.HIT);
    }

    @Test
    public void testMissAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 6, 'B', true);
        Result res = board.attack(1, 'B');

        assertTrue(res.getResult() == AttackStatus.MISS);
    }

    @Test
    public void testSunkAttack() {
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 4, 'D', true);
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        Result res = board.attack(1, 'B');
        Result res2 = board.attack(2, 'B');

        assertTrue(res.getResult() == AttackStatus.SUNK);
    }

    @Test
    public void testSurrenderAttack() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        Result res = board.attack(1, 'B');
        Result res2 = board.attack(2, 'B');

        assertTrue(res.getResult() == AttackStatus.SURRENDER);
    }

    @Test
    public void testClearShips() {
        Board board = new Board();
        board.placeShip(new Ship("MINESWEEPER"), 3, 'C', true);

        assertFalse(board.getShips().isEmpty());
        assertTrue(board.shipsLeft());

        board.clearShips();

        assertTrue(board.getShips().isEmpty());
        assertFalse(board.shipsLeft());

    }

    @Test
    public void testCaptainsQuarters(){
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 4, 'D', true);
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        board.placeShip(new Ship("DESTROYER"),3,'F',false);
        board.placeShip(new Submarine("SUBMARINE"), 8,'A', false);

        for(Ship ships : board.getShips()){
            if((ships.getKind() == "BATTLESHIP") &&(ships.getCaptainsQuarters().getRow() == 6) && ships.getCaptainsQuarters().getColumn() == 'D')
                System.out.println("Captain's Quarters on Battleship Found");
            if((ships.getKind() == "MINESWEEPER") &&(ships.getCaptainsQuarters().getRow() == 1) && ships.getCaptainsQuarters().getColumn() == 'B')
                System.out.println("Captain's Quarters on Minesweeper Found");
            if(ships.getKind() == "DESTROYER" && ships.getCaptainsQuarters().getRow() == 3 && ships.getCaptainsQuarters().getColumn() == 'G')
                System.out.println("Captain's Quarters on Destroyer Found");
            if(ships.getKind() == "SUBMARINE" && ships.getCaptainsQuarters().getRow() == 8 && ships.getCaptainsQuarters().getColumn() == 'D')
                System.out.println("Captain's Quarters on Submarine Found");
        }
    }

    // Test attacks on the captain's quarters
    @Test
    public void testAttackingCaptainsQuarters(){
        Board board = new Board();
        Result res;

        board.placeShip(new Ship("BATTLESHIP"), 4, 'D', true);
        board.placeShip(new Ship("MINESWEEPER"), 1, 'B', true);
        board.placeShip(new Ship("DESTROYER"),3,'F',false);

        // attack captain's quarters and print the results
        System.out.println("Results from attacking captain's quarters:");

        res = board.attack(6,'D');
        System.out.println(res.getResult() );
        res = board.attack(6,'D');
        System.out.println(res.getResult() );

        res = board.attack(3,'G');
        System.out.println(res.getResult() );
        res = board.attack(3,'G');
        System.out.println(res.getResult() );

        res = board.attack(1,'B');
        System.out.println(res.getResult() );
    }

    @Test
    public void testSubmarinePlacement(){
        Board board = new Board();
        board.placeShip(new Submarine("SUBMARINE"), 8,'A', false);
        for(Ship ships : board.getShips()) {
            if (ships.getOccupiedSquares().get(4).getRow() == 7 && ships.getOccupiedSquares().get(4).getColumn() == 'C')
                System.out.println("Submarine Placement Good");
        }
    }

    @Test
    public void testVerticalSubmarinePlacement(){
        Board board = new Board();
        board.placeShip(new Submarine("SUBMARINE"), 1,'A', true);
        for(Ship ships : board.getShips()) {
            if (ships.getOccupiedSquares().get(4).getRow() == 3 && ships.getOccupiedSquares().get(4).getColumn() == 'B')
                System.out.println("Submarine Vertical Placement Good");
        }
    }

    @Test
    public void testAllowedDuplicateAttack(){
        Game game = new Game();
        //with 2 ships, duplicate attacks should be allowed, as the space laser is active
        assertTrue(game.placeShip(new Ship("MINDSWEEPER"), 2, 'D', true));
        assertTrue(game.placeShip(new Ship("DESTROYER"), 4, 'B', false));
        assertTrue(game.attack(5,'D'));
        assertTrue(game.attack(5,'D'));
    }

    @Test
    //Place a submerged submarine directly under another ship
    public void testSubmergedSubmarinePlacement(){
        Board board = new Board();
        board.placeShip(new Ship("BATTLESHIP"), 5, 'B', false);
        assertTrue(board.placeShip(new Submarine("SUBMARINE", true),5,'B', false));

    }

    @Test
    public void testValidMove()
    {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");

        //this is a valid placement
        assertTrue(testBoard.placeShip(test, 2, 'B', true));
        var proposedMove = test.getMove("west");
        assertTrue(testBoard.validMove(test.getMove("west"), test, testBoard.getShips()));
        assertTrue(testBoard.validMove(test.getMove("north"), test, testBoard.getShips()));
        assertTrue(testBoard.validMove(test.getMove("south"), test, testBoard.getShips()));
        assertTrue(testBoard.validMove(test.getMove("east"), test, testBoard.getShips()));

        test.move("north");
        assertFalse(testBoard.validMove(test.getMove("north"), test, testBoard.getShips()));
        test.move("west");
        assertFalse(testBoard.validMove(test.getMove("west"), test, testBoard.getShips()));
    }

    @Test
    public void testGetDirectionmostShip()
    {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");
        Ship test2 = new Ship("BATTLESHIP");

        assertTrue(testBoard.placeShip(test, 3, 'C', true));
        assertTrue(testBoard.placeShip(test2, 9, 'C', false));

        //northernmostShip is expected to be "MINESWEEPER"
        //southernmostShip is expected to be "BATTLESHIP"
        //easternmostShip is expected to be "BATTLESHIP"
        assertTrue(testBoard.getDirectionmostShip("north").getKind().equals("MINESWEEPER"));
        assertTrue(testBoard.getDirectionmostShip("south").getKind().equals("BATTLESHIP"));
        assertTrue(testBoard.getDirectionmostShip("east").getKind().equals("BATTLESHIP"));
    }

    @Test
    public void testMoveShips()
    {
        Board testBoard = new Board();
        Ship test = new Ship("MINESWEEPER");
        assertTrue(testBoard.placeShip(test, 2, 'B', true));

        Square sq1 = new Square();
        sq1.setColumn(test.getOccupiedSquares().get(0).getColumn());
        sq1.setRow(test.getOccupiedSquares().get(0).getRow());

        Square sq2 = new Square();
        sq2.setColumn(test.getOccupiedSquares().get(1).getColumn());
        sq2.setRow(test.getOccupiedSquares().get(1).getRow());

        test.move("east");

        assertTrue(test.getOccupiedSquares().get(0).getColumn() != sq1.getColumn());
        assertTrue(test.getOccupiedSquares().get(1).getColumn() != sq2.getColumn());

        test.move("north");

        assertTrue(test.getOccupiedSquares().get(0).getRow() != sq1.getRow());
        assertTrue(test.getOccupiedSquares().get(1).getRow() != sq2.getRow());
    }

}

