package be.lycoops.vincent.iv.model;


import java.util.Arrays;
import java.util.Comparator;






public enum Nature {

    HARDY   ("Hardy", 0, 0, 0, 0, 0),
    LONELY  ("Lonely", 1, -1, 0, 0, 0),
    BRAVE   ("Brave", 1, 0, -1, 0, 0),
    ADAMANT ("Adamant", 1, 0, 0, -1, 0),
    NAUGHTY ("Naughty", 1, 0, 0, 0, -1),
    BOLD    ("Bold", -1, 1, 0, 0, 0),
    DOCILE  ("Docile", 0, 0 , 0, 0, 0),
    RELAXED ("Relaxed", 0, 1, -1, 0, 0),
    IMPISH  ("Impish", 0, 1, 0, -1, 0),
    LAX     ("Lax", 0, 1, 0, 0, -1),
    TIMID   ("Timid", -1, 0, 1, 0, 0),
    HASTY   ("Hasty", 0, -1, 1, 0, 0),
    SERIOUS ("Serious", 0, 0, 0, 0, 0),
    JOLLY   ("Jolly", 0, 0, 1, -1, 0),
    NAIVE   ("Naive", 0, 0, 1, 0, -1),
    MODEST  ("Modest", -1, 0, 0, 1, 0),
    MILD    ("Mild", 0, -1, 0, 1, 0),
    QUIET   ("Quiet", 0, 0, -1, 1, 0),
    BASHFUL ("Bashful", 0, 0, 0, 0, 0),
    RASH    ("Rash", 0, 0, 0, 1, -1),
    CALM    ("Calm", -1, 0, 0, 0, 1),
    GENTLE  ("Gentle", 0, -1, 0, 0, 1),
    SASSY   ("Sassy", 0, 0, -1, 0, 1),
    CAREFUL ("Careful", 0, 0, 0, -1, 1),
    QUIRKY  ("Quirky", 0, 0, 0, 0, 0);


    private final int atk;
    private final int def;
    private final int spd;
    private final int spAtk;
    private final int spDef;

    private final String name;

    Nature(String name, int atk, int def, int spd, int spAtk, int spDef) {
        this.name = name;
        this.atk = atk;
        this.def = def;
        this.spd = spd;
        this.spAtk = spAtk;
        this.spDef = spDef;
    }

    public String getName() {
        return name;
    }

    public static Nature[] getSorted() {
        Nature[] natures = Nature.values();
        Arrays.sort(natures, Comparator.comparing(o -> o.name));
        return natures;
    }

    public static Nature getNature(String minusNature, String plusNature) {
        if (minusNature == null || plusNature == null) {
            return null;
        }

        for (Nature nature: values()) {
            switch (minusNature) {
                case "atk": if (nature.atk != -1) continue; break;
                case "def": if (nature.def != -1) continue; break;
                case "spd": if (nature.spd != -1) continue; break;
                case "spAtk": if (nature.spAtk != -1) continue; break;
                case "spDef": if (nature.spDef != -1) continue; break;
            }
            switch (plusNature) {
                case "atk": if (nature.atk != 1) continue; break;
                case "def": if (nature.def != 1) continue; break;
                case "spd": if (nature.spd != 1) continue; break;
                case "spAtk": if (nature.spAtk != 1) continue; break;
                case "spDef": if (nature.spDef != 1) continue; break;
            }
            return nature;
        }
        return null;
    }


    public int getStat(String stat) {
        switch (stat) {
            case "atk": return atk;
            case "def": return def;
            case "spd": return spd;
            case "spAtk": return spAtk;
            case "spDef": return spDef;
        }
        throw new IllegalArgumentException(stat + " is not a valid stat modified by a nature.");
    }
}
