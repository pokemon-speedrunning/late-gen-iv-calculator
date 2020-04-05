package be.lycoops.vincent.iv.calculator.reset;

import be.lycoops.vincent.iv.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import javax.inject.Inject;
import java.net.URL;
import java.util.ResourceBundle;

public class ResetPresenter implements Initializable {

    @FXML
    private Button notation;

    @Inject
    private Pokemon pokemon;

    @Inject
    private HiddenPowerCalculator hiddenPowerCalculator;

    @Inject
    private NatureCalculator natureCalculator;

    @Inject
    private History history;

    @Inject
    private CalculatorConfig config;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        config.shortNotationProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                notation.setText("Short");
            } else {
                notation.setText("Long");
            }
        });
    }

    public void reset() {
        pokemon.reset(pokemon.getBaseLevel());
        natureCalculator.reset();
        pokemon.setHiddenPower(hiddenPowerCalculator.setUnknown());
        history.reset();
    }

    public void toggleShortNotation() {
        if (config.shortNotationProperty().get()) {
            config.disableShortNotation();
        } else {
            config.enableShortNotation();
        }
    }
}
