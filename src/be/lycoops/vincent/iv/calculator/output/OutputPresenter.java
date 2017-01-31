package be.lycoops.vincent.iv.calculator.output;

import be.lycoops.vincent.iv.model.HiddenPowerCalculator;
import be.lycoops.vincent.iv.model.Nature;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
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

    private Map<String, Label> statRanges = new HashMap<>();

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

    private Map<String, Label> natureLabels = new HashMap<>();

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private Pokemon pokemon;

    public void makeNeutralNature() {
        natureCalculator.reset();
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
        statRanges.put("hp", hpIvRange);
        statRanges.put("atk", atkIvRange);
        statRanges.put("def", defIvRange);
        statRanges.put("spd", spdIvRange);
        statRanges.put("spAtk", spAtkIvRange);
        statRanges.put("spDef", spDefIvRange);


        natureLabels.put("atk", atkText);
        natureLabels.put("def", defText);
        natureLabels.put("spd", spdText);
        natureLabels.put("spAtk", spAtkText);
        natureLabels.put("spDef", spDefText);

        for (final String stat: Arrays.asList("hp", "atk", "def", "spd", "spAtk", "spDef")) {
            pokemon.getMinIndividualValues().get(stat).addListener((o, oldMin, newMin) -> asMinIv(stat, (int) newMin));
            pokemon.getMaxIndividualValues().get(stat).addListener((o, oldMax, newMax) -> asMaxIv(stat, (int) newMax));
        }

        pokemon.hiddenPowerProperty().addListener((o, oldHp, newHp) ->
            hiddenPowerLabel.setText(newHp == null ? "?" : newHp.getName()));

        pokemon.natureProperty().addListener((o, oldNature, newNature) ->
                nature.setText(newNature == null ? "?" : newNature.getName()));

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
            default: color = "white"; break;
        }
        return Paint.valueOf(color);
    }

    private void asMinIv(String stat, int min) {
        displayIndividualValues(stat, min, pokemon.getMaxIndividualValues().get(stat).get());
    }

    private void asMaxIv(String stat, int max) {
        displayIndividualValues(stat, pokemon.getMinIndividualValues().get(stat).get(), max);
    }

    private void displayIndividualValues(String stat, int min, int max) {
        statRanges.get(stat).setText(min == max ? String.valueOf(min) : (min + "-" + max));
    }
}
