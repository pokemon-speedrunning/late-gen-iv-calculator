package be.lycoops.vincent.iv.calculator.statselector.knownselector;

import be.lycoops.vincent.iv.model.History;
import be.lycoops.vincent.iv.model.NatureCalculator;
import be.lycoops.vincent.iv.model.Pokemon;
import be.lycoops.vincent.iv.model.Stat;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class KnownSelectorPresenter implements Initializable {

    @FXML
    private List<Button> statButtons;

    @FXML
    private Label statEv;

    @Inject
    private Pokemon pokemon;

    @Inject
    private History history;

    private Stat stat;

    @Inject
    private NatureCalculator natureCalculator;

    public void setStat(Stat stat) {
        this.stat = stat;
        pokemon.getMinIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());
        pokemon.getMaxIndividualValues().get(stat).addListener((o, oldMax, newMax) -> formatButtons());
        pokemon.getEffortValues().get(stat).addListener((o, oldEv, newEv) -> formatButtons());
        pokemon.getAdditionalEffortValues().getEffortValue(stat).addListener((o, oldEv, newEv) -> {
            statEv.setText(String.valueOf(newEv));
            formatButtons();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> formatButtons());
        formatButtons();
    }

    public void setStat(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.isDisabled()) {
            return;
        }
        int value = Integer.valueOf(button.getText());

        int min = pokemon.getMinIndividualValues().get(stat).get();
        int max = pokemon.getMaxIndividualValues().get(stat).get();
        history.addStat(stat, min, max);

        if (!event.isShiftDown()) {
            pokemon.setKnownStat(stat, value);
        } else {
            pokemon.setKnownIv(stat, statButtons.indexOf(button));
        }
    }

    public void statUp() {
        pokemon.addAdditionalEffortValue(stat);
        history.addEvAdded(stat);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> formatButtons());
        natureCalculator.plusNatureProperty().addListener((o, oldPlus, newPlus) -> formatButtons());
        natureCalculator.minusNatureProperty().addListener((o, oldMinus, newMinus) -> formatButtons());
    }

    private void formatButtons() {
        int min = pokemon.getMinIndividualValues().get(stat).get();
        int max = pokemon.getMaxIndividualValues().get(stat).get();
        for (int i = 0; i < 32; ++i) {
            Button button = statButtons.get(i);
            button.setDisable(i < min || i > max);
            button.setText(String.valueOf(pokemon.getExpectedStat(stat, i)));
        }
    }
}
