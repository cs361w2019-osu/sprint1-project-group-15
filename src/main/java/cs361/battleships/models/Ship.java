package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Ship {

	@JsonProperty protected List<Square> occupiedSquares;
	@JsonProperty protected String kind;
	@JsonProperty protected int size;
	@JsonProperty protected Square captainsQuarters;
	@JsonProperty protected int health; // HP of captain's quarter
	@JsonProperty protected boolean sunk;
	@JsonProperty protected boolean submerged;

	public Ship() {
		this.occupiedSquares = new ArrayList<>();
		this.kind = new String();
		this.size=0;
		this.captainsQuarters = new Square();
		this.health=1;
		this.sunk=false;
		this.submerged=false;
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

	boolean isVertical()
	{
		return (this.getOccupiedSquares().get(0).getColumn() - this.getOccupiedSquares().get(1).getColumn() == 0);
	}

	public List<Square> getMove(String direction)
	{
		System.out.println("moving " + this.getKind() + " Is vertical: " + this.isVertical());
		List<Square> newSquares = new ArrayList<>();
		for(var coord : this.getOccupiedSquares())
		{
			switch(direction)
			{
				case "north":
					newSquares.add(new Square(coord.getRow() - 1, coord.getColumn()));
					break;
				case "south":
					newSquares.add(new Square(coord.getRow() + 1, coord.getColumn()));
					break;
				case "east":
					newSquares.add(new Square(coord.getRow(),  (char)((int) coord.getColumn() + 1)));
					break;
				case "west":
					newSquares.add(new Square(coord.getRow(), (char)((int) coord.getColumn() - 1)));
					break;
			}
		}
		return newSquares;
	}

	public Square getCaptainsQuarters() { return this.captainsQuarters; }

	public void createCaptainsQuarters(){
		if(kind.equals("MINESWEEPER")) {
			this.captainsQuarters = occupiedSquares.get(0);
		} else if(kind.equals("DESTROYER")) {
			this.captainsQuarters = occupiedSquares.get(1);
		} else if(kind.equals("BATTLESHIP")) {
			this.captainsQuarters = occupiedSquares.get(2);
		} else if(kind.toLowerCase().equals("submarine")) {
			this.captainsQuarters = occupiedSquares.get(3);
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

	public void move(String direction)
	{
		System.out.println(String.format("MOVING %s %s", this.getKind(), direction));
		for(var square : this.getOccupiedSquares())
		{
			switch(direction)
			{
				case "north":
					square.setRow(square.getRow() - 1);
					break;
				case "south":
					square.setRow(square.getRow() + 1);
					break;
				case "east":
					square.setColumn((char)((int)square.getColumn() + 1));
					break;
				case "west":
					square.setColumn((char)((int)square.getColumn() - 1));
					break;
				default:
					break;
			}
		}
		this.createCaptainsQuarters();
	}
}
