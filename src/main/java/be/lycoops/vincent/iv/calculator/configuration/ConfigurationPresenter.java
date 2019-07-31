package be.lycoops.vincent.iv.calculator.configuration;


import be.lycoops.vincent.iv.model.EffortValueProvider;
import be.lycoops.vincent.iv.model.History;
import be.lycoops.vincent.iv.model.Pokemon;
import be.lycoops.vincent.iv.model.Stat;
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

    @Inject
    private Pokemon pokemon;

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
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
    }

    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
