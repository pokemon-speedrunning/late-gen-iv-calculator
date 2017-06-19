package be.lycoops.vincent.iv.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

public class NatureCalculator {

    private ObjectProperty<Stat> plusNature = new SimpleObjectProperty<>(null);
    private ObjectProperty<Stat> minusNature = new SimpleObjectProperty<>(null);
    private ListProperty<Stat> neutralNatures = new SimpleListProperty<>(FXCollections.observableArrayList());

    @Inject
    private Pokemon pokemon;

    @PostConstruct
    public void init() {
        pokemon.setNatureCalculator(this);
    }

    public void reset() {
        plusNature.set(null);
        minusNature.set(null);
        neutralNatures.clear();
    }

    public void undoNature(Stat nature) {
        if (nature.equals(minusNature.get())) {
            minusNature.set(null);
        } else if (nature.equals(plusNature.get())){
            plusNature.set(null);
        }
    }

    public Stat getPlusNature() {
        return plusNature.get();
    }

    public ObjectProperty<Stat> plusNatureProperty() {
        return plusNature;
    }

    public boolean setPlusNature(Stat plusNature) {
        if (this.plusNature.get() == null && plusNature == null) {
            return false;
        }
        boolean changed = plusNature == null || !plusNature.equals(this.plusNature.get());
        this.plusNature.set(plusNature);
        if (plusNature != null && plusNature.equals(minusNature.get())) {
            minusNature.set(null);
        }
        return changed;
    }

    public Stat getMinusNature() {
        return minusNature.get();
    }

    public ObjectProperty<Stat> minusNatureProperty() {
        return minusNature;
    }

    public Object getNeutralNatures() {
        return neutralNatures.get();
    }

    public ListProperty<Stat> neutralNaturesProperty() {
        return neutralNatures;
    }

    public boolean setMinusNature(Stat minusNature) {
        if (this.minusNature.get() == null && minusNature == null) {
            return false;
        }
        boolean changed = minusNature == null || !minusNature.equals(this.minusNature.get());
        this.minusNature.set(minusNature);
        if (minusNature != null && minusNature.equals(plusNature.get())) {
            plusNature.set(null);
        }
        return changed;
    }

    public Nature computeNature() {
        return Nature.getNature(minusNature.get(), plusNature.get());
    }

    public boolean addNeutralNature(final Stat stat) {
        boolean added = !neutralNatures.contains(stat);
        if (added) {
            neutralNatures.add(stat);
        }
        int size = neutralNatures.size();
        if (size > 3) {
            if (size == 4) {
                pokemon.setNature(Nature.DOCILE);
                for (final Stat s: Stat.DEFAULT_STATS) {
                    if (!neutralNatures.contains(s)) {
                        neutralNatures.add(s);
                        pokemon.getMinMinusIndividualValues().get(s).set(-1);
                        pokemon.getMaxMinusIndividualValues().get(s).set(-1);
                        pokemon.getMinPlusIndividualValues().get(s).set(-1);
                        pokemon.getMaxPlusIndividualValues().get(s).set(-1);
                        pokemon.refreshIvRange(s);
                    }
                }
            }
        }
        return added;
    }
}
