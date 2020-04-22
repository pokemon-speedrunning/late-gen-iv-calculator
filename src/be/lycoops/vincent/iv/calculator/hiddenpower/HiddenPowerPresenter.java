package be.lycoops.vincent.iv.calculator.hiddenpower;


import be.lycoops.vincent.iv.model.HiddenPower;
import be.lycoops.vincent.iv.model.HiddenPowerCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
import be.lycoops.vincent.iv.model.Stat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javax.inject.Inject;
import java.math.RoundingMode;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class HiddenPowerPresenter implements Initializable {

    @FXML
    private List<Label> hiddenPowerLabels;

    @FXML
    private ImageView feelsBadMan;

    @Inject
    private Pokemon pokemon;

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        for (final Stat stat: Stat.ALL_STATS) {
            pokemon.getMinIndividualValues().get(stat).addListener((obs, old, n) -> displayProbabilities());
            pokemon.getMaxIndividualValues().get(stat).addListener((obs, old, n) -> displayProbabilities());
        }

        try {
            feelsBadMan.setImage(new Image(getClass().getResource("../../rsz_feels.png").openStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayProbabilities() {
        final DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        final DecimalFormat df = new DecimalFormat("", symbols);
        df.setMaximumFractionDigits(1);
        df.setRoundingMode(RoundingMode.HALF_UP);
        df.setMinimumIntegerDigits(1);
        Map<HiddenPower, Double> hiddenPowers = hiddenPowerCalculator.computeHiddenPower(pokemon);
        double badHiddenPower = 0;
        for (final HiddenPower hiddenPower: HiddenPower.values()) {
            double probability = hiddenPowers.get(hiddenPower);
            hiddenPowerLabels.get(hiddenPower.ordinal()).setText(hiddenPower.getName() + ": " + df.format(probability * 100) + "%");
            if (hiddenPower.equals(HiddenPower.WATER) || hiddenPower.equals(HiddenPower.GRASS)) {
                badHiddenPower+= probability;
            }
        }
        feelsBadMan.setVisible(badHiddenPower >= 0.375);
    }
}
