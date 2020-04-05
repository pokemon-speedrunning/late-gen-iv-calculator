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
    private Button lv7;

    @FXML
    private Button lv8;

    @FXML
    private Button lv9;

    @FXML
    private Button lv10;

    @Inject
    private Pokemon pokemon;

    @Inject
    private GameService gameService;

    @Inject
    private History history;

    @Inject
    private NatureCalculator natureCalculator;

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

    public void setL7() {
        setBaseLevel(7);

        updateEffortValues();
    }

    public void setL8() {
        setBaseLevel(8);

        updateEffortValues();
    }

    public void setL9() {
        setBaseLevel(9);

        updateEffortValues();
    }

    public void setL10() {
        setBaseLevel(10);

        updateEffortValues();
    }

    public void setBaseLevel(int level) {
        Button[] buttons = {lv7, lv8, lv9, lv10};
        pokemon.reset(level);
        natureCalculator.reset();
        pokemon.setHiddenPower(null);
        history.reset();
        for (int i = 0; i < 4; ++i) {
            buttons[i].setDisable(i == level - 7);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.setText("L" + pokemon.getLevel());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
        lv7.setDisable(true);
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getBaseLevel());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
