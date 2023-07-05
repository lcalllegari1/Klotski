package com.klotski.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HistoryTest {

    String config = "MAIN:(0,0);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";
    History history;
    Puzzle puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(8, config);
        history = new History(new Snapshot(puzzle, config));
    }

    @Test
    void add() {
        // add a new element
        String element = "MAIN:(0,1);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);\n";
        history.add(new Snapshot(puzzle, element));

        // check if the last element is the correct new
        // inserted element.
        int index = history.download().size() - 1;
        assertEquals(element, history.download().get(index));
    }

    @Test
    void undo() {
        String element = "MAIN:(0,1);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);\n";
        history.add(new Snapshot(puzzle, element));
        // Try the undo
        assertEquals(config, history.undo().token());
    }

    @Test
    void redo() {
        String element = "MAIN:(0,1);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";
        history.add(new Snapshot(puzzle, element));

        // undo first (necessary to do redo)
        history.undo();

        // try the redo
        assertEquals(element, history.redo().token());
    }

    @Test
    void isUndoAllowed() {
        // undo should not be allowed when the history contains
        // only the initial configuration snapshot, see setUp() for
        // details
        assertFalse(history.isUndoAllowed());

        // add an element
        String element = "MAIN:(0,1);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";
        history.add(new Snapshot(puzzle, element));

        // when the history contains multiple elements, and the index
        // does not refer to the first one, then the undo should be
        // allowed
        assertTrue(history.isUndoAllowed());
    }

    @Test
    void isRedoAllowed() {
        // redo should not be allowed when the history contains
        // only the initial configuration snapshot, see setUp() for
        // details
        assertFalse(history.isRedoAllowed());

        // add an element
        String element = "MAIN:(0,1);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";
        history.add(new Snapshot(puzzle, element));
        history.undo();

        // when the history contains multiple elements, and the index
        // does not refer to the last one, then the redodo should be
        // allowed
        assertTrue(history.isRedoAllowed());
    }
}