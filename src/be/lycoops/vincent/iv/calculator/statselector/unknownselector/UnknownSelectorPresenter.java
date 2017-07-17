package be.lycoops.vincent.iv.calculator.statselector.unknownselector;

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
import java.util.SortedSet;

public class UnknownSelectorPresenter implements Initializable {

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

        pokemon.getMinMinusIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());
        pokemon.getMaxMinusIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());

        pokemon.getMinNeutralIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());
        pokemon.getMaxNeutralIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());

        pokemon.getMinPlusIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());
        pokemon.getMaxPlusIndividualValues().get(stat).addListener((o, oldMin, newMin) -> formatButtons());

        pokemon.getEffortValues().get(stat).addListener((o, oldEv, newEv) -> formatButtons());

        pokemon.getAdditionalEffortValues().getEffortValue(stat).addListener((o, oldEv, newEv) -> {
            statEv.setText(String.valueOf(newEv));
            formatButtons();
        });

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
        pokemon.setKnownStat(stat, value);
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
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> formatButtons());
    }

    private void formatButtons() {

        SortedSet<Integer> stats = pokemon.getPossibleStats(stat);

        int index = 0;
        for (int stat: stats) {
            if (index == 32) {
                break;
            }
            Button button = statButtons.get(index++);
            button.setDisable(false);
            button.setText(String.valueOf(stat));
        }

        for (; index < 32; ++index) {
            Button button = statButtons.get(index);
            button.setDisable(true);
            button.setText("");
        }

    }
}
