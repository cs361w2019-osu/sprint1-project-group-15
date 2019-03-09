package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import static cs361.battleships.models.AttackStatus.*;

public class Game {

    @JsonProperty private Board playersBoard = new Board();
    @JsonProperty private Board opponentsBoard = new OpponentBoard();

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean successful = playersBoard.placeShip(new Ship(ship.getKind()), x, y, isVertical);
        if (!successful) return false;
        Ship oppShip = new Ship(ship.getKind());
        while (! (opponentsBoard.placeShip(oppShip, Board.randRow(), Board.randCol(), Board.randVertical())));
        return true;
    }

    /*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
    public boolean attack(int x, char  y) {
        Result playerAttack = opponentsBoard.attack(x, y);
        if (playerAttack.getResult() == INVALID) {
            return false;
        }
        playersBoard.attack(Board.randRow(), Board.randCol());
        return true;
    }
}
