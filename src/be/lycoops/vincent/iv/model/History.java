package be.lycoops.vincent.iv.model;

import java.util.ArrayList;
import java.util.List;

public class History {
    public interface HistoryAction {
    }

    public class Stat implements HistoryAction {
        private String stat;
        private int lowIv;
        private int highIv;

        private Stat(String stat, int lowIv, int highIv) {
            this.stat = stat;
            this.lowIv = lowIv;
            this.highIv = highIv;
        }

        public String getStat() {
            return stat;
        }

        public int getLowIv() {
            return lowIv;
        }

        public int getHighIv() {
            return highIv;
        }
    }

    public class Evolution implements HistoryAction {}

    public class EvAdded implements HistoryAction {
        private String stat;

        private EvAdded(String stat) {
            this.stat = stat;
        }

        public String getStat() {
            return stat;
        }
    }

    private List<HistoryAction> stats = new ArrayList<>(32);

    public void reset() {
        stats.clear();
    }

    public void addStat(String stat, int lowIv, int highIv) {
        stats.add(new Stat(stat, lowIv, highIv));
    }

    public void addEvolution() {
        stats.add(new Evolution());
    }

    public void addEvAdded(String stat) {
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
