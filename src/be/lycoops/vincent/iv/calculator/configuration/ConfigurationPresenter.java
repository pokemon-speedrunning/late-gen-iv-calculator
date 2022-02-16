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
    private Button lv16;

    @FXML
    private Button lv17;

    @FXML
    private Button lv18;

    @FXML
    private Button lv19;

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

    public void setL16() {
        setBaseLevel(16);

        updateEffortValues();
    }

    public void setL17() {
        setBaseLevel(17);

        updateEffortValues();
    }

    public void setL18() {
        setBaseLevel(18);

        updateEffortValues();
    }

    public void setL19() {
        setBaseLevel(19);

        updateEffortValues();
    }

    public void setBaseLevel(int level) {
        Button[] buttons = {lv16, lv17, lv18, lv19};
        pokemon.reset(level);
        natureCalculator.reset();
        pokemon.setHiddenPower(null);
        history.reset();
        for (int i = 0; i < 4; ++i) {
            buttons[i].setDisable(i == level - 16);
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
        lv16.setDisable(true);
        updateEffortValues();
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getBaseLevel());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
