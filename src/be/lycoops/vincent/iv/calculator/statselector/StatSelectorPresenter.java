package be.lycoops.vincent.iv.calculator.statselector;


import be.lycoops.vincent.iv.calculator.statselector.knownselector.KnownSelectorPresenter;
import be.lycoops.vincent.iv.calculator.statselector.knownselector.KnownSelectorView;
import be.lycoops.vincent.iv.calculator.statselector.unknownselector.UnknownSelectorPresenter;
import be.lycoops.vincent.iv.calculator.statselector.unknownselector.UnknownSelectorView;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
import be.lycoops.vincent.iv.model.Stat;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Paint;

import javax.inject.Inject;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StatSelectorPresenter implements Initializable {

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

    private Map<Stat, Label> natureLabels = new HashMap<>();

    private Map<Stat, GridPane> known = new HashMap<>();

    private Map<Stat, GridPane> unknown = new HashMap<>();

    @Inject
    private Pokemon pokemon;

    @Inject
    private NatureCalculator natureCalculator;

    private void formatNatures(final Stat minusNature, final Stat plusNature) {
        for (final Stat stat: Stat.DEFAULT_STATS) {
            formatNature(stat, minusNature, plusNature);
        }
    }

    private void formatNature(Stat stat, Stat minusNature, Stat plusNature) {
        int value = 0;
        if (stat.equals(plusNature)) {
            value = 1;
        } else if (stat.equals(minusNature)) {
            value = -1;
        }
        natureLabels.get(stat).setTextFill(getTextFillColor(value));

        ObservableList<Node> children = centerGrid.getChildren();
        children.remove(known.get(stat));
        children.remove(unknown.get(stat));
        children.add((mustShowKnown(stat) ? known : unknown).get(stat));
    }

    private boolean mustShowKnown(Stat stat) {

        if (pokemon.getNature() != null) {
            return true;
        }

        if (stat.equals(natureCalculator.getMinusNature()) || stat.equals(natureCalculator.getPlusNature())) {
            return true;
        }

        if (natureCalculator.neutralNaturesProperty().contains(stat)){
            return true;
        }

        int possible = 0;
        int minus = pokemon.getMinMinusIndividualValues().get(stat).get();
        int neutral = pokemon.getMinNeutralIndividualValues().get(stat).get();
        int plus = pokemon.getMinPlusIndividualValues().get(stat).get();

        if (minus != -1) ++possible;
        if (neutral != -1) ++possible;
        if (plus != -1) ++possible;

        // Covers neutral case.
        return possible == 1;
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

    private void setNature(MouseEvent event, Stat stat) {
        MouseButton button = event.getButton();
        switch (button) {
            case PRIMARY: {
                if (natureCalculator.getPlusNature() == null) {
                    natureCalculator.setPlusNature(stat);
                }
            } break;
            case SECONDARY: {
                if (natureCalculator.getMinusNature() == null) {
                    natureCalculator.setMinusNature(stat);
                }
            } break;
//            case MIDDLE: {
//                natureCalculator.undoNature(stat);
//            } break;
        }
        pokemon.setNature(natureCalculator.computeNature());
    }

    public void atkNature(MouseEvent event) {
        setNature(event, Stat.ATK);
    }

    public void defNature(MouseEvent event) {
        setNature(event, Stat.DEF);
    }

    public void spdNature(MouseEvent event) {
        setNature(event, Stat.SPD);
    }

    public void spAtkNature(MouseEvent event) {
        setNature(event, Stat.SP_ATK);
    }

    public void spDefNature(MouseEvent event) {
        setNature(event, Stat.SP_DEF);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<Node> children = centerGrid.getChildren();

        KnownSelectorView hpSelector = new KnownSelectorView();
        GridPane hpGrid = (GridPane) hpSelector.getView();
        GridPane.setConstraints(hpGrid, 1, 1);
        children.add(hpGrid);
        ((KnownSelectorPresenter) hpSelector.getPresenter()).setStat(Stat.HP);


        int columnIndex = 2;
        for (final Stat stat: Stat.DEFAULT_STATS) {
            KnownSelectorView knownSelectorView = new KnownSelectorView();
            ((KnownSelectorPresenter) knownSelectorView.getPresenter()).setStat(stat);

            UnknownSelectorView unknownSelectorView = new UnknownSelectorView();
            ((UnknownSelectorPresenter) unknownSelectorView.getPresenter()).setStat(stat);

            GridPane grid = (GridPane) knownSelectorView.getView();
            GridPane.setConstraints(grid, columnIndex, 1);
            known.put(stat, grid);

            grid = (GridPane) unknownSelectorView.getView();
            GridPane.setConstraints(grid, columnIndex++, 1);
            unknown.put(stat, grid);

            children.add(grid);
        }

        natureLabels.put(Stat.ATK, atkNatureLabel);
        natureLabels.put(Stat.DEF, defNatureLabel);
        natureLabels.put(Stat.SPD, spdNatureLabel);
        natureLabels.put(Stat.SP_ATK, spAtkNatureLabel);
        natureLabels.put(Stat.SP_DEF, spDefNatureLabel);

        natureCalculator.minusNatureProperty().addListener((o, old, newNature) ->
                formatNatures(newNature, natureCalculator.plusNatureProperty().get()));

        natureCalculator.neutralNaturesProperty().addListener((o, old, n) ->
                formatNatures(natureCalculator.minusNatureProperty().get(), natureCalculator.plusNatureProperty().get()));

        natureCalculator.plusNatureProperty().addListener((o, old, newNature) ->
                formatNatures(natureCalculator.minusNatureProperty().get(), newNature));

        pokemon.natureProperty().addListener((o, old, newNature) ->
                formatNatures(natureCalculator.minusNatureProperty().get(), natureCalculator.plusNatureProperty().get()));
    }
}
