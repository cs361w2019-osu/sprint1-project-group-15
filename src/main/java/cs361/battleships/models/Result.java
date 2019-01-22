package cs361.battleships.models;

public class Result {

	private AtackStatus status;
	private Square location;
	private Ship ship;

	public AtackStatus getResult() {
		//TODO implement
		return status;
	}

	public void setResult(AtackStatus result) {
		//TODO implement
		this.status = result;
	}

	public Ship getShip() {
		//TODO implement
		return ship;
	}

	public void setShip(Ship ship) {
		//TODO implement
		this.ship = ship;
	}

	public Square getLocation() {
		//TODO implement
		return location;
	}

	public void setLocation(Square square) {
		//TODO implement
		this.location = square;
	}
}
