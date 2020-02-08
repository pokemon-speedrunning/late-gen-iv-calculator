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
    private Button skwovet;

    @FXML
    private Button wooloo;

    @FXML
    private Button rookidee;

    @FXML
    private Button nickit;

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

    public void setSkwovet() {
        skwovet.setDisable(true);
        wooloo.setDisable(false);
        rookidee.setDisable(false);
        nickit.setDisable(false);
        updateEffortValues();
    }

    public void setWooloo() {
        skwovet.setDisable(false);
        wooloo.setDisable(true);
        rookidee.setDisable(false);
        nickit.setDisable(false);
        updateEffortValues();
    }

    public void setRookidee() {
        skwovet.setDisable(false);
        wooloo.setDisable(false);
        rookidee.setDisable(true);
        nickit.setDisable(false);
        updateEffortValues();
    }

    public void setNickit() {
        skwovet.setDisable(false);
        wooloo.setDisable(false);
        rookidee.setDisable(false);
        nickit.setDisable(true);
        updateEffortValues();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
        wooloo.setDisable(false);
        skwovet.setDisable(false);
        rookidee.setDisable(false);
        nickit.setDisable(false);
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;
        if (skwovet.isDisabled()) {
            additionalEv = Stat.HP;
        } else if (wooloo.isDisabled()) {
            additionalEv = Stat.DEF;
        } else if (rookidee.isDisabled()) {
            additionalEv = Stat.SPD;
        } else if (nickit.isDisabled()) {
            additionalEv = Stat.SP_DEF;
        }


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), additionalEv);
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
