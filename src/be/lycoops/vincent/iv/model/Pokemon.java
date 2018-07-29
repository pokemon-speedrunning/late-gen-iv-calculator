package be.lycoops.vincent.iv.model;

import javafx.beans.property.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class Pokemon {
    private boolean isPikipek = false;

    /**
     * Current level of the Pokémon.
     */
    private IntegerProperty level = new SimpleIntegerProperty();

    /**
     * Nature of the Pokémon.
     */
    private ObjectProperty<Nature> nature = new SimpleObjectProperty<>(null);

    /**
     * Hidden power of the Pokémon.
     */
    private ObjectProperty<HiddenPower> hiddenPower = new SimpleObjectProperty<>(null);

    /**
     * Whether the Pokémon is currently evolved.
     */
    private BooleanProperty evolved = new SimpleBooleanProperty();

    /**
     * Amount of individual values of each stat that have already been collected.
     */
    private Map<Stat, IntegerProperty> effortValues = new HashMap<>();

    /**
     * Base values for each stat of the current Pokémon. Used to compute expected stats at certain IVs.
     */
    private Map<Stat, Integer> baseValues = new HashMap<>();

    /**
     * Minimal IV bound for each stat.
     */
    private Map<Stat, IntegerProperty> minIndividualValues = new HashMap<>();

    /**
     * Maximal IV bound for each stat.
     */
    private Map<Stat, IntegerProperty> maxIndividualValues = new HashMap<>();

    private Map<Stat, IntegerProperty> minMinusIndividualValues = new HashMap<>();
    private Map<Stat, IntegerProperty> maxMinusIndividualValues = new HashMap<>();
    private Map<Stat, IntegerProperty> minNeutralIndividualValues = new HashMap<>();
    private Map<Stat, IntegerProperty> maxNeutralIndividualValues = new HashMap<>();
    private Map<Stat, IntegerProperty> minPlusIndividualValues = new HashMap<>();
    private Map<Stat, IntegerProperty> maxPlusIndividualValues = new HashMap<>();

    /**
     * Additional effort values of the current Pokémon
     */
    private AdditionalEffortValues additionalEffortValues = new AdditionalEffortValues();
    private NatureCalculator natureCalculator;

    @PostConstruct
    public void init() {
        for (final Stat stat: Stat.ALL_STATS) {
            effortValues.put(stat, new SimpleIntegerProperty(0));
            minIndividualValues.put(stat, new SimpleIntegerProperty(0));
            maxIndividualValues.put(stat, new SimpleIntegerProperty(31));
            if (!stat.equals(Stat.HP)) {
                minMinusIndividualValues.put(stat, new SimpleIntegerProperty(0));
                maxMinusIndividualValues.put(stat, new SimpleIntegerProperty(31));
                minNeutralIndividualValues.put(stat, new SimpleIntegerProperty(0));
                maxNeutralIndividualValues.put(stat, new SimpleIntegerProperty(31));
                minPlusIndividualValues.put(stat, new SimpleIntegerProperty(0));
                maxPlusIndividualValues.put(stat, new SimpleIntegerProperty(31));
            }

        }
        reset();
    }

    /**
     * Resets the Pokémon to Popplio at level 5
     */
    public void reset() {
        if (isPikipek) {
            level.set(2);
            baseValues.put(Stat.HP, 35);
            baseValues.put(Stat.ATK, 75);
            baseValues.put(Stat.DEF, 30);
            baseValues.put(Stat.SPD, 65);
            baseValues.put(Stat.SP_ATK, 30);
            baseValues.put(Stat.SP_DEF, 30);
        } else {
            level.set(10);
            baseValues.put(Stat.HP, 52);
            baseValues.put(Stat.ATK, 65);
            baseValues.put(Stat.DEF, 55);
            baseValues.put(Stat.SPD, 58);
            baseValues.put(Stat.SP_ATK, 62);
            baseValues.put(Stat.SP_DEF, 60);
        }
        evolved.set(false);

        for (final Stat stat: Stat.ALL_STATS) {
            effortValues.get(stat).set(0);
            minIndividualValues.get(stat).set(0);
            maxIndividualValues.get(stat).set(31);
            if (!stat.equals(Stat.HP)) {
                minMinusIndividualValues.get(stat).set(0);
                maxMinusIndividualValues.get(stat).set(31);
                minNeutralIndividualValues.get(stat).set(0);
                maxNeutralIndividualValues.get(stat).set(31);
                minPlusIndividualValues.get(stat).set(0);
                maxPlusIndividualValues.get(stat).set(31);
            }
        }

        for (final Stat stat: Stat.ALL_STATS) {
            effortValues.get(stat).set(0);
            if (stat.equals(Stat.SPD)) {
                minMinusIndividualValues.get(stat).set(-1);
                maxMinusIndividualValues.get(stat).set(-1);
                minNeutralIndividualValues.get(stat).set(-1);
                maxNeutralIndividualValues.get(stat).set(-1);
                minPlusIndividualValues.get(stat).set(31);
                maxPlusIndividualValues.get(stat).set(31);
                minIndividualValues.get(stat).set(31);
                maxIndividualValues.get(stat).set(31);
                continue;
            } else  if (stat.equals(Stat.SP_ATK)) {
                minMinusIndividualValues.get(stat).set(0);
                maxMinusIndividualValues.get(stat).set(31);
                minNeutralIndividualValues.get(stat).set(-1);
                maxNeutralIndividualValues.get(stat).set(-1);
                minPlusIndividualValues.get(stat).set(-1);
                maxPlusIndividualValues.get(stat).set(-1);
                minIndividualValues.get(stat).set(0);
                maxIndividualValues.get(stat).set(31);
                continue;
            }
            minIndividualValues.get(stat).set(0);
            maxIndividualValues.get(stat).set(31);
            if (!stat.equals(Stat.HP)) {
                minMinusIndividualValues.get(stat).set(0);
                maxMinusIndividualValues.get(stat).set(31);
                minNeutralIndividualValues.get(stat).set(0);
                maxNeutralIndividualValues.get(stat).set(31);
                minPlusIndividualValues.get(stat).set(0);
                maxPlusIndividualValues.get(stat).set(31);
            }
        }
        additionalEffortValues.reset();
        if (natureCalculator != null) {
            natureCalculator.setPlusNature(Stat.SPD);
            natureCalculator.setMinusNature(Stat.SP_ATK);
        }
        setNature(Nature.JOLLY);
    }

    /**
     * Defines the base stats of the Pokémon to Brionne's base stats
     */
    public void evolve() {
        if (isPikipek) {
            baseValues.put(Stat.HP, 55);
            baseValues.put(Stat.ATK, 85);
            baseValues.put(Stat.DEF, 50);
            baseValues.put(Stat.SPD, 75);
            baseValues.put(Stat.SP_ATK, 40);
            baseValues.put(Stat.SP_DEF, 50);
        } else {
            baseValues.put(Stat.HP, 60);
            baseValues.put(Stat.ATK, 69);
            baseValues.put(Stat.DEF, 69);
            baseValues.put(Stat.SPD, 50);
            baseValues.put(Stat.SP_ATK, 91);
            baseValues.put(Stat.SP_DEF, 81);
        }
        evolved.set(true);
    }

    /**
     * Defines the base stats of the Pokémon to Popplio's base stats
     */
    public void unevolve() {
        if (isPikipek) {
            baseValues.put(Stat.HP, 35);
            baseValues.put(Stat.ATK, 75);
            baseValues.put(Stat.DEF, 30);
            baseValues.put(Stat.SPD, 65);
            baseValues.put(Stat.SP_ATK, 30);
            baseValues.put(Stat.SP_DEF, 30);
        } else {
            baseValues.put(Stat.HP, 50);
            baseValues.put(Stat.ATK, 54);
            baseValues.put(Stat.DEF, 54);
            baseValues.put(Stat.SPD, 40);
            baseValues.put(Stat.SP_ATK, 66);
            baseValues.put(Stat.SP_DEF, 56);
        }
        evolved.set(false);
    }

    /**
     * Increases the current Pokémon's level by 1
     */
    public void levelUp() {
        int level = this.level.get();
        if (level == 35 && !isPikipek && evolved.get()) {
            baseValues.put(Stat.HP, 80);
            baseValues.put(Stat.ATK, 74);
            baseValues.put(Stat.DEF, 74);
            baseValues.put(Stat.SPD, 60);
            baseValues.put(Stat.SP_ATK, 126);
            baseValues.put(Stat.SP_DEF, 116);
        }
        if (level == 99) {
            return;
        }
        this.level.set(level + 1);
    }

    /**
     * Decreases the current Pokémon's level by 1
     */
    public void levelDown() {
        int level = this.level.get();
        if ((!isPikipek && level == 10) || level == 2) {
            return;
        }
        this.level.set(level - 1);
    }

    public int getLevel() {
        return level.get();
    }

    public IntegerProperty levelProperty() {
        return level;
    }

    public void setLevel(int level) {
        this.level.set(level);
    }

    public Map<Stat, IntegerProperty> getMinIndividualValues() {
        return minIndividualValues;
    }

    public void setMinIndividualValues(Map<Stat, IntegerProperty> minIndividualValues) {
        this.minIndividualValues = minIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMaxIndividualValues() {
        return maxIndividualValues;
    }

    public void setMaxIndividualValues(Map<Stat, IntegerProperty> maxIndividualValues) {
        this.maxIndividualValues = maxIndividualValues;
    }

    private static int getExpectedStat(Stat stat, int level, int iv, int ev, final Map<Stat, Integer> baseValues, int nature) {
        int statValue = (2 * baseValues.get(stat) + iv + ev / 4) * level / 100 + 5;
        if (nature == 1) {
            statValue *= 1.1;
        } else if (nature == -1) {
            statValue *= 0.9;
        }
        return statValue;
    }

    private static int getExpectedHp(int level, int iv, int ev, final Map<Stat, Integer> baseValues) {
        return ((2 * baseValues.get(Stat.HP) + iv + ev / 4) * level)/ 100 + level + 10;
    }

    /**
     * Computes the expected stat given a specified individual value.
     * This computation is based on effort values, current level, and nature.
     * @param stat The name of the stat
     * @param individualValue The individual value level
     * @return The expected stat
     */
    public int getExpectedStat(Stat stat, int individualValue) {
        if (stat.equals(Stat.HP)) {
            return getExpectedHp(individualValue);
        }
        int minusValue = stat.equals(natureCalculator.getMinusNature()) ? -1 : 0;
        int plusValue = stat.equals(natureCalculator.getPlusNature()) ? 1 : 0;
        int natureValue = minusValue + plusValue;
        return getExpectedStat(stat, individualValue, natureValue);
    }

    /**
     * Computes the expected stat given a specified individual value.
     * This computation is based on effort values, current level, and nature.
     * @param stat The name of the stat
     * @param individualValue The individual value level
     * @return The expected stat
     */
    private int getExpectedStat(Stat stat, int individualValue, int natureValue) {
        return getExpectedStat(stat, getLevel(), individualValue, getTotalEvs(stat), baseValues, natureValue);

    }

    /**
     * Computes the expected HP stat given a specified individual value.
     * This computation is based on effort values, current level, and nature.
     * @param hpIv The HP individual value level
     * @return The expected HP stat
     */
    private int getExpectedHp(int hpIv) {
        return getExpectedHp(getLevel(), hpIv, getTotalEvs(Stat.HP), baseValues);
    }

    /**
     * Defines the current stat of a Pokémon. Given that stat the lower and upper bound of the IV of that stat is
     * determined and modified accordingly.
     * @param stat The name of the stat
     * @param value The value of the stat
     */
    public void setKnownStat(Stat stat, int value) {

        if (stat.equals(Stat.HP)) {
            setKnownStat(stat, value, minIndividualValues.get(Stat.HP), maxIndividualValues.get(Stat.HP), 0);
            return;
        }

        int minIv = setKnownStat(stat, value, minMinusIndividualValues.get(stat), maxMinusIndividualValues.get(stat), -1);
        int neutralIv = setKnownStat(stat, value, minNeutralIndividualValues.get(stat), maxNeutralIndividualValues.get(stat), 0);
        int plusIv = setKnownStat(stat, value, minPlusIndividualValues.get(stat), maxPlusIndividualValues.get(stat), 1);

        refreshIvRange(stat, minIv, neutralIv, plusIv);
        natureCalculator.checkNatures();
    }

    private int setKnownStat(Stat stat, int value, IntegerProperty minProp, IntegerProperty maxProp, int nature) {
        int min = minProp.get();
        if (min == -1) {
            return -1;
        }

        int max = maxProp.get();

        boolean isHp = stat.equals(Stat.HP);

        // Increases lower bound and decreases upper bound of the computed IV until they are compatible.
        for (int i = min, j = max; j <= max && j >= min && i <= max; ++i, --j) {
            int expectedMin = isHp ? getExpectedHp(i) : getExpectedStat(stat, i, nature);
            int expectedMax = isHp ? getExpectedHp(j) : getExpectedStat(stat, j, nature);
            if (expectedMin < value) {
                minProp.set(minProp.get() + 1);
            }
            if (expectedMax > value) {
                maxProp.set(maxProp.get() - 1);
            }

            if (expectedMin >= value && expectedMax <= value) {
                return minProp.get();
            }
        }

        minProp.set(-1);
        maxProp.set(-1);

        return -1;
    }

    /**
     * Forces the IV of a stat
     * @param stat The name of the stat
     * @param iv The IV level to set that stat to
     */
    public void setKnownIv(Stat stat, int iv) {
        this.minIndividualValues.get(stat).set(iv);
        this.maxIndividualValues.get(stat).set(iv);
    }

    /**
     * Adds 1 additional effort value to the Pokémon of a given stat
     * @param stat The name of the stat
     */
    public void addAdditionalEffortValue(Stat stat) {
        additionalEffortValues.increment(stat);
    }

    public AdditionalEffortValues getAdditionalEffortValues() {
        return additionalEffortValues;
    }

    public void setHiddenPower(HiddenPower hiddenPower) {
        this.hiddenPower.set(hiddenPower);
    }

    public HiddenPower getHiddenPower() {
        return hiddenPower.get();
    }

    public ObjectProperty<HiddenPower> hiddenPowerProperty() {
        return hiddenPower;
    }

    public void setNature(Nature nature) {
        this.nature.set(nature);
    }

    public Map<Stat, IntegerProperty> getEffortValues() {
        return effortValues;
    }

    public boolean isEvolved() {
        return evolved.get();
    }

    public BooleanProperty evolvedProperty() {
        return evolved;
    }

    public Nature getNature() {
        return nature.get();
    }

    public ObjectProperty<Nature> natureProperty() {
        return nature;
    }

    public void setStatRange(History.StatAction statRange) {
        Stat stat = statRange.getStat();
        this.minIndividualValues.get(stat).set(statRange.getLowIv());
        this.maxIndividualValues.get(stat).set(statRange.getHighIv());
    }

    int getTotalEvs(Stat stat) {
        return effortValues.get(stat).get() + additionalEffortValues.getEffortValue(stat).get();
    }

    Map<Stat, Integer> getBaseValues() {
        return baseValues;
    }

    public void setNatureCalculator(NatureCalculator natureCalculator) {
        this.natureCalculator = natureCalculator;
    }

    public Map<Stat, IntegerProperty> getMinMinusIndividualValues() {
        return minMinusIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMinNeutralIndividualValues() {
        return minNeutralIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMinPlusIndividualValues() {
        return minPlusIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMaxMinusIndividualValues() {
        return maxMinusIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMaxNeutralIndividualValues() {
        return maxNeutralIndividualValues;
    }

    public Map<Stat, IntegerProperty> getMaxPlusIndividualValues() {
        return maxPlusIndividualValues;
    }

    public SortedSet<Integer> getPossibleStats(Stat stat) {
        SortedSet<Integer> stats = new TreeSet<>();

        int min = minMinusIndividualValues.get(stat).get();
        int max;
        if (min != -1) {
            max = maxMinusIndividualValues.get(stat).get();
            for (int i = min; i <= max; ++i) {
                stats.add(getExpectedStat(stat, i, -1));
            }
        }

        min = minNeutralIndividualValues.get(stat).get();
        if (min != -1) {
            max = maxNeutralIndividualValues.get(stat).get();
            for (int i = min; i <= max; ++i) {
                stats.add(getExpectedStat(stat, i, 0));
            }
        }

        min = minPlusIndividualValues.get(stat).get();
        if (min != -1) {
            max = maxPlusIndividualValues.get(stat).get();
            for (int i = min; i <= max; ++i) {
                stats.add(getExpectedStat(stat, i, 1));
            }
        }

        return stats;
    }

    void refreshIvRange(Stat stat) {
        refreshIvRange(stat, minMinusIndividualValues.get(stat).get(),
                minNeutralIndividualValues.get(stat).get(), minPlusIndividualValues.get(stat).get());
    }

    private void refreshIvRange(Stat stat, int minIv, int neutralIv, int plusIv) {
        int lowest = 31;
        int highest = 0;

        int possible = 0;
        int nature = 0;

        if (minIv != -1) {
            ++possible;
            nature = -1;
            if (minIv < lowest) {
                lowest = minIv;
            }
            if (maxMinusIndividualValues.get(stat).get() > highest) {
                highest = maxMinusIndividualValues.get(stat).get();
            }
        }

        if (neutralIv != -1) {
            ++possible;
            if (neutralIv < lowest) {
                lowest = neutralIv;
            }
            if (maxNeutralIndividualValues.get(stat).get() > highest) {
                highest = maxNeutralIndividualValues.get(stat).get();
            }
        }

        if (plusIv != -1) {
            ++possible;
            nature = 1;
            if (plusIv < lowest) {
                lowest = plusIv;
            }
            if (maxPlusIndividualValues.get(stat).get() > highest) {
                highest = maxPlusIndividualValues.get(stat).get();
            }
        }


        minIndividualValues.get(stat).set(lowest);
        maxIndividualValues.get(stat).set(highest);

        if (possible == 1) {

            boolean knowledgeChanged = false;
            // We found the correct nature!
            switch (nature) {
                case -1: knowledgeChanged = natureCalculator.setMinusNature(stat); break;
                case 1: knowledgeChanged = natureCalculator.setPlusNature(stat); break;
                case 0: knowledgeChanged = natureCalculator.addNeutralNature(stat); break;
            }

            Stat minus = natureCalculator.getMinusNature();
            Stat plus = natureCalculator.getPlusNature();

            if (minus != null && plus != null) {
                setNature(Nature.getNature(minus, plus));
            }

            if (knowledgeChanged) {
                cleanUpIncompatibleRanges(stat, nature);
            }
        }
    }

    private void cleanUpIncompatibleRanges(Stat referenceNature, int value) {

        for (final Stat stat: Stat.DEFAULT_STATS) {
            if (stat.equals(referenceNature)) {
                continue;
            }
            if (value == 1) {
                int oldMin = minPlusIndividualValues.get(stat).get();
                if (oldMin != -1) {
                    minPlusIndividualValues.get(stat).set(-1);
                    maxPlusIndividualValues.get(stat).set(-1);
                    refreshIvRange(stat);
                }
            } else if (value == -1) {
                int oldMin = minMinusIndividualValues.get(stat).get();
                if (oldMin != -1) {
                    minMinusIndividualValues.get(stat).set(-1);
                    maxMinusIndividualValues.get(stat).set(-1);
                    refreshIvRange(stat);
                }
            }
        }
    }

}
