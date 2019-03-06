package controllers;

import com.google.inject.Singleton;
import cs361.battleships.models.Game;
import cs361.battleships.models.Ship;
import cs361.battleships.models.Sonar;
import cs361.battleships.ShipFactory;
import cs361.battleships.models.Submarine;
import ninja.Context;
import ninja.Result;
import ninja.Results;
import org.hibernate.jpa.criteria.expression.function.AggregationFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
        Ship ship = ShipFactory.Build(g.getShipType());

        if(g.isSubmerged() && ship instanceof Submarine) {
            ship.setSubmerged(true);
        }

        boolean result = game.placeShip(ship, g.getActionRow(), g.getActionColumn(), g.isVertical());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result sonar(Context context, SonarGameAction g) {
        Game game = g.getGame();
        boolean result = game.sonar(g.getActionRow(), g.getActionColumn());
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }

    public Result attack(Context context, AttackGameAction g) {
        Game game = g.getGame();
        boolean result = game.attack(g.getActionRow(), g.getActionColumn());
        System.out.println("asdfasdfasdf");
        if (result) {
            return Results.json().render(game);
        } else {
            return Results.badRequest();
        }
    }
}
