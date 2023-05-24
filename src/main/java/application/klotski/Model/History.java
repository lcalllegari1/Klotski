package application.klotski.Model;

import java.util.ArrayList;

public class History {
    private final ArrayList<Snapshot> history = new ArrayList<>();
    private int index = 0;
    private int max = -1;

    public History(Snapshot snapshot) {
        history.add(snapshot);
    }

    public void add(Snapshot snapshot) {
        if (max != -1) {
            history.subList(index + 1, max + 1).clear();
            max = -1;
        }

        history.add(snapshot);
        index++;
    }

    public Snapshot undo() {
        if (max == -1)
            max = index;

        index--;
        Snapshot snapshot = history.get(index);
        snapshot.restore();
        return snapshot;
    }

    public Snapshot redo() {
        index++;

        if (index == max)
            max = -1;

        Snapshot snapshot = history.get(index);
        snapshot.restore();
        return snapshot;
    }

    public boolean isUndoAllowed() {
        return this.index != 0;
    }

    public boolean isRedoAllowed() {
        return this.max != -1;
    }

    public ArrayList<Snapshot> download() {
        return this.history;
    }
}
