package cs361.battleships.models;

import org.junit.Assert;
import org.junit.Test;
import java.util.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class GameTest {

    @Test
    public void testFullGameSetup(){
        Game game = new Game();

        //4 ships placed
        assertTrue(game.placeShip(new Ship("MINDSWEEPER"), 2, 'D', true));
        assertTrue(game.placeShip(new Ship("DESTROYER"), 4, 'B', false));
        assertTrue(game.placeShip(new Ship("BATTLESHIP"), 5, 'C', false));
        assertTrue(game.placeShip(new Ship("submarine"), 7, 'E', false));

        //attack the 4 corners
        assertTrue(game.attack(1,'A'));
        assertTrue(game.attack(10,'A'));
        assertTrue(game.attack(10, 'J'));
        assertTrue(game.attack(1,'J'));



    }
}