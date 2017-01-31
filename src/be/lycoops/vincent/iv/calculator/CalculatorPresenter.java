package be.lycoops.vincent.iv.calculator;


import be.lycoops.vincent.iv.calculator.configuration.ConfigurationView;
import be.lycoops.vincent.iv.calculator.hiddenpower.HiddenPowerView;
import be.lycoops.vincent.iv.calculator.output.OutputView;
import be.lycoops.vincent.iv.calculator.reset.ResetView;
import be.lycoops.vincent.iv.calculator.statselector.StatSelectorView;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class CalculatorPresenter implements Initializable {

    @FXML
    private BorderPane pane;

    @FXML
    private GridPane centerGrid;

    @FXML
    private Label atkNatureLabel;

    @FXML
    private Label defNatureLabel;

    @FXML
    private Label spdNatureLabel;

    @FXML
    private Label spAtkNatureLabel;

    @FXML
    private Label spDefNatureLabel;

    private Map<String, Label> natureLabels = new HashMap<>();

    @Inject
    private Pokemon pokemon;

    @Inject
    private NatureCalculator natureCalculator;

    public void initialize(URL location, ResourceBundle resources) {
        pane.setTop(new HiddenPowerView().getView());
        pane.setLeft(new ConfigurationView().getView());
        pane.setRight(new ResetView().getView());
        pane.setBottom(new OutputView().getView());

        ObservableList<Node> children = centerGrid.getChildren();

        StatSelectorView hpSelector = new StatSelectorView(StatSelectorView.injectionStrategy("hp"));
        GridPane hpGrid = (GridPane) hpSelector.getView();
        GridPane.setConstraints(hpGrid, 1, 1);

        StatSelectorView atkSelector = new StatSelectorView(StatSelectorView.injectionStrategy("atk"));
        GridPane atkGrid = (GridPane) atkSelector.getView();
        GridPane.setConstraints(atkGrid, 2, 1);

        StatSelectorView defSelector = new StatSelectorView(StatSelectorView.injectionStrategy("def"));
        GridPane defGrid = (GridPane) defSelector.getView();
        GridPane.setConstraints(defGrid, 3, 1);

        StatSelectorView spAtkSelector = new StatSelectorView(StatSelectorView.injectionStrategy("spAtk"));
        GridPane spAtkGrid = (GridPane) spAtkSelector.getView();
        GridPane.setConstraints(spAtkGrid, 4, 1);

        StatSelectorView spDefSelector = new StatSelectorView(StatSelectorView.injectionStrategy("spDef"));
        GridPane spDefGrid = (GridPane) spDefSelector.getView();
        GridPane.setConstraints(spDefGrid, 5, 1);

        StatSelectorView spdSelector = new StatSelectorView(StatSelectorView.injectionStrategy("spd"));
        GridPane spdGrid = (GridPane) spdSelector.getView();
        GridPane.setConstraints(spdGrid, 6, 1);

        children.addAll(hpGrid, atkGrid, defGrid, spAtkGrid, spDefGrid, spdGrid);

        natureLabels.put("atk", atkNatureLabel);
        natureLabels.put("def", defNatureLabel);
        natureLabels.put("spd", spdNatureLabel);
        natureLabels.put("spAtk", spAtkNatureLabel);
        natureLabels.put("spDef", spDefNatureLabel);

        natureCalculator.minusNatureProperty().addListener((o, old, newNature) ->
                formatNatures(newNature, natureCalculator.plusNatureProperty().get()));

        natureCalculator.plusNatureProperty().addListener((o, old, newNature) ->
                formatNatures(natureCalculator.minusNatureProperty().get(), newNature));
    }

    private void formatNatures(String minusNature, String plusNature) {
        natureLabels.forEach((nature, label) -> {
            int value = 0;
            if (nature.equals(plusNature)) {
                value = 1;
            } else if (nature.equals(minusNature)) {
                value = -1;
            }
            label.setTextFill(getTextFillColor(value));
        });
    }

    private static Paint getTextFillColor(int value) {
        String color;
        switch (value) {
            case -1: color = "#79CEE3"; break;
            case 1: color = "#FF5555"; break;
            default: color = "#000000"; break;
        }
        return Paint.valueOf(color);
    }

    private void setNature(MouseEvent event, String nature) {
        MouseButton button = event.getButton();
        switch (button) {
            case PRIMARY: {
                natureCalculator.setPlusNature(nature);
            } break;
            case SECONDARY: {
                natureCalculator.setMinusNature(nature);
            } break;
            case MIDDLE: {
                natureCalculator.undoNature(nature);
            } break;
        }
        pokemon.setNature(natureCalculator.computeNature());
    }

    public void atkNature(MouseEvent event) {
        setNature(event, "atk");
    }

    public void defNature(MouseEvent event) {
        setNature(event, "def");
    }

    public void spdNature(MouseEvent event) {
        setNature(event, "spd");
    }

    public void spAtkNature(MouseEvent event) {
        setNature(event, "spAtk");
    }

    public void spDefNature(MouseEvent event) {
        setNature(event, "spDef");
    }
}
