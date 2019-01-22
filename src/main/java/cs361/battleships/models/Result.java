package cs361.battleships.models;

public class Result {

	// private members
	private AtackStatus result;
	private Square square;
	private Ship ship;

	// public methods
	public AtackStatus getResult() {
		return result;
	}

	public void setResult(AtackStatus result) {
		this.result = result;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

	public Square getLocation() {
		return square;
	}

	public void setLocation(Square square) {
		this.square = square;
	}
}
