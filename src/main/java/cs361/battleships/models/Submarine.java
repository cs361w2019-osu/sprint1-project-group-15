package cs361.battleships.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class Submarine extends Ship{

    public Submarine(){
        setHealth(2);
        setSize(5);
        setKind("SUBMARINE");
    }
}