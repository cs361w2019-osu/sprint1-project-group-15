package cs361.battleships.models;

public class OpponentBoard extends Board {

    @Override
    //uses the original placeShip function but retries until it is placed properly.
    public boolean placeShip(Ship ship, int x, char y, boolean isVertical) {
        boolean placed;
        do {
            placed = super.placeShip(ship, randRow(), randCol(), randVertical());
        } while(!placed);
        return placed;
    }

    @Override
    public Result attack(int x , char y) {
        Result res = super.attack(x, y);
        while(res.getResult() == AttackStatus.INVALID)
        {
            res = super.attack(randRow(), randCol());
        }
        return res;
    }
}
