package be.lycoops.vincent.iv.calculator.output;

import be.lycoops.vincent.iv.model.*;
import javafx.beans.property.IntegerProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Paint;

import javax.inject.Inject;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class OutputPresenter implements Initializable {

    @FXML
    private Label hpIvRange;

    @FXML
    private Label atkIvRange;

    @FXML
    private Label defIvRange;

    @FXML
    private Label spdIvRange;

    @FXML
    private Label spAtkIvRange;

    @FXML
    private Label spDefIvRange;

    @FXML
    private Label hiddenPowerLabel;

    @FXML
    private Label nature;

    private Map<Stat, Label> statRanges = new HashMap<>();

    @FXML
    private Label atkText;

    @FXML
    private Label defText;

    @FXML
    private Label spdText;

    @FXML
    private Label spAtkText;

    @FXML
    private Label spDefText;

    private Map<Stat, Label> natureLabels = new HashMap<>();

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private Pokemon pokemon;

    public void makeNeutralNature() {
        natureCalculator.reset();
        pokemon.getMinMinusIndividualValues().forEach((stat, integerProperty) -> {
            integerProperty.set(-1);
        });
        pokemon.getMinPlusIndividualValues().forEach((stat, integerProperty) -> {
            integerProperty.set(-1);
        });
        pokemon.getMaxPlusIndividualValues().forEach((stat, integerProperty) -> {
            integerProperty.set(-1);
        });
        pokemon.getMaxPlusIndividualValues().forEach((stat, integerProperty) -> {
            integerProperty.set(-1);
        });

        natureCalculator.addNeutralNature(Stat.ATK);
        natureCalculator.addNeutralNature(Stat.DEF);
        natureCalculator.addNeutralNature(Stat.SP_ATK);
        natureCalculator.addNeutralNature(Stat.SP_DEF);
        natureCalculator.addNeutralNature(Stat.SPD);
        pokemon.setNature(Nature.DOCILE);
    }

    public void changeHiddenPower(Event event) {
        if (event instanceof MouseEvent) {
            MouseEvent mouseEvent = (MouseEvent) event;
            MouseButton mouseButton = mouseEvent.getButton();
            if (mouseEvent.isShiftDown() || mouseButton.equals(MouseButton.SECONDARY)) {
                pokemon.setHiddenPower(hiddenPowerCalculator.updateToNext());
            } else if (mouseButton.equals(MouseButton.PRIMARY)) {
                pokemon.setHiddenPower(hiddenPowerCalculator.updateToPrevious());
            } else if (mouseButton.equals(MouseButton.MIDDLE)) {
                pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
            }
        } else if (event instanceof ScrollEvent) {
            double delta = ((ScrollEvent) event).getDeltaY();
            if (delta > 0) {
                pokemon.setHiddenPower(hiddenPowerCalculator.updateToNext());
            } else if (delta < 0) {
                pokemon.setHiddenPower(hiddenPowerCalculator.updateToPrevious());
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        statRanges.put(Stat.HP, hpIvRange);
        statRanges.put(Stat.ATK, atkIvRange);
        statRanges.put(Stat.DEF, defIvRange);
        statRanges.put(Stat.SPD, spdIvRange);
        statRanges.put(Stat.SP_ATK, spAtkIvRange);
        statRanges.put(Stat.SP_DEF, spDefIvRange);


        natureLabels.put(Stat.ATK, atkText);
        natureLabels.put(Stat.DEF, defText);
        natureLabels.put(Stat.SPD, spdText);
        natureLabels.put(Stat.SP_ATK, spAtkText);
        natureLabels.put(Stat.SP_DEF, spDefText);

        for (final Stat stat: Stat.ALL_STATS) {
            pokemon.getMinIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMaxIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            if (stat.equals(Stat.HP)) {
                continue;
            }
            pokemon.getMinMinusIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMaxMinusIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMinNeutralIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMaxNeutralIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMinPlusIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            pokemon.getMaxPlusIndividualValues().get(stat).addListener((_o, o, n) -> displayIndividualValues(stat));
            displayIndividualValues(stat);
        }

        pokemon.hiddenPowerProperty().addListener((o, oldHp, newHp) ->
                hiddenPowerLabel.setText(newHp == null ? "?" : newHp.getName()));

        pokemon.natureProperty().addListener((o, oldNature, newNature) ->
                nature.setText(newNature == null ? "?" : newNature.getName()));

        natureCalculator.minusNatureProperty().addListener((o, old, newNature) -> {
            formatNatures(newNature, natureCalculator.plusNatureProperty().get());
            for (final Stat stat: Stat.DEFAULT_STATS) {
                displayIndividualValues(stat);
            }
        });

        natureCalculator.plusNatureProperty().addListener((o, old, newNature) -> {
            formatNatures(natureCalculator.minusNatureProperty().get(), newNature);
            for (final Stat stat: Stat.DEFAULT_STATS) {
                displayIndividualValues(stat);
            }
        });


    }

    private void formatNatures(Stat minusNature, Stat plusNature) {
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
            default: color = "white"; break;
        }
        return Paint.valueOf(color);
    }

    private void displayIndividualValues(Stat stat) {

        int min = pokemon.getMinIndividualValues().get(stat).get();
        int max = pokemon.getMaxIndividualValues().get(stat).get();


        statRanges.get(stat).setText(min == max ? String.valueOf(min) : (min + "-" + max));

        if (stat.equals(Stat.HP)) {
            return;
        }

        boolean canBeMinus = pokemon.getMinMinusIndividualValues().get(stat).get() != -1;
        boolean canBeNeutral = pokemon.getMinNeutralIndividualValues().get(stat).get() != -1;
        boolean canBePlus = pokemon.getMinPlusIndividualValues().get(stat).get() != -1;

        String visualName = stat.getVisualName();

        Stat minusNature = natureCalculator.getMinusNature();
        Stat plusNature = natureCalculator.getPlusNature();

        if (canBeMinus && minusNature != null && !minusNature.equals(stat)) {
            canBeMinus = false;
        }

        if (canBePlus && plusNature != null && !plusNature.equals(stat)) {
            canBePlus = false;
        }

        int sum = (canBeMinus ? 1 : 0) + (canBeNeutral ? 1 : 0) + (canBePlus ? 1 : 0);


        if (sum > 1 && !stat.equals(minusNature) && !stat.equals(plusNature)) {
            if (canBeMinus && canBePlus) {
                visualName = "±" + visualName;
            } else if (canBeMinus) {
                visualName = "-" + visualName;
            } else if (canBePlus) {
                visualName = "+" + visualName;
            }
        }
        natureLabels.get(stat).setText(visualName);
    }
}
