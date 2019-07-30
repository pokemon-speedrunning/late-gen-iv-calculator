package be.lycoops.vincent.iv.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.util.Callback;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Map;

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

        if (plusNature != null) {
            pokemon.getMinMinusIndividualValues().get(plusNature).set(-1);
            pokemon.getMaxMinusIndividualValues().get(plusNature).set(-1);
            pokemon.getMinNeutralIndividualValues().get(plusNature).set(-1);
            pokemon.getMaxNeutralIndividualValues().get(plusNature).set(-1);

            for (final Stat stat : Stat.DEFAULT_STATS) {
                if (!stat.equals(plusNature)) {
                    pokemon.getMinPlusIndividualValues().get(stat).set(-1);
                    pokemon.getMaxPlusIndividualValues().get(stat).set(-1);
                }
            }
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

        if (minusNature != null) {
            pokemon.getMinPlusIndividualValues().get(minusNature).set(-1);
            pokemon.getMaxPlusIndividualValues().get(minusNature).set(-1);
            pokemon.getMinNeutralIndividualValues().get(minusNature).set(-1);
            pokemon.getMaxNeutralIndividualValues().get(minusNature).set(-1);

            for (final Stat stat : Stat.DEFAULT_STATS) {
                if (!stat.equals(minusNature)) {
                    pokemon.getMinMinusIndividualValues().get(stat).set(-1);
                    pokemon.getMaxMinusIndividualValues().get(stat).set(-1);
                }
            }
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

    /**
     * Dirty method to verify nature - this method just fixes the oversight of the initial nature calculation.
     */
    public void checkNatures() {
        if (pokemon.getNature() != null) {
            return;
        }
        checkNeutralNature();

        if (pokemon.getNature() != null) {
            return;
        }
        if (minusNature.get() == null && plusNature.get() == null) {
            return;
        }
        Stat foundStat = null;
        Map<Stat, IntegerProperty> minIvs;
        Map<Stat, IntegerProperty> maxIvs;
        Callback<Stat, Boolean> setNature;
        if (minusNature.get() != null) {
            minIvs = pokemon.getMinPlusIndividualValues();
            maxIvs = pokemon.getMaxPlusIndividualValues();
            setNature = this::setPlusNature;
        } else {
            minIvs = pokemon.getMinMinusIndividualValues();
            maxIvs = pokemon.getMaxMinusIndividualValues();
            setNature = this::setMinusNature;
        }

        for (final Stat s: Stat.DEFAULT_STATS) {
            if (minIvs.get(s).get() != -1) {
                if (foundStat != null) {
                    foundStat = null;
                    break;
                }
                foundStat = s;
            }
        }

        if (foundStat != null) {
            pokemon.getMinNeutralIndividualValues().get(foundStat).set(-1);
            pokemon.getMaxNeutralIndividualValues().get(foundStat).set(-1);
            pokemon.getMinIndividualValues().get(foundStat).set(minIvs.get(foundStat).get());
            pokemon.getMaxIndividualValues().get(foundStat).set(maxIvs.get(foundStat).get());
            setNature.call(foundStat);
            pokemon.setNature(Nature.getNature(getMinusNature(), getPlusNature()));
        }
    }

    private void checkNeutralNature() {
        if (minusNature.get() != null || plusNature.get() != null) {
            return;
        }

        int potentialMinus = 0;
        int potentialPlus = 0;

        for (final Stat s: Stat.DEFAULT_STATS) {
            if (pokemon.getMinMinusIndividualValues().get(s).get() != -1) {
                ++potentialMinus;
            }
            if (pokemon.getMinPlusIndividualValues().get(s).get() != -1) {
                ++potentialPlus;
            }
        }

        if (potentialMinus == 0 || potentialPlus == 0) {
            pokemon.setNature(Nature.DOCILE);
            for (final Stat s: Stat.DEFAULT_STATS) {
                pokemon.getMinMinusIndividualValues().get(s).set(-1);
                pokemon.getMaxMinusIndividualValues().get(s).set(-1);
                pokemon.getMinPlusIndividualValues().get(s).set(-1);
                pokemon.getMaxPlusIndividualValues().get(s).set(-1);
                pokemon.refreshIvRange(s);
            }
        }
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
