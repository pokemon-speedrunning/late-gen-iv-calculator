package be.lycoops.vincent.iv.calculator.statselector;

import be.lycoops.vincent.iv.model.Pokemon;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import javax.inject.Inject;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StatSelectorPresenter implements Initializable {

    @FXML
    private List<Button> statButtons;

    @FXML
    private Label statEv;

    @Inject
    private Pokemon pokemon;

    @Inject
    private String stat;

    public void setStat(MouseEvent event) {
        Button button = (Button) event.getSource();
        if (button.isDisabled()) {
            return;
        }
        int value = Integer.valueOf(button.getText());
        // TODO append to history
        if (!event.isShiftDown()) {
            pokemon.setKnownStat(stat, value);
        } else {
            pokemon.setKnownIv(stat, statButtons.indexOf(button));
        }
    }

    public void statUp() {
        pokemon.addAdditionalEffortValue(stat);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pokemon.getMinIndividualValues().get(stat).addListener((o, oldMin, newMin) ->formatButtons());
        pokemon.getMaxIndividualValues().get(stat).addListener((o, oldMax, newMax) -> formatButtons());
        pokemon.levelProperty().addListener((o, oldLevel, newLevel) -> formatButtons());
        pokemon.getEffortValues().get(stat).addListener((o, oldEv, newEv) -> formatButtons());
        pokemon.natureProperty().addListener((o, oldNature, newNature) -> formatButtons());
        pokemon.getAdditionalEffortValues().getEffortValue(stat).addListener((o, oldEv, newEv) -> {
            statEv.setText(String.valueOf(newEv));
            formatButtons();
        });
        pokemon.evolvedProperty().addListener((o, wasEvolved, isEvolved) -> formatButtons());

        formatButtons();
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
