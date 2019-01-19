package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private List<Square> shipSquares;

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<>();
		this.attacks = new ArrayList<>();
		this.shipSquares = new ArrayList<>();
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		int shipSize = ship.getSize();
		if(this.getShips().size()>0) {
			if (!validShipType(ship)) return false;
		}
		if (validLocation(shipSize, x, y, isVertical)) {
			this.shipSquares.addAll(ship.populateSquares(x, y, isVertical));
			this.ships.add(ship);
			return true;
		}
		return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Result attack(int x, char y) {
		//TODO Implement
		return null;
	}

	public List<Ship> getShips() {
		return this.ships;
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
	private boolean validLocation(int size, int x, char y, boolean vertical) {
		List<Square> allProposedSquares = new ArrayList<>();
		//check max x and y and populate proposed squares depending on orientation
		if (vertical) {
			if (x + size - 1 > 10 || (int)y > 74) {
				return false;
			}
			for (int i = 0; i < size; i++) {
				allProposedSquares.add(new Square(x + i, y));
			}
		} else {
			if ((int) y + size - 1 > 74 || x > 10) {
				return false;
			}
			for (int i = 0; i < size; i++) {
				allProposedSquares.add(new Square(x, (char) ((int) y + 1)));
			}
		}
		//if max range is outside grid, return false;
		//now checking if new ship would overlap with existing ships
		if(this.getShips().size()>0) {
			for (int i=0; i<allProposedSquares.size(); i++) {
				for (int j=0; j<shipSquares.size(); j++) {
					if (!checkSquareConflict(allProposedSquares.get(i), shipSquares.get(j))) return false;
				}
			}
		}
		//otherwise we make it through the tests and return true
		return true;
	}

	private boolean validShipType(Ship ship) {
		for (Ship myShips : this.getShips()) {
			if (ship.getKind().equals(myShips.getKind())) return false;
		}
		return true;
	}

	private boolean checkSquareConflict(Square sq1, Square sq2) {
		if(sq1.getRow()==sq2.getRow()) return false;
		else if(sq1.getColumn()==sq2.getColumn()) return false;
		else return true;
	}

	public void addShipSquare(Square sq){
		shipSquares.add(sq);
	}
}
