package be.lycoops.vincent.iv.model;

public enum Stat {

    HP("hp", "HP", "HP"),
    ATK("atk", "ATK", "ATK"),
    DEF("def", "DEF", "DEF"),
    SP_ATK("spAtk", "SP.ATK", "SPA"),
    SP_DEF("spDef", "SP.DEF", "SPD"),
    SPD("spd", "SPD", "SPE");

    public static final Stat[] ALL_STATS = {HP, ATK, DEF, SP_ATK, SP_DEF, SPD};
    public static final Stat[] DEFAULT_STATS = {ATK, DEF, SP_ATK, SP_DEF, SPD};


    private final String statName;
    private final String visualNameLong;
    private final String visualNameShort;

    Stat(String statName, String visualNameLong, String visualNameShort) {
        this.statName = statName;
        this.visualNameLong = visualNameLong;
        this.visualNameShort = visualNameShort;
    }

    public String toString() {
        return statName;
    }

    public String getVisualName(boolean shortName) {
        return shortName ? visualNameShort : visualNameLong;
    }
}
