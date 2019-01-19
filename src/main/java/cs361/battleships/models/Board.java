package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<>();
		this.attacks = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		int shipSize = ship.getSize();
		if(!validShipType(ship)) return false;
		if(validLocation(shipSize, x, y, isVertical)){
			ship.populateSquares(x, y, isVertical);
			ships.add(ship);
			return true;
		}
		else{
			return false;
		}
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		return ships;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public List<Result> getAttacks() {
		//TODO implement
		return null;
	}

	public void setAttacks(List<Result> attacks) {
		//TODO implement
	}
	/*
	This function takes proposed ship coords and returns true or false based on whether the proposed location is valid
	 */
	private boolean validLocation(int size, int x, char y, boolean vertical){
		int intY = (int)y;
		if(vertical) x = x+size-1;
		else intY = intY+size-1;
		//if max range is outside grid, return false;
		if( x > 10 || intY > 73) return false;
		else {
			//now checking if new ship would overlap with existing ships
			for(Ship myShips : this.getShips()){
				for(Square mySquare : myShips.getOccupiedSquares()){
					//loop through all squares and test against proposed boundaries of new ship
					int currRow=mySquare.getRow();
					int currCol=(int)mySquare.getColumn();
					if(vertical) {
						if(intY == currCol) return false;
						if(currRow <= x && currRow >= x-size+1) return false;
					}
					else{
						if(x == currRow) return false;
						if(currCol <= intY && currCol >= y-size+1) return false;
					}
				}
			}
			//otherwise we make it through the tests and return true
			return true;
		}
	}
	private boolean validShipType(Ship ship){
		for(Ship myShips: this.getShips()){
			if(ship.getKind().equals(myShips.getKind())) return false;
		}
		return true;
	}
}
