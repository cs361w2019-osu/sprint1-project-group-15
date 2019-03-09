package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Submarine extends Ship {

	public Submarine(String kind) {
		this.health = 2;
		this.size = 5;
		this.submerged=false;
		if(kind.equals("SUBMARINE")) this.submerged = true;
		else this.submerged = false;
		this.kind = kind.toUpperCase();
	}

	public Submarine(String kind, boolean submerged){
		this.health =2;
		this.size=5;
		this.kind = kind.toUpperCase();
		this.submerged = submerged;
	}

	public void populateSquares(int x, char y, boolean vert) {
		for (int i = 0; i < this.size - 1; i++) {
			this.occupiedSquares.add(new Square(x, y));
			if (vert) x++;
			else y++;
		}

		int tempRow = this.occupiedSquares.get(2).getRow();
		char tempChar = this.occupiedSquares.get(2).getColumn();

		if (vert)
			tempChar++;
		else
			tempRow--;

		this.occupiedSquares.add(new Square(tempRow, tempChar));
		this.captainsQuarters = occupiedSquares.get(3);
	}
}