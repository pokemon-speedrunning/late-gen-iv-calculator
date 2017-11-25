package be.lycoops.vincent.iv.model;

import java.util.HashMap;
import java.util.Map;

public class EffortValueProvider {
    public static Map<Stat, Integer> getEffortValues(Game game, int level) {
        return getPikipekEffortValues(game, level);
        /*Map<Stat, Integer> effortValues = new HashMap<>();
        int hp = 0, atk = 0, def = 0, spd = 0, spAtk = 0, spDef = 0;
        if (game.equals(Game.MOON)) {
            switch (level) {
                case 5:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 6: // Litten (1SPD): 0 0 0 1 0 0
                    hp = 0; atk = 0; def = 0; spd = 1; spAtk = 0; spDef = 0; break;
                case 7: // Pikipek (1ATK), Rattata (1SPD): 0 1 0 2 0 0
                    hp = 0; atk = 1; def = 0; spd = 2; spAtk = 0; spDef = 0; break;
                case 8: // Pichu (1SPD), Litten (1SPD): 0 1 0 4 0 0
                    hp = 0; atk = 1; def = 0; spd = 4; spAtk = 0; spDef = 0; break;
                case 9: // Metapod (2Def): 0 1 2 4 0 0
                    hp = 0; atk = 1; def = 2; spd = 4; spAtk = 0; spDef = 0; break;
                case 10: // Bonsly (1Def): 0 1 3 4 0 0
                    hp = 0; atk = 1; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
                case 11: // Pikipek (1ATK), Grimer (1HP): 1 2 3 4 0 0
                    hp = 1; atk = 2; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
                case 12: // Magnemite (1SpATK), Meowth (1SPD), Zubat (1SPD): 1 2 3 6 1 0
                    hp = 1; atk = 2; def = 3; spd = 6; spAtk = 1; spDef = 0; break;
                case 13: // Yungoos (1ATK), Smeargle (1SPD): 1 3 3 7 1 0
                    hp = 1; atk = 3; def = 3; spd = 7; spAtk = 1; spDef = 0; break;
                case 14: // Spearow (1SPD), Drowzee (1SpDEF), 2 Rattatas (2SPD): 1 3 3 10 1 1
                    hp = 1; atk = 3; def = 3; spd = 10; spAtk = 1; spDef = 1; break;
                case 15: // Drowzee (1SpDEF), Raticate (2SPD): 1 3 3 12 1 2
                    hp = 1; atk = 3; def = 3; spd = 12; spAtk = 1; spDef = 2; break;
                case 16: // Rattata (1SPD * 2), Raticate (2SPD * 2): 1 3 3 18 1 2
                    hp = 1; atk = 3; def = 3; spd = 18; spAtk = 1; spDef = 2; break;
                case 17: // Mankey (1ATK), Makuhita (1HP), Crabrawler (1ATK): 2 5 3 18 1 2
                    hp = 2; atk = 5; def = 3; spd = 18; spAtk = 1; spDef = 2; break;
                case 18: // Delibird (1SPD), Glaceon (2SpATK): 2 5 3 19 3 2
                    hp = 2; atk = 5; def = 3; spd = 19; spAtk = 3; spDef = 2; break;
                case 19: // Zubat (1SPD), Type: Null (2HP): 4 5 3 20 3 2
                    hp = 4; atk = 5; def = 3; spd = 20; spAtk = 3; spDef = 2; break;
                case 20: // 2 Wishiwashi (2HP * 2): 8 5 3 20 3 2
                    hp = 8; atk = 5; def = 3; spd = 20; spAtk = 3; spDef = 2; break;
                case 21: // Lillipup (1ATK), Growlithe (1ATK): 8 7 3 20 3 2
                    hp = 8; atk = 7; def = 3; spd = 20; spAtk = 3; spDef = 2; break;
                case 22: // Drowzee (1SpDEF), Marowak (2DEF), Magmar (2SpATK): 8 7 5 20 5 3
                    hp = 8; atk = 7; def = 5; spd = 20; spAtk = 5; spDef = 3; break;
                case 23: // Salazzle (2SPD * 2): 8 7 5 24 5 3
                    hp = 8; atk = 7; def = 5; spd = 24; spAtk = 5; spDef = 3; break;
                case 24: // Salandit (1SPD * 2), Shiinotic (2SpDEF), 2 Fomantis (2ATK): 8 9 5 26 5 5
                    hp = 8; atk = 9; def = 5; spd = 26; spAtk = 5; spDef = 5; break;
                case 25: //
                    hp = 8; atk = 9; def = 5; spd = 26; spAtk = 5; spDef = 5; break;
                case 26: //
                    hp = 8; atk = 9; def = 5; spd = 26; spAtk = 5; spDef = 5; break;
                case 27: //
                    hp = 8; atk = 9; def = 5; spd = 26; spAtk = 5; spDef = 5; break;
                case 28: //
                    hp = 8; atk = 9; def = 5; spd = 26; spAtk = 5; spDef = 5; break;
                case 29: // Trumbeak (2ATK * 2), Lurantis (2ATK * 2), Fomantis (1ATK), Salandit (1SPD), Raticate (2SPD)
                    hp = 8; atk = 18; def = 5; spd = 29; spAtk = 5; spDef = 5; break;
                default: // Golbat (2SPD), Salandit (1SPD), Nosepass (1DEF), Boldore (1ATK + 1DEF)
                    hp = 8; atk = 19; def = 7; spd = 32; spAtk = 5; spDef = 5; break;
            }
        } else {
            switch (level) {
                case 5:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 6: // Litten (1SPD): 0 0 0 1 0 0
                    hp = 0; atk = 0; def = 0; spd = 1; spAtk = 0; spDef = 0; break;
                case 7: // Pikipek (1ATK), Rattata (1SPD): 0 1 0 2 0 0
                    hp = 0; atk = 1; def = 0; spd = 2; spAtk = 0; spDef = 0; break;
                case 8: // Pichu (1SPD), Litten (1SPD): 0 1 0 4 0 0
                    hp = 0; atk = 1; def = 0; spd = 4; spAtk = 0; spDef = 0; break;
                case 9: // Metapod (2Def): 0 1 2 4 0 0
                    hp = 0; atk = 1; def = 2; spd = 4; spAtk = 0; spDef = 0; break;
                case 10: // Bonsly (1Def): 0 1 3 4 0 0
                    hp = 0; atk = 1; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
                case 11: // Pikipek (1ATK), Grimer (1HP): 1 2 3 4 0 0
                    hp = 1; atk = 2; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
                case 12: // Magnemite (1SpATK), Meowth (1SPD), Zubat (1SPD): 1 2 3 6 1 0
                    hp = 1; atk = 2; def = 3; spd = 6; spAtk = 1; spDef = 0; break;
                case 13: // Yungoos (1ATK), Smeargle (1SPD): 1 3 3 7 1 0
                    hp = 1; atk = 3; def = 3; spd = 7; spAtk = 1; spDef = 0; break;
                case 14: // Spearow (1SPD), Drowzee (1SpDEF), 2 Yungoos (2ATK): 1 5 3 8 1 1
                    hp = 1; atk = 5; def = 3; spd = 8; spAtk = 1; spDef = 1; break;
                case 15: // Drowzee (1SpDEF), Gumshoos (2ATK): 1 7 3 8 1 2
                    hp = 1; atk = 7; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
                case 16: // Gumshoos (2ATK * 2), Yungoos (1ATK * 2): 1 13 3 8 1 2
                    hp = 1; atk = 13; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
                case 17: // Mankey (1ATK), Makuhita (1HP), Crabrawler (1ATK): 2 15 3 8 1 2
                    hp = 2; atk = 15; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
                case 18: // Slowpoke (1HP), Espeon (2SpATK): 3 15 3 8 3 2
                    hp = 3; atk = 15; def = 3; spd = 8; spAtk = 3; spDef = 2; break;
                case 19: // Zubat (1SPD), Type: Null (2HP): 5 15 3 9 3 2
                    hp = 5; atk = 15; def = 3; spd = 9; spAtk = 3; spDef = 2; break;
                case 20: // 2 Wishiwashi (2HP * 2): 9 15 3 9 3 2
                    hp = 9; atk = 15; def = 3; spd = 9; spAtk = 3; spDef = 2; break;
                case 21: // Lillipup (1ATK), Growlithe (1ATK), Drowzee (1SpDef): 9 17 3 9 3 3
                    hp = 9; atk = 17; def = 3; spd = 9; spAtk = 3; spDef = 3; break;
                case 22: // Marowak (2DEF), Magmar (2SpATK): 9 17 5 9 5 3
                    hp = 9; atk = 17; def = 5; spd = 9; spAtk = 5; spDef = 3; break;
                case 23: // Salazzle (2SPD * 2), Salandit (1SPD * 2) : 9 17 5 15 5 3
                    hp = 9; atk = 17; def = 5; spd = 15; spAtk = 5; spDef = 3; break;
                default: // Parasect (2ATK, 1DEF), 2 Fomantis (2ATK): 9 21 6 15 5 3
                    hp = 9; atk = 21; def = 6; spd = 15; spAtk = 5; spDef = 3; break;
            }
        }
        effortValues.put(Stat.HP, hp);
        effortValues.put(Stat.ATK, atk);
        effortValues.put(Stat.DEF, def);
        effortValues.put(Stat.SPD, spd);
        effortValues.put(Stat.SP_ATK, spAtk);
        effortValues.put(Stat.SP_DEF, spDef);
        return effortValues;*/
    }

    public static Map<Stat, Integer> getPikipekEffortValues(Game game, int level) {
        Map<Stat, Integer> effortValues = new HashMap<>();
        int hp = 0, atk = 0, def = 0, spd = 0, spAtk = 0, spDef = 0;
        if (game.equals(Game.MOON)) {
            switch (level) {
                case 2:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 3:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 4:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 5:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                case 6:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
                default:
                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
            }
        } else {
            throw new RuntimeException();
        }
        effortValues.put(Stat.HP, hp);
        effortValues.put(Stat.ATK, atk);
        effortValues.put(Stat.DEF, def);
        effortValues.put(Stat.SPD, spd);
        effortValues.put(Stat.SP_ATK, spAtk);
        effortValues.put(Stat.SP_DEF, spDef);
        return effortValues;
    }
}
