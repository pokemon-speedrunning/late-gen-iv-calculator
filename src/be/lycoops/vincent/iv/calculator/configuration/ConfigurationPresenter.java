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
    private Button route1;

    @FXML
    private Button route2;

    @FXML
    private Button route3;

    @FXML
    private Button route4;

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

    public void setRoute1() {
        setRoute("1");

        updateEffortValues();
    }

    public void setRoute2() {
        setRoute("2");

        updateEffortValues();
    }

    public void setRoute3() {
        setRoute("3");

        updateEffortValues();
    }

    public void setRoute4() {
        setRoute("4");

        updateEffortValues();
    }

    public void setRoute(String routeName) {

//        pokemon.reset(level);
//        natureCalculator.reset();
//        pokemon.setHiddenPower(null);
//        history.reset();

        pokemon.setRoute(routeName);

        route1.setDisable(routeName.equals("1"));
        route2.setDisable(routeName.equals("2"));
        route3.setDisable(routeName.equals("3"));
        route4.setDisable(routeName.equals("4"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.setText("L" + pokemon.getLevel());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        route1.setDisable(true);
        pokemon.routeProperty().addListener((o, oldRoute, newRoute) -> {
            route1.setDisable(newRoute.equals("1"));
            route2.setDisable(newRoute.equals("2"));
            route3.setDisable(newRoute.equals("3"));
            route4.setDisable(newRoute.equals("4"));
        });
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getRoute());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
