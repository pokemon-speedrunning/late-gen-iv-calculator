package be.lycoops.vincent.iv.model.json;

public class PokemonStats {

    private int hp;

    private int atk;

    private int def;

    private int spAtk;

    private int spDef;

    private int spd;

    public PokemonStats(int hp, int atk, int def, int spAtk, int spDef, int spd) {
        this.hp = hp;
        this.atk = atk;
        this.def = def;
        this.spAtk = spAtk;
        this.spDef = spDef;
        this.spd = spd;
    }

    public int getHp() {
        return hp;
    }

    public int getAtk() {
        return atk;
    }

    public int getDef() {
        return def;
    }

    public int getSpAtk() {
        return spAtk;
    }

    public int getSpDef() {
        return spDef;
    }

    public int getSpd() {
        return spd;
    }
}
