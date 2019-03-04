package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Submarine extends Ship{

    public Submarine(){
        this.health = 2;
        this.size = 5;
        this.kind = "SUBMARINE";
    }

    public void populateSquares(int x, chary, boolean vert) {
        for(int i = 0; i<(this.size-1); i++){
			this.occupiedSquares.add(new Square(x,y));
			if(vert) x++;
			else y++;
		}

        this.captainsQuarters = occupiedSquares.get(3);
    }
}