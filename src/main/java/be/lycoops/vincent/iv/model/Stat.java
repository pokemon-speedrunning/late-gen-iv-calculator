package be.lycoops.vincent.iv.model;

public enum Stat {

    HP("hp", "HP"),
    ATK("atk", "ATK"),
    DEF("def", "DEF"),
    SP_ATK("spAtk", "SP.ATK"),
    SP_DEF("spDef", "SP.DEF"),
    SPD("spd", "SPD");

    public static final Stat[] ALL_STATS = {HP, ATK, DEF, SP_ATK, SP_DEF, SPD};
    public static final Stat[] DEFAULT_STATS = {ATK, DEF, SP_ATK, SP_DEF, SPD};


    private final String statName;
    private final String visualName;

    Stat(String statName, String visualName) {
        this.statName = statName;
        this.visualName = visualName;
    }

    public String toString() {
        return statName;
    }

    public String getVisualName() {
        return visualName;
    }
}
