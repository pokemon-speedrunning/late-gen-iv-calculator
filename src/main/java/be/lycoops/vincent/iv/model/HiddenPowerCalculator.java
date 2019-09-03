package be.lycoops.vincent.iv.model;

import java.util.HashMap;
import java.util.Map;

public class HiddenPowerCalculator {

    private final static HiddenPower[] hiddenPowers = HiddenPower.values();
    private final static HiddenPower[] sortedHiddenPowers = HiddenPower.getSorted();

    private int hiddenPowerId = -1;

    public HiddenPowerCalculator() {

    }

    /**
     * Cycles alphabetically towards the higher Hidden Power
     * @return The resulting Hidden Power
     */
    public HiddenPower updateToNext() {
        hiddenPowerId--;
        if (hiddenPowerId < 0) {
            hiddenPowerId = 15;
        }
        return sortedHiddenPowers[hiddenPowerId];
    }

    /**
     * Cycles alphabetically towards the lower Hidden Power
     * @return The resulting Hidden Power
     */
    public HiddenPower updateToPrevious() {
        hiddenPowerId++;
        if (hiddenPowerId > 15) {
            hiddenPowerId = 0;
        }
        return sortedHiddenPowers[hiddenPowerId];
    }

    /**
     * Erases the current Hidden Power
     * @return NULL
     */
    public HiddenPower setUnknown() {
        hiddenPowerId = -1;
        return null;
    }

    /**
     * Computes the probability of each Hidden Power to occur given the known IV ranges of the given Pokémon.
     * @param pokemon The Pokémon
     * @return A map from a Hidden Power to its probability
     */
    public Map<HiddenPower, Double> computeHiddenPower(Pokemon pokemon) {
        Map<HiddenPower, Double> computed = new HashMap<>();
        for (int i = 0; i < 16; ++i) {
            computed.put(hiddenPowers[i], 0d);
        }

        // Compute every oddness combination for each stat. (2^6 = 64 combinations).
        for (int hp = 0; hp <= 1; ++hp) {
            for (int atk = 0; atk <= 1; ++atk) {
                for (int def = 0; def <= 1; ++def) {
                    for (int spd = 0; spd <= 1; ++spd) {
                        for (int spAtk = 0; spAtk <= 1; ++spAtk) {
                            for (int spDef = 0; spDef <= 1; ++spDef) {
                                int id = (hp + atk * 2 + def * 4 + spd * 8 + spAtk * 16 + spDef * 32) * 15 / 63;
                                double probability = combinationProbability(pokemon, hp, atk, def, spd, spAtk, spDef);
                                double oldProbability = computed.get(hiddenPowers[id]);
                                computed.put(hiddenPowers[id], oldProbability + probability);
                            }
                        }
                    }
                }
            }
        }

        return computed;
    }

    /**
     * Computes the probability of a given oddness to occur for the given IV ranges.
     * @param pokemon The current Pokémon
     * @param hp The HP oddness
     * @param atk The ATK oddness
     * @param def The DEF oddness
     * @param spd The SPD oddness
     * @param spAtk The SP.ATK oddness
     * @param spDef The SP.DEF oddness
     * @return The probability
     */
    private double combinationProbability(Pokemon pokemon, int hp, int atk, int def, int spd, int spAtk, int spDef) {

        final int[] oddness = new int[]{hp, atk, def, spd, spAtk, spDef};
        final Stat[] stats = new Stat[]{Stat.HP, Stat.ATK, Stat.DEF, Stat.SPD, Stat.SP_ATK, Stat.SP_DEF};

        double probability = 1;

        for (int i = 0; i < oddness.length; ++i) {
            int minIv = pokemon.getMinIndividualValues().get(stats[i]).get();
            int maxIv = pokemon.getMaxIndividualValues().get(stats[i]).get();
            if (minIv % 2 != maxIv % 2) {
                // 50/50 chance, since even amount of possible IVs are possible.
                probability*= 0.5;
            } else {
                // Odd amount of possible IVs. Increase the likelihood of the highest oddness count.
                int div = maxIv - minIv + 1;
                int half = div / 2;
                if (oddness[i] == minIv % 2) {
                    ++half;
                }
                probability*= (double) half / div;
            }
        }
        return probability;
    }
}
