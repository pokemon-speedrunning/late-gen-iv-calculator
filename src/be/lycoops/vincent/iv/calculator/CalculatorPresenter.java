package be.lycoops.vincent.iv.calculator;


import be.lycoops.vincent.iv.calculator.configuration.ConfigurationView;
import be.lycoops.vincent.iv.calculator.hiddenpower.HiddenPowerView;
import be.lycoops.vincent.iv.calculator.output.OutputView;
import be.lycoops.vincent.iv.calculator.reset.ResetView;
import be.lycoops.vincent.iv.calculator.statselector.StatSelectorView;
import be.lycoops.vincent.iv.model.History;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
import be.lycoops.vincent.iv.model.Stat;
import javafx.beans.property.IntegerProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class CalculatorPresenter implements Initializable {

    @FXML
    private BorderPane pane;

    @Inject
    private Pokemon pokemon;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private History history;

    public void initialize(URL location, ResourceBundle resources) {
        pane.setTop(new HiddenPowerView().getView());
        pane.setLeft(new ConfigurationView().getView());
        pane.setRight(new ResetView().getView());
        pane.setBottom(new OutputView().getView());
        pane.setCenter(new StatSelectorView().getView());
    }

    public void setScene(Scene scene) {
        final KeyCombination ctrlZCombination = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN);
        scene.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (ctrlZCombination.match(event)) {
                undo();
            }
        });
    }

    private void undo() {
        History.HistoryAction action = history.undo();
        if (action == null) {
            return;
        }
        if (action instanceof History.StatAction) {
            History.StatAction statAction = (History.StatAction) action;
            pokemon.setStatRange(statAction);
        } else if (action instanceof History.Evolution) {
            pokemon.unevolve();
        } else if (action instanceof History.EvAdded) {
            History.EvAdded evAction = (History.EvAdded) action;
            Stat stat = evAction.getStat();
            IntegerProperty statProperty = pokemon.getAdditionalEffortValues().getEffortValue(stat);
            statProperty.set(statProperty.get() - 1);
        }
    }
}
