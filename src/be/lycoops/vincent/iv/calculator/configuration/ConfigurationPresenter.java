package be.lycoops.vincent.iv.calculator.configuration;


import be.lycoops.vincent.iv.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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

    @Inject
    private NatureCalculator natureCalculator;

    private String ability = "";

    public void levelPlus() {
        pokemon.levelUp();
    }

    public void level10Plus() {
        for (int i = 0; i < 10; ++i) {
            pokemon.levelUp();
        }
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

    public void setL18Sr() {
        setBaseLevel(18, "sr");

        updateEffortValues();
    }

    public void setL18Sf() {
        setBaseLevel(18, "sf");

        updateEffortValues();
    }

    public void setL19() {
        setBaseLevel(19);

        updateEffortValues();
    }

    public void setBaseLevel(int level) {
        setBaseLevel(level, null);
    }

    public void setBaseLevel(int level, String ability) {

        this.ability = ability;

        pokemon.reset(level);
        natureCalculator.reset();
        pokemon.setHiddenPower(null);
        history.reset();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.setText("L" + pokemon.getLevel());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        updateEffortValues();
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getBaseLevel(), ability);
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
