package com.klotski.Model;

import java.util.ArrayList;

// Class to handle undo/redo features.
public class History {

    private ArrayList<Snapshot> history = new ArrayList<>();
    private int index = 0; // current snapshot
    private int max = -1;  // latest snapshot when not equal to -1

    public History(Snapshot snapshot) {
        history.add(snapshot);
    }

    // adds a new snapshot at the end of the history. If max is
    // set to a valid index value, then all elements from index + 1
    // to max have to be erased before the insertion.
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

    // Returns an ArrayList of String representing all
    // configuration currently stored in history.
    public ArrayList<String> download() {
        ArrayList<String> tokens = new ArrayList<>();
        for (Snapshot snapshot : history) {
            tokens.add(snapshot.token());
        }
        return tokens;
    }

    // Uploads the specified "history" to this history, setting
    // this index to the specified "index". The data member max
    // gets the right value based on the "index" value and the
    // "history" length.
    public void upload(ArrayList<Snapshot> history, int index) {
        this.history = history;
        this.index = index;
        this.max = (index == (history.size() - 1)) ? -1 : history.size() - 1;
    }
}
