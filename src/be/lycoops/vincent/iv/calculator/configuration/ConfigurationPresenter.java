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

    @FXML
    private Button evolved;

    @FXML
    private Button onixFail;

    @FXML
    private Button onixSuccess;

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

    public void setOnixFail() {
        onixFail.setDisable(true);
        onixSuccess.setDisable(false);
        updateEffortValues();
    }

    public void setOnixSuccess() {
        onixFail.setDisable(false);
        onixSuccess.setDisable(true);
        updateEffortValues();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
        onixSuccess.setDisable(true);
        onixFail.setDisable(false);
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), onixSuccess.isDisabled());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
