package be.lycoops.vincent.iv.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class GameService {

    private ObjectProperty<Game> game = new SimpleObjectProperty<>(Game.MOON);

    public void setGame(Game game) {
        this.game.set(game);
    }

    public Game getGame() {
        return game.get();
    }

    public ObjectProperty<Game> gameProperty() {
        return game;
    }
}
