package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;
	@JsonProperty private boolean Sunk;
	@JsonProperty private Square captainsQuarters;

	public Ship() {
		this.occupiedSquares = new ArrayList<>();
		this.kind = new String();
		this.Sunk = false;
		this.size=0;
	}

	public Ship(String kind) {
		this.kind = kind;
		this.size=0;
		if(kind.equals("MINESWEEPER")){
			this.Sunk = false;
			this.size=2;
		}
		else if(kind.equals("DESTROYER")){
			this.Sunk = false;
			this.size=3;
		}
		else if(kind.equals("BATTLESHIP")){
			this.Sunk = false;
			this.size=4;
		}
		this.occupiedSquares = new ArrayList<>();
	}

	public List<Square> getOccupiedSquares() {
		return this.occupiedSquares;
	}

	public String getKind() {
		return this.kind;
	}

	public void populateSquares(int x, char y, boolean vert) {
		for(int i = 0; i<this.size; i++){
			this.occupiedSquares.add(new Square(x,y));
			if(vert) x++;
			else y++;
		}
		createCaptainsQuarters();
	}

	public Square getCaptainsQuarters() { return this.captainsQuarters; }

	public void createCaptainsQuarters(){
		if(kind.equals("MINESWEEPER"))
			captainsQuarters = occupiedSquares.get(0);
		else if(kind.equals("DESTROYER"))
			captainsQuarters = occupiedSquares.get(1);
		else if(kind.equals("BATTLESHIP"))
			captainsQuarters = occupiedSquares.get(2);
	}

	public int getSize(){
		return this.size;
	}

	public boolean isSunk(List<Result> attacksOnBoard) {
		//counts the number of hits on a ship. If the hit count
		//is the same as the number of squares on the ship, we know the
		//ship has been sunk, otherwise it has not.
		int hits = 0;

		for(Square shipLocationSquare : this.getOccupiedSquares()) {
			//for every ship
			//loop through every attack on the board
			for(Result attack : attacksOnBoard) {
				if((shipLocationSquare.getColumn() == attack.getLocation().getColumn()) &&
						(shipLocationSquare.getRow() == attack.getLocation().getRow())) {
					hits = hits + 1;
				}
			}
		}

		if(hits == this.getSize()) {
			this.Sunk = true;
			return true;
		} else {
			return false;
		}
	}
}
