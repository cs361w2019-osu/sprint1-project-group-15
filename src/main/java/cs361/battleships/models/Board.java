package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.Collections;
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
		if (validLocation(shipSize, x, y, isVertical)) {
			ship.populateSquares(x,y,isVertical);
			this.ships.add(ship);
			remainingShips++;
			return true;
		} else return false;
	}


	public void moveShips(String direction)
	{
		List<Ship> sortedShips = new ArrayList<>();
		Ship curShip = getDirectionmostShip(direction);
		var ships = this.getShips();
		while(curShip != null)
		{
			sortedShips.add(curShip);
			ships.remove(curShip);
			curShip = getDirectionmostShip(direction);
		}
		this.ships = sortedShips;

		for(var ship : this.getShips())
		{
			System.out.println(ship.getKind());
			var proposedMove = ship.getMove(direction);
			if(validMove(proposedMove, ship, this.getShips()))
			{
				ship.move(direction);
			}
			else
			{
				System.out.println("FALSE");
			}
		}
	}


//
//	if (x + size - 1 > 10 || (int)y > 74) {
//		return false;
//	}
//			for (int i = 0; i < size; i++) {
//		allProposedSquares.add(new Square(x + i, y));
//	}
//} else {
//		if ((int) y + size - 1 > 74 || x > 10) {
//		return false;
//		}
//		for (int i = 0; i < size; i++) {
//		allProposedSquares.add(new Square(x, (char) ((int) y + i)));
//		}
//		}

	private boolean validMove(List<Square> proposedMove, Ship proposedShip, List<Ship> ships)
	{
		for(var square : proposedMove)
		{
			if(square.getColumn() > 'J' || square.getColumn() < 'A')
			{
				System.out.println("FAILING COLUMN CHECK");
				return false;
			}
			if(square.getRow() > 10 || square.getRow() < 1)
			{
				System.out.println("FAILING ROW CHECK");
				return false;
			}
		}

		for(var propSq : proposedMove)
		{
			//loop through every ship
			for(var ship : ships)
			{
				//loop through every square in the ship
				for(var sq : ship.getOccupiedSquares())
				{
					//compare each square to the proposed square.
					if(propSq.getRow() == sq.getRow() && propSq.getColumn() == sq.getColumn())
					{
						if(!proposedShip.equals(ship))
						{
							return false;
						}
					}
				}
			}
		}


		return true;
	}

	public Ship getDirectionmostShip(String direction)
	{
		if(this.getShips().size() == 0)
		{
			return null;
		}

	    List<Ship> ships = this.getShips();
	    Ship directionMostShip = ships.get(0);
	    Square directionMostSquare = directionMostShip.getOccupiedSquares().get(0);
		for(var ship : ships)
		{
			for(var square : ship.getOccupiedSquares())
			{
				if(direction.contentEquals("north"))
				{
					System.out.println("NORTH");
					if(square.getRow() < directionMostSquare.getRow())
					{
						directionMostShip = ship;
						directionMostSquare = square;
					}
				}
				else if(direction.contentEquals("south"))
				{
					System.out.println("SOUTH");
					if(square.getRow() > directionMostSquare.getRow())
					{
						directionMostShip = ship;
						directionMostSquare = square;
					}
				}
				else if(direction.contentEquals("west"))
				{
					System.out.println("WEST");
					if(square.getColumn() < directionMostSquare.getColumn())
					{
						directionMostShip = ship;
						directionMostSquare = square;
					}
				}
				else if(direction.contentEquals("east"))
				{
					System.out.println("EAST");
					if(square.getColumn() > directionMostSquare.getColumn())
					{
						directionMostShip = ship;
						directionMostSquare = square;
					}
				}

			}
		}

	    return directionMostShip;
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
				allProposedSquares.add(new Square(x, (char) ((int) y + i)));
			}
		}
		//if max range is outside grid, return false;
		//now checking if new ship would overlap with existing ships
		if(this.getShips().size()>0) {
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
