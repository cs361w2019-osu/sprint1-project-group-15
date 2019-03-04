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
}