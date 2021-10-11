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
    private Button route19;

    @FXML
    private Button route20;

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

    public void setRoute19() {
        setRoute("19");

        updateEffortValues();
    }

    public void setRoute20() {
        setRoute("20");

        updateEffortValues();
    }

    public void setRoute(String routeName) {

        pokemon.reset();
        natureCalculator.reset();
        pokemon.setHiddenPower(null);
        history.reset();

        pokemon.setRoute(routeName);

        route19.setDisable(routeName.equals("19"));
        route20.setDisable(routeName.equals("20"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.setText("L" + pokemon.getLevel());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        route19.setDisable(true);
        pokemon.routeProperty().addListener((o, oldRoute, newRoute) -> {
            route19.setDisable(newRoute.equals("19"));
            route20.setDisable(newRoute.equals("20"));
        });
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getRoute());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
