package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty private List<Square> occupiedSquares;
	@JsonProperty private String kind;
	@JsonProperty private int size;
	@JsonProperty private Square captainsQuarters;
	@JsonProperty private int health; // HP of captain's quarter
	@JsonProperty private boolean sunk;

	public Ship() {
		this.occupiedSquares = new ArrayList<>();
		this.kind = new String();
		this.size=0;
		this.captainsQuarters = new Square();
		this.health=1;
		this.sunk=false;
	}

	public Ship(String kind) {
		this.kind = kind;
		this.sunk=false;
		this.size=0;
		if(kind.equals("MINESWEEPER")){
			this.size=2;
			this.health=1;
		}
		else if(kind.equals("DESTROYER")){
			this.size=3;
			this.health=2;
		}
		else if(kind.equals("BATTLESHIP")){
			this.size=4;
			this.health=2;
		}
		this.occupiedSquares = new ArrayList<>();
	}

	public void setHealth(int health) { this.health = health; }
	public void setSize(int size) { this.size = size; }
	public void setKind(String kind) { this.kind = kind; }

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
		if(kind.equals("MINESWEEPER")) {
			this.captainsQuarters = occupiedSquares.get(0);
		} else if(kind.equals("DESTROYER")) {
			this.captainsQuarters = occupiedSquares.get(1);
		} else if(kind.equals("BATTLESHIP")) {
			this.captainsQuarters = occupiedSquares.get(2);
		}
	}

	public int getSize(){
		return this.size;
	}

	public void deductHealth() {
		this.health--;
		if(this.health==0) this.sunk=true;
	}

	public boolean isSunk() {
		if (this.health == 0) { this.sunk=true; }
		else { this.sunk=false; }
		return this.sunk;
	}
}
