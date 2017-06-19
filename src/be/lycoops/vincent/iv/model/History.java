package be.lycoops.vincent.iv.model;

import java.util.ArrayList;
import java.util.List;

public class History {

    public static String name = "a";


    public interface HistoryAction {
    }

    public class StatAction implements HistoryAction {
        private Stat stat;
        private int lowIv;
        private int highIv;

        private StatAction(Stat stat, int lowIv, int highIv) {
            this.stat = stat;
            this.lowIv = lowIv;
            this.highIv = highIv;
        }

        public Stat getStat() {
            return stat;
        }

        int getLowIv() {
            return lowIv;
        }

        int getHighIv() {
            return highIv;
        }
    }

    public class Evolution implements HistoryAction {}

    public class EvAdded implements HistoryAction {
        private Stat stat;

        private EvAdded(Stat stat) {
            this.stat = stat;
        }

        public Stat getStat() {
            return stat;
        }
    }

    private List<HistoryAction> stats = new ArrayList<>(32);

    public void reset() {
        stats.clear();
    }


    public void addStat(Stat stat, int lowIv, int highIv) {
        stats.add(new StatAction(stat, lowIv, highIv));
    }

    public void addEvolution() {
        stats.add(new Evolution());
    }

    public void addEvAdded(Stat stat) {
        stats.add(new EvAdded(stat));
    }

    public HistoryAction undo() {
        int length = stats.size();
        if (length == 0) {
            return null;
        }
        return stats.remove(length - 1);
    }
}
