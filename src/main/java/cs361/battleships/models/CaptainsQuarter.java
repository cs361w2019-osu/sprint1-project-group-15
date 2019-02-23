package cs361.battleships.models;

public class CaptainsQuarter extends Square {
    private int health;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void deductHealth() {
        this.health--;
    }
}
