package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.Game;
import cs361.battleships.models.Ship;
import cs361.battleships.models.Submarine;
import ninja.Context;
import ninja.Result;
import ninja.Results;

@Singleton
public class ApplicationController {

    public Result index() {
        return Results.html();
    }

    public Result newGame() {
        Game g = new Game();
        return Results.json().render(g);
    }

    public Result placeShip(Context context, PlacementGameAction g) {
        Game game = g.getGame();
        Ship ship;
        // submerged flag is determined by case -- see doShipPlacement() in game.js
        if(g.getShipType().equals("SUBMARINE") || g.getShipType().equals("submarine")) {
            ship = new Submarine(g.getShipType());
        }

        else{
            ship = new Ship(g.getShipType());
        }
        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result attack(Context context, AttackGameAction g) {
        Game game = g.getGame();
        boolean result = game.attack(g.getActionRow(), g.getActionColumn());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public static Result moveShips(Context context, MovementGameAction action) {
        System.out.println(action.getDirection());
        action.getGame().getPlayersBoard().moveShips(action.getDirection());
        return Results.json().render(action.getGame());
    }
}
