package be.lycoops.vincent.iv.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class NatureCalculator {
    private StringProperty plusNature = new SimpleStringProperty(null);
    private StringProperty minusNature = new SimpleStringProperty(null);

    public void reset() {
        plusNature.set(null);
        minusNature.set(null);
    }

    public void undoNature(String nature) {
        if (nature.equals(minusNature.get())) {
            minusNature.set(null);
        } else if (nature.equals(plusNature.get())){
            plusNature.set(null);
        }
    }

    public String getPlusNature() {
        return plusNature.get();
    }

    public StringProperty plusNatureProperty() {
        return plusNature;
    }

    public void setPlusNature(String plusNature) {
        this.plusNature.set(plusNature);
        if (plusNature.equals(minusNature.get())) {
            minusNature.set(null);
        }
    }

    public String getMinusNature() {
        return minusNature.get();
    }

    public StringProperty minusNatureProperty() {
        return minusNature;
    }

    public void setMinusNature(String minusNature) {
        this.minusNature.set(minusNature);
        if (minusNature.equals(plusNature.get())) {
            plusNature.set(null);
        }
    }

    public Nature computeNature() {
        return Nature.getNature(minusNature.get(), plusNature.get());
    }
}
