package be.lycoops.vincent.iv.model;


import java.util.Arrays;
import java.util.Comparator;

public enum HiddenPower {

    FIGHTING("FIGHTING", 0),
    FLYING("FLYING", 1),
    POISON("POISON", 2),
    GROUND("GROUND", 3),
    ROCK("ROCK", 4),
    BUG("BUG", 5),
    GHOST("GHOST", 6),
    STEEL("STEEL", 7),
    FIRE("FIRE", 8),
    WATER("WATER", 9),
    GRASS("GRASS", 10),
    ELECTRIC("ELECTRIC", 11),
    PSYCHIC("PSYCHIC", 12),
    ICE("ICE", 13),
    DRAGON("DRAGON", 14),
    DARK("DARK", 15);

    private String name;

    HiddenPower(String name, int number) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static HiddenPower[] getSorted() {
        HiddenPower[] hiddenPowers = HiddenPower.values();
        Arrays.sort(hiddenPowers, Comparator.comparing(o -> o.name));
        return hiddenPowers;
    }
}

