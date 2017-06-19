package be.lycoops.vincent.iv.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AdditionalEffortValues {

    private Map<Stat, IntegerProperty> effortValues = new HashMap<>();

    AdditionalEffortValues() {
        for (final Stat stat: Stat.ALL_STATS) {
            effortValues.put(stat, new SimpleIntegerProperty(0));
        }
    }

    void increment(final Stat stat) {
        effortValues.get(stat).set(effortValues.get(stat).get() + 1);
    }

    public IntegerProperty getEffortValue(Stat stat) {
        return effortValues.get(stat);
    }

    public void reset() {
        for (final Stat stat: Stat.ALL_STATS) {
            effortValues.get(stat).set(0);
        }
    }
}
