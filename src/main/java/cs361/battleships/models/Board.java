package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Board {

	@JsonProperty private List<Ship> ships;
	@JsonProperty private List<Result> attacks;
	@JsonProperty private int remainingShips;
	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public Board() {
		this.ships = new ArrayList<>();
		this.attacks = new ArrayList<>();
		this.remainingShips = 0;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.
	 */
	public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
		int shipSize = ship.getSize();
		if(this.getShips().size()>0) {
			if (!validShipType(ship)) return false;
		}
		boolean submerged = false;
		if(ship instanceof Submarine) submerged = ((Submarine) ship).submerged;
		if (validLocation(shipSize, x, y, isVertical, submerged)) {
			ship.populateSquares(x,y,isVertical);
			this.ships.add(ship);
			remainingShips++;
			return true;
		} else return false;
	}

	/*
	DO NOT change the signature of this method. It is used by the grading scripts.

	Looks for the square and changes the result
	Will later be split into multiple functions for HIT/MISS/INVALID
	 */
	public Result attack(int x, char y) {
		//Initialize square with desired coordinates
		Square sq1 = new Square(x,y);

		//Initialize result with status TBD
		Result res = new Result();
		res.setLocation(sq1);
		//If ship is present on coordinates

		if(isDuplicateAttack(sq1) && !isCaptainsQuarter(x,y) || (isCaptainsQuarter(x,y) && !(this.getShip(x,y) == null) && this.getShip(x,y).isSunk()) ) {
			res.setResult(AttackStatus.INVALID);
			return res;
		}
		this.attacks.add(res);
		if(shipPresent(x,y)){
			res.setShip(getShip(x,y));

			if (!isCaptainsQuarter(x,y) ) {
				res.setResult(AttackStatus.HIT);
			} else if (isCaptainsQuarter(x,y) && res.getShip().isSunk()) {
				res.setResult(AttackStatus.SUNK);
				remainingShips--;
				if (!this.shipsLeft()) {
					res.setResult(AttackStatus.SURRENDER);
				}
			} else res.setResult(AttackStatus.MISS);
		}

		//If input is out of bounds or incorrect. Needs to include duplicate attack as well.
		else if((x>10 || x<0) || (y > 'J' || y < 'A')){
			res.setResult(AttackStatus.INVALID);
		}
		//If no ship is on the space
		else {
			res.setResult(AttackStatus.MISS);
		}
		return res;
	}

	public boolean isDuplicateAttack(Square attackLocation) {
		//loops through every attack on the board and checks if one already exists at the current location
		//returns true if another attack at the location exists, false if not
		for(Result attack : this.getAttacks()) {
			if((attack.getLocation().getRow() == attackLocation.getRow()) &&
				(attack.getLocation().getColumn() == attackLocation.getColumn())) {
				return true;
			}
		}
		return false;
	}

	public List<Ship> getShips() {
		return this.ships;
	}

	//Needs isSunk for implementation
	protected boolean shipsLeft(){
		return remainingShips > 0;
	}

	//Returns the ship on a given coordinate
	private Ship getShip(int x, char y){
		Square sq = new Square(x, y);
		for(Ship ships : this.getShips()){
			for(Square squares : ships.getOccupiedSquares()){
				if(isSquareConflict(sq, squares))
					return ships;
			}
		}
		return null;
	}

	public void setShips(List<Ship> ships) {
		this.ships = ships;
	}

	public void clearShips()
	{
		this.ships.clear();
		this.remainingShips = 0;
	}


	public List<Result> getAttacks() {
		return this.attacks;
	}

	public void setAttacks(List<Result> attacks) {
		this.attacks = attacks;
	}

	// Helper function to check if a coordinate is a captain's quarter
	public boolean isCaptainsQuarter(int x, char y) {
		Square sq = new Square(x, y);
		for(Ship ships : this.getShips()) {
			if (isSquareConflict(sq, ships.getCaptainsQuarters())) {
				return true;
			}
		}

		return false;
	}

	/*
	This function checks if a ship is present, returns true or false
	 */
	private boolean shipPresent(int x, char y){
		Square sq = new Square(x, y);
		for(Ship ships : this.getShips()){
			for(Square squares : ships.getOccupiedSquares()) {
				if (isSquareConflict(sq, squares)) {
					if (isSquareConflict(sq, ships.getCaptainsQuarters())) {
						ships.deductHealth();
					}
					return true;
				}
			}
		}
		return false;
	}

	/*
	This function takes proposed ship coords and returns true or false based on whether the proposed location is valid
	 */
	private boolean validLocation(int size, int x, char y, boolean vertical, boolean submerged) {
		List<Square> allProposedSquares = new ArrayList<>();
		//if size = 5, it's a sub, so we need to modify the size and set a flag, we'll use an integer flag to modify conditionals on the fly
		int isSub = 0;
		if(size == 5) {
			isSub = 1;
			size = 4;
		}
		//check max x and y and populate proposed squares depending on orientation
		if (vertical) {
			if (x + size - 1 > 10 || (int) y > (74 - isSub)) {
				return false;
			}
			for (int i = 0; i < size; i++) {
				allProposedSquares.add(new Square(x + i, y));
			}
			//if this is a sub, we'll manually add the "nub", complete with ugly casting to integer and back to char
			if(isSub==1) allProposedSquares.add(new Square(x+2,(char)((int)y+1)));
		} else {
			//expanding the bounds checking for when isSub=1
			if ((int) y + size - 1 > 74 || x > 10 || (x-isSub) < 1) {
				return false;
			}
			for (int i = 0; i < size; i++) {
				allProposedSquares.add(new Square(x, (char) ((int) y + i)));
			}
			//if its a sub, add the nub
			if(isSub==1) allProposedSquares.add(new Square(x-1, (char)((int)y+2)));
		}

		//if max range is outside grid, return false;
		//now checking if new ship would overlap with existing ships
		//if it's submerged we'll skip this part, we know our sub is always placed last, so we use this to cut down the work we do here
		if(this.getShips().size()>0 && !submerged) {
			for(Ship ships : this.getShips()){
				for(Square sq : ships.getOccupiedSquares()) {
					for (int i=0; i<allProposedSquares.size(); i++) {
						if (isSquareConflict(allProposedSquares.get(i), sq)) return false;
					}
				}
			}
		}
		//otherwise we make it through the tests and return true
		return true;
	}

	private boolean validShipType(Ship ship) {
		for (Ship ships : this.getShips()){
			if(ship.getKind().equals(ships.getKind())) return false;
		}
		return true;
	}

	public static boolean isSquareConflict(Square sq1, Square sq2) {
		return (sq1.getRow()==sq2.getRow() && sq1.getColumn()==sq2.getColumn());
	}

	public static char randCol() {
		//return 'A' to 'J'
		return (char)(Math.random()*10+65);
	}

	public static int randRow() {
		//return 1-10
		return (int)(Math.random()*10+1);
	}

	public static boolean randVertical() {
		return (Math.random() >=.5);
	}
}
