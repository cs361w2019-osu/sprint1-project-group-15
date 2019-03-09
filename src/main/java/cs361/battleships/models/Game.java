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
        boolean successful;
        String shipKind = ship.getKind();
        if(shipKind.toUpperCase().equals("SUBMARINE")) {
            successful = playersBoard.placeShip(new Submarine(shipKind,ship.submerged), x, y, isVertical);
        }
        else
            successful = playersBoard.placeShip(new Ship(shipKind), x, y, isVertical);
        if (!successful) return false;

        Ship oppShip;
        if(shipKind.toUpperCase().equals("SUBMARINE"))
            oppShip = new Submarine(shipKind,ship.submerged);
        else
            oppShip = new Ship(shipKind);
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
