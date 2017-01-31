package be.lycoops.vincent.iv.model;

import javafx.beans.property.*;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Pokemon {
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
    private SimpleObjectProperty<HiddenPower> hiddenPower = new SimpleObjectProperty<>(null);

    /**
     * Whether the Pokémon is currently evolved.
     */
    private BooleanProperty evolved = new SimpleBooleanProperty();

    /**
     * Amount of individual values of each stat that have already been collected.
     */
    private Map<String, IntegerProperty> effortValues = new HashMap<>();

    /**
     * Base values for each stat of the current Pokémon. Used to compute expected stats at certain IVs.
     */
    private Map<String, Integer> baseValues = new HashMap<>();

    /**
     * Minimal IV bound for each stat.
     */
    private Map<String, IntegerProperty> minIndividualValues = new HashMap<>();

    /**
     * Maximal IV bound for each stat.
     */
    private Map<String, IntegerProperty> maxIndividualValues = new HashMap<>();

    /**
     * Additional effort values of the current Pokémon
     */
    private AdditionalEffortValues additionalEffortValues = new AdditionalEffortValues();

    @PostConstruct
    public void init() {
        for (final String stat: Arrays.asList("hp", "atk", "def", "spd", "spAtk", "spDef")) {
            effortValues.put(stat, new SimpleIntegerProperty(0));
            minIndividualValues.put(stat, new SimpleIntegerProperty(0));
            maxIndividualValues.put(stat, new SimpleIntegerProperty(31));
        }
        reset();
    }

    /**
     * Resets the Pokémon to Popplio at level 5
     */
    public void reset() {
        level.set(5);
        baseValues.put("hp", 50);
        baseValues.put("atk", 54);
        baseValues.put("def", 54);
        baseValues.put("spd", 40);
        baseValues.put("spAtk", 66);
        baseValues.put("spDef", 56);
        evolved.set(false);
        for (String stat: Arrays.asList("hp", "atk", "def", "spd", "spAtk", "spDef")) {
            effortValues.get(stat).set(0);
            minIndividualValues.get(stat).set(0);
            maxIndividualValues.get(stat).set(31);
        }
        additionalEffortValues.reset();
        setNature(null);
    }

    /**
     * Defines the base stats of the Pokémon to Brionne's base stats
     */
    public void evolve() {
        baseValues.put("hp", 60);
        baseValues.put("atk", 69);
        baseValues.put("def", 69);
        baseValues.put("spd", 50);
        baseValues.put("spAtk", 91);
        baseValues.put("spDef", 81);
        evolved.set(true);
    }

    /**
     * Defines the base stats of the Pokémon to Popplio's base stats
     */
    public void unevolve() {
        baseValues.put("hp", 50);
        baseValues.put("atk", 54);
        baseValues.put("def", 54);
        baseValues.put("spd", 40);
        baseValues.put("spAtk", 66);
        baseValues.put("spDef", 56);
        evolved.set(false);
    }

    /**
     * Increases the current Pokémon's level by 1
     */
    public void levelUp() {
        int level = this.level.get();
        if (level == 24) {
            return;
        }
        this.level.set(level + 1);
    }

    /**
     * Decreases the current Pokémon's level by 1
     */
    public void levelDown() {
        int level = this.level.get();
        if (level == 5) {
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

    public Map<String, IntegerProperty> getMinIndividualValues() {
        return minIndividualValues;
    }

    public void setMinIndividualValues(Map<String, IntegerProperty> minIndividualValues) {
        this.minIndividualValues = minIndividualValues;
    }

    public Map<String, IntegerProperty> getMaxIndividualValues() {
        return maxIndividualValues;
    }

    public void setMaxIndividualValues(Map<String, IntegerProperty> maxIndividualValues) {
        this.maxIndividualValues = maxIndividualValues;
    }

    /**
     * Computes the expected stat given a specified individual value.
     * This computation is based on effort values, current level, and nature.
     * @param stat The name of the stat
     * @param individualValue The individual value level
     * @return The expected stat
     */
    public int getExpectedStat(String stat, int individualValue) {
        if (stat.equals("hp")) {
            return getExpectedHp(individualValue);
        }
        int ev = effortValues.get(stat).get() + additionalEffortValues.getEffortValue(stat).get();
        int statValue = ((2 * baseValues.get(stat) + individualValue + ev / 4) * level.get()) / 100 + 5;
        Nature nature = this.nature.get();
        if (nature != null) {
            if (nature.getStat(stat) == 1) {
                statValue = (int) (statValue * 1.1);
            } else if (nature.getStat(stat) == -1) {
                statValue = (int) (statValue * 0.9);
            }
        }

        return statValue;
    }

    /**
     * Computes the expected HP stat given a specified individual value.
     * This computation is based on effort values, current level, and nature.
     * @param hpIv The HP individual value level
     * @return The expected HP stat
     */
    private int getExpectedHp(int hpIv) {
        int level = this.level.get();
        int ev = effortValues.get("hp").get() + additionalEffortValues.getEffortValue("hp").get();
        return ((2 * baseValues.get("hp") + hpIv + ev / 4) * level)/ 100 + level + 10;
    }

    /**
     * Defines the current stat of a Pokémon. Given that stat the lower and upper bound of the IV of that stat is
     * determined and modified accordingly.
     * @param stat The name of the stat
     * @param value The value of the stat
     */
    public void setKnownStat(String stat, int value) {

        int min = minIndividualValues.get(stat).get();
        int max = maxIndividualValues.get(stat).get();

        // Increases lower bound and decreases upper bound of the computed IV until they are compatible.
        for (int i = min, j = max; j <= max; ++i, --j) {
            int expectedMin = getExpectedStat(stat, i);
            int expectedMax = getExpectedStat(stat, j);
            if (expectedMin < value) {
                minIndividualValues.get(stat).set(minIndividualValues.get(stat).get() + 1);
            }
            if (expectedMax > value) {
                maxIndividualValues.get(stat).set(maxIndividualValues.get(stat).get() - 1);
            }

            if (expectedMin >= value && expectedMax <= value) {
                // Can skip since we know the upper and lower bounds already.
                break;
            }
        }
    }

    /**
     * Forces the IV of a stat
     * @param stat The name of the stat
     * @param iv The IV level to set that stat to
     */
    public void setKnownIv(String stat, int iv) {
        this.minIndividualValues.get(stat).set(iv);
        this.maxIndividualValues.get(stat).set(iv);
    }

    /**
     * Adds 1 additional effort value to the Pokémon of a given stat
     * @param stat The name of the stat
     */
    public void addAdditionalEffortValue(String stat) {
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

    public SimpleObjectProperty<HiddenPower> hiddenPowerProperty() {
        return hiddenPower;
    }

    public void setNature(Nature nature) {
        this.nature.set(nature);
    }

    public Map<String, IntegerProperty> getEffortValues() {
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

    public void setStatRange(History.Stat statRange) {
        String stat = statRange.getStat();
        this.minIndividualValues.get(stat).set(statRange.getLowIv());
        this.maxIndividualValues.get(stat).set(statRange.getHighIv());
    }
}
