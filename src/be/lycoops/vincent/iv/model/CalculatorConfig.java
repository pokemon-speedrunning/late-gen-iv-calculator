package be.lycoops.vincent.iv.model;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class CalculatorConfig {
    private BooleanProperty shortNotation = new SimpleBooleanProperty(false);

    public void enableShortNotation() {
        shortNotation.set(true);
    }

    public void disableShortNotation() {
        shortNotation.set(false);
    }

    public BooleanProperty shortNotationProperty() {
        return shortNotation;
    }
}
