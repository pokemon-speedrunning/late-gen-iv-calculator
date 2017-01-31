package be.lycoops.vincent.iv.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdditionalEffortValues {

    private Map<String, IntegerProperty> effortValues = new HashMap<>();

    AdditionalEffortValues() {
        for (final String stat: Arrays.asList("hp", "atk", "def", "spd", "spAtk", "spDef")) {
            effortValues.put(stat, new SimpleIntegerProperty(0));
        }
    }

    void increment(final String stat) {
        effortValues.get(stat).set(effortValues.get(stat).get() + 1);
    }

    public IntegerProperty getEffortValue(String stat) {
        return effortValues.get(stat);
    }

    public void reset() {
        for (final String stat: Arrays.asList("hp", "atk", "def", "spd", "spAtk", "spDef")) {
            effortValues.get(stat).set(0);
        }
    }
}
