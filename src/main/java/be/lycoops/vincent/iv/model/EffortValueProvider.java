package be.lycoops.vincent.iv.model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class EffortValueProvider {
    public static Map<Stat, Integer> getEffortValues(Game game, int level) {




        Map<Stat, Integer> effortValues = importEffortValues(level);

        if (effortValues != null) {
            return effortValues;
        }
        effortValues = new HashMap<>();

        int hp = 0, atk = 0, def = 0, spd = 0, spAtk = 0, spDef = 0;
//        if (game.equals(Game.MOON)) {
        switch (level) {
//                case 5:
//                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
//                case 6:
//                    hp = 0; atk = 0; def = 0; spd = 1; spAtk = 0; spDef = 0; break;
//                case 7:
//                    hp = 0; atk = 1; def = 0; spd = 2; spAtk = 0; spDef = 0; break;
            case 8:
                hp = 0; atk = 0; def = 0; spAtk = 0; spDef = 0; spd = 0; break;
        }
//        } else {
//            switch (level) {
//                case 5:
//                    hp = 0; atk = 0; def = 0; spd = 0; spAtk = 0; spDef = 0; break;
//                case 6: // Litten (1SPD): 0 0 0 1 0 0
//                    hp = 0; atk = 0; def = 0; spd = 1; spAtk = 0; spDef = 0; break;
//                case 7: // Pikipek (1ATK), Rattata (1SPD): 0 1 0 2 0 0
//                    hp = 0; atk = 1; def = 0; spd = 2; spAtk = 0; spDef = 0; break;
//                case 8: // Pichu (1SPD), Litten (1SPD): 0 1 0 4 0 0
//                    hp = 0; atk = 1; def = 0; spd = 4; spAtk = 0; spDef = 0; break;
//                case 9: // Metapod (2Def): 0 1 2 4 0 0
//                    hp = 0; atk = 1; def = 2; spd = 4; spAtk = 0; spDef = 0; break;
//                case 10: // Bonsly (1Def): 0 1 3 4 0 0
//                    hp = 0; atk = 1; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
//                case 11: // Pikipek (1ATK), Grimer (1HP): 1 2 3 4 0 0
//                    hp = 1; atk = 2; def = 3; spd = 4; spAtk = 0; spDef = 0; break;
//                case 12: // Magnemite (1SpATK), Meowth (1SPD), Zubat (1SPD): 1 2 3 6 1 0
//                    hp = 1; atk = 2; def = 3; spd = 6; spAtk = 1; spDef = 0; break;
//                case 13: // Yungoos (1ATK), Smeargle (1SPD): 1 3 3 7 1 0
//                    hp = 1; atk = 3; def = 3; spd = 7; spAtk = 1; spDef = 0; break;
//                case 14: // Spearow (1SPD), Drowzee (1SpDEF), 2 Yungoos (2ATK): 1 5 3 8 1 1
//                    hp = 1; atk = 5; def = 3; spd = 8; spAtk = 1; spDef = 1; break;
//                case 15: // Drowzee (1SpDEF), Gumshoos (2ATK): 1 7 3 8 1 2
//                    hp = 1; atk = 7; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
//                case 16: // Gumshoos (2ATK * 2), Yungoos (1ATK * 2): 1 13 3 8 1 2
//                    hp = 1; atk = 13; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
//                case 17: // Mankey (1ATK), Makuhita (1HP), Crabrawler (1ATK): 2 15 3 8 1 2
//                    hp = 2; atk = 15; def = 3; spd = 8; spAtk = 1; spDef = 2; break;
//                case 18: // Slowpoke (1HP), Espeon (2SpATK): 3 15 3 8 3 2
//                    hp = 3; atk = 15; def = 3; spd = 8; spAtk = 3; spDef = 2; break;
//                case 19: // Zubat (1SPD), Type: Null (2HP): 5 15 3 9 3 2
//                    hp = 5; atk = 15; def = 3; spd = 9; spAtk = 3; spDef = 2; break;
//                case 20: // 2 Wishiwashi (2HP * 2): 9 15 3 9 3 2
//                    hp = 9; atk = 15; def = 3; spd = 9; spAtk = 3; spDef = 2; break;
//                case 21: // Lillipup (1ATK), Growlithe (1ATK), Drowzee (1SpDef): 9 17 3 9 3 3
//                    hp = 9; atk = 17; def = 3; spd = 9; spAtk = 3; spDef = 3; break;
//                case 22: // Marowak (2DEF), Magmar (2SpATK): 9 17 5 9 5 3
//                    hp = 9; atk = 17; def = 5; spd = 9; spAtk = 5; spDef = 3; break;
//                case 23: // Salazzle (2SPD * 2), Salandit (1SPD * 2) : 9 17 5 15 5 3
//                    hp = 9; atk = 17; def = 5; spd = 15; spAtk = 5; spDef = 3; break;
//                default: // Parasect (2ATK, 1DEF), 2 Fomantis (2ATK): 9 21 6 15 5 3
//                    hp = 9; atk = 21; def = 6; spd = 15; spAtk = 5; spDef = 3; break;
//            }
//        }
        effortValues.put(Stat.HP, hp);
        effortValues.put(Stat.ATK, atk);
        effortValues.put(Stat.DEF, def);
        effortValues.put(Stat.SPD, spd);
        effortValues.put(Stat.SP_ATK, spAtk);
        effortValues.put(Stat.SP_DEF, spDef);
        return effortValues;
    }

    private static final Pattern pattern = Pattern.compile("^(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)\\s+(\\d+)");

    private static Map<Stat, Integer> importEffortValues(int level) {
        File file = new File("effort-values.txt");
        if (!file.exists() || !file.canRead()) {
            return null;
        }

        final Map<Stat, Integer> effortValues = new HashMap<>();

        try (Stream<String> stream = Files.lines(Paths.get("effort-values.txt"))) {
            stream.forEach(line -> {
                Matcher m = pattern.matcher(line);
                if (m.find() && Integer.parseInt(m.group(1)) == level){
                    effortValues.put(Stat.HP, Integer.parseInt(m.group(2)));
                    effortValues.put(Stat.ATK, Integer.parseInt(m.group(3)));
                    effortValues.put(Stat.DEF, Integer.parseInt(m.group(4)));
                    effortValues.put(Stat.SP_ATK, Integer.parseInt(m.group(5)));
                    effortValues.put(Stat.SP_DEF, Integer.parseInt(m.group(6)));
                    effortValues.put(Stat.SPD, Integer.parseInt(m.group(7)));
                }
            });
            if (effortValues.size() == 0) {
                System.out.println("Default values");
                return null;
            }
            System.out.println("Custom values: ");
            System.out.println(effortValues.get(Stat.HP));
            System.out.println(effortValues.get(Stat.ATK));
            System.out.println(effortValues.get(Stat.DEF));
            System.out.println(effortValues.get(Stat.SPD));
            System.out.println(effortValues.get(Stat.SP_ATK));
            System.out.println(effortValues.get(Stat.SP_DEF));
            return effortValues;
        } catch (IOException e) {
            return null;
        }
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
