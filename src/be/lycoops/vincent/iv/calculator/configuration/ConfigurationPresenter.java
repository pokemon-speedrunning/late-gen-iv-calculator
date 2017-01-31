package be.lycoops.vincent.iv.calculator.configuration;


import be.lycoops.vincent.iv.model.EffortValueProvider;
import be.lycoops.vincent.iv.model.Game;
import be.lycoops.vincent.iv.model.GameService;
import be.lycoops.vincent.iv.model.Pokemon;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import javax.inject.Inject;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class ConfigurationPresenter implements Initializable {

    @FXML
    private Label level;

    @FXML
    private Button evolved;

    @FXML
    private Button sun;

    @FXML
    private Button moon;

    @Inject
    private Pokemon pokemon;

    @Inject
    private GameService gameService;

    public void levelPlus() {
        pokemon.levelUp();
    }

    public void levelMinus() {
        pokemon.levelDown();
    }

    public void evolve() {
        pokemon.evolve();
    }

    public void setSun() {
        gameService.setGame(Game.SUN);
    }

    public void setMoon() {
        gameService.setGame(Game.MOON);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
        gameService.gameProperty().addListener((o, old, newGame) -> {
            setGame(newGame);
        });
        moon.setDisable(true);
    }

    private void setGame(Game game) {
        if (game.equals(Game.MOON)) {
            sun.setDisable(false);
            moon.setDisable(true);
        } else {
            sun.setDisable(true);
            moon.setDisable(false);
        }
        updateEffortValues();
    }

    private void updateEffortValues() {
        Map<String, IntegerProperty> effortValues = pokemon.getEffortValues();
        Map<String, Integer> newEffortValues = EffortValueProvider.getEffortValues(gameService.getGame(), pokemon.getLevel());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
