package be.lycoops.vincent.iv.calculator.configuration;


import be.lycoops.vincent.iv.model.*;
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

    @Inject
    private Pokemon pokemon;

    @Inject
    private GameService gameService;

    @Inject
    private History history;

    public void levelPlus() {
        pokemon.levelUp();
    }

    public void levelMinus() {
        pokemon.levelDown();
    }

    public void evolve() {
        pokemon.evolve();
        history.addEvolution();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
    }

    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(gameService.getGame(), pokemon.getLevel());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
