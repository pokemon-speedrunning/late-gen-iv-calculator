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
    private Button route2;

    @FXML
    private Button route4;

    @FXML
    private Button route5;

    @FXML
    private Button route8;

    @FXML
    private Button route8k;

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

    public void setRoute2() {
        setRoute("2");

        updateEffortValues();
    }

    public void setRoute4() {
        setRoute("4");

        updateEffortValues();
    }

    public void setRoute5() {
        setRoute("5");

        updateEffortValues();
    }

    public void setRoute8() {
        setRoute("8");

        updateEffortValues();
    }

    public void setRoute8K() {
        setRoute("8K");

        updateEffortValues();
    }

    public void setRoute(String routeName) {

//        pokemon.reset(level);
//        natureCalculator.reset();
//        pokemon.setHiddenPower(null);
//        history.reset();

        pokemon.setRoute(routeName);

        route2.setDisable(routeName.equals("2"));
        route4.setDisable(routeName.equals("4"));
        route5.setDisable(routeName.equals("5"));
        route8.setDisable(routeName.equals("8"));
        route8k.setDisable(routeName.equals("8K"));
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        level.setText("L" + pokemon.getLevel());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> {
            level.setText("L" + newLevel);
            updateEffortValues();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> evolved.setDisable(isEvolved));
        route2.setDisable(true);
        pokemon.routeProperty().addListener((o, oldRoute, newRoute) -> {
            route2.setDisable(newRoute.equals("2"));
            route4.setDisable(newRoute.equals("4"));
            route5.setDisable(newRoute.equals("5"));
            route8.setDisable(newRoute.equals("8"));
            route8k.setDisable(newRoute.equals("8K"));
        });
    }


    private void updateEffortValues() {
        Map<Stat, IntegerProperty> effortValues = pokemon.getEffortValues();
        Stat additionalEv = null;


        Map<Stat, Integer> newEffortValues = EffortValueProvider.getEffortValues(pokemon.getLevel(), pokemon.getRoute());
        effortValues.forEach((stat, effortValue) -> effortValue.set(newEffortValues.get(stat)));
    }
}
