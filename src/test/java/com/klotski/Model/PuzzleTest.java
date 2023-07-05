package com.klotski.Model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PuzzleTest {

    Puzzle puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(1, "MAIN:(1,0);WIDE:(1,2);TALL:(0,0);TALL:(3,0);TALL:(0,2);TALL:(3,2);SQUARE:(0,4);SQUARE:(1,3);SQUARE:(2,3);SQUARE:(3,4);");
    }

    @Test
    void getPieces() {
        ArrayList<Piece> expected = new ArrayList<>();
        expected.add(new Piece(Type.MAIN, new Position(1, 0)));
        expected.add(new Piece(Type.WIDE, new Position(1, 2)));
        expected.add(new Piece(Type.TALL, new Position(0, 0)));
        expected.add(new Piece(Type.TALL, new Position(3, 0)));
        expected.add(new Piece(Type.TALL, new Position(0, 2)));
        expected.add(new Piece(Type.TALL, new Position(3, 2)));
        expected.add(new Piece(Type.SQUARE, new Position(0, 4)));
        expected.add(new Piece(Type.SQUARE, new Position(1, 3)));
        expected.add(new Piece(Type.SQUARE, new Position(2, 3)));
        expected.add(new Piece(Type.SQUARE, new Position(3, 4)));

        assertTrue(expected.containsAll(puzzle.getPieces()) &&
                puzzle.getPieces().containsAll(expected));
    }

    @Test
    void getId() {
        assertEquals(1, puzzle.getId());
    }

    @Test
    void getPiece() {
        Piece actual = puzzle.getPiece(new Position(1, 0));
        Piece expected = new Piece(Type.MAIN, new Position(1, 0));

        assertEquals(expected, actual);
    }

    @Test
    void reset() {
        Puzzle initial = new Puzzle(puzzle.getId(), puzzle.getConfigToken());

        // set a different configuration
        puzzle.restoreSnapshot("MAIN:(1,0);WIDE:(0,2);TALL:(0,0);TALL:(3,0);TALL:(0,3);TALL:(3,2);SQUARE:(1,4);SQUARE:(1,3);SQUARE:(2,2);SQUARE:(3,4);");
        // make sure the initial and the current are different
        assertFalse(initial.getPieces().containsAll(puzzle.getPieces())
        && puzzle.getPieces().containsAll(initial.getPieces()));
        // reset to the initial configuration
        puzzle.reset();
        // compare initial (expected) with the actual configuration
        assertTrue(initial.getPieces().containsAll(puzzle.getPieces()) &&
                puzzle.getPieces().containsAll(initial.getPieces()));
    }

    @Test
    void getPosition() {
        String position = "(1,2)";
        Position expected = new Position(1, 2);

        assertEquals(expected, puzzle.getPosition(position));

        // Try with some format (layout) errors
        assertThrows(NumberFormatException.class, () -> { puzzle.getPosition("(1,,2]"); });
    }

    @Test
    void getMainPiecePosition() {
        Position expected = new Position(1, 0);
        Position actual = puzzle.getMainPiecePosition();

        assertEquals(expected, actual);
    }

    @Test
    void createSnapshot() {
        String expected = "MAIN:(1,0);WIDE:(1,2);TALL:(0,0);TALL:(3,0);TALL:(0,2);TALL:(3,2);SQUARE:(0,4);SQUARE:(1,3);SQUARE:(2,3);SQUARE:(3,4);";
        Snapshot snap = puzzle.createSnapshot();

        assertEquals(expected, snap.token());
    }

    @Test
    void restoreSnapshot() {
        String token = "MAIN:(1,0);WIDE:(0,2);TALL:(0,0);TALL:(3,0);TALL:(0,3);TALL:(3,2);SQUARE:(1,4);SQUARE:(1,3);SQUARE:(2,2);SQUARE:(3,4);";
        puzzle.restoreSnapshot(token);

        assertEquals(token, puzzle.createSnapshot().token());
    }

    @Test
    void getTokenAt() {
        // I use a known file, which has 25 rows
        String filename = "test.hs";
        String expected = "MAIN:(1,1);WIDE:(0,0);WIDE:(2,0);TALL:(0,1);TALL:(1,3);TALL:(3,3);SQUARE:(3,1);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";

        // try the method with a valid index
        assertEquals(expected, Puzzle.getTokenAt(9, filename));

        // try the method with an invalid index
        assertNull(Puzzle.getTokenAt(50, filename));
    }
}