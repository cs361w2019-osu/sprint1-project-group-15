package controllers;

import com.fasterxml.jackson.annotation.JsonProperty;
import cs361.battleships.models.Ship;
import cs361.battleships.models.Submarine;
import cs361.battleships.models.Game;

public class MovementGameAction {

    @JsonProperty private Game game;
    @JsonProperty private String direction;

    public Game getGame() {
        return game;
    }

    public void replaceSubmarine()
    {
       for(int i = 0; i < this.getGame().getPlayersBoard().getShips().size(); i++)
       {
           if(this.getGame().getPlayersBoard().getShips().get(i).getKind().toLowerCase() == "submarine")
           {
               Submarine sub = new Submarine(this.getGame().getPlayersBoard().getShips().get(i));
               sub.createCaptainsQuarters();
               this.getGame().getPlayersBoard().getShips().set(i, sub);
           }
       }
    }


    public String getDirection() { return direction; }
}