package be.lycoops.vincent.iv.model.json;

public class PokemonModel {

    private int startLevel;

    private int randomGuaranteedPerfectStats;

    private String nature;

    private PokemonStats baseStats;

    private PokemonStats baseStatsEvo;

    private PokemonStats guaranteedStats;

    public PokemonModel(int startLevel, int randomGuaranteedPerfectStats, String nature, PokemonStats baseStats, PokemonStats baseStatsEvo, PokemonStats guaranteedStats) {
        this.startLevel = startLevel;
        this.randomGuaranteedPerfectStats = randomGuaranteedPerfectStats;
        this.nature = nature;
        this.baseStats = baseStats;
        this.baseStatsEvo = baseStatsEvo;
        this.guaranteedStats = guaranteedStats;
    }

    public int getStartLevel() {
        return startLevel;
    }

    public int getRandomGuaranteedPerfectStats() {
        return randomGuaranteedPerfectStats;
    }

    public String getNature() {
        return nature;
    }

    public PokemonStats getBaseStats() {
        return baseStats;
    }

    public PokemonStats getBaseStatsEvo() {
        return baseStatsEvo;
    }

    public PokemonStats getGuaranteedStats() {
        return guaranteedStats;
    }
}
