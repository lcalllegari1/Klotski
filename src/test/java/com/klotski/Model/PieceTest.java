package com.klotski.Model;

import javafx.geometry.Pos;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {

    Position pos;

    @BeforeEach
    void setUp() {
        pos = new Position(1, 1);
    }

    @Test
    void getType() {
        Piece piece = new Piece(Type.MAIN, pos);
        assertEquals(Type.MAIN, piece.getType());

        piece = new Piece(Type.WIDE, pos);
        assertEquals(Type.WIDE, piece.getType());

        piece = new Piece(Type.TALL, pos);
        assertEquals(Type.TALL, piece.getType());

        piece = new Piece(Type.SQUARE, pos);
        assertEquals(Type.SQUARE, piece.getType());
    }

    @Test
    void getPosition() {
        Piece piece = new Piece(Type.MAIN, pos);
        assertEquals(pos, piece.getPosition());
    }

    @Test
    void getWidth() {
        Piece piece = new Piece(Type.MAIN, pos);
        assertEquals(2, piece.getWidth());

        piece = new Piece(Type.WIDE, pos);
        assertEquals(2, piece.getWidth());

        piece = new Piece(Type.TALL, pos);
        assertEquals(1, piece.getWidth());

        piece = new Piece(Type.SQUARE, pos);
        assertEquals(1, piece.getWidth());
    }

    @Test
    void getHeight() {
        Piece piece = new Piece(Type.MAIN, pos);
        assertEquals(2, piece.getHeight());

        piece = new Piece(Type.WIDE, pos);
        assertEquals(1, piece.getHeight());

        piece = new Piece(Type.TALL, pos);
        assertEquals(2, piece.getHeight());

        piece = new Piece(Type.SQUARE, pos);
        assertEquals(1, piece.getHeight());
    }

    @Test
    void move() {
        // test move up
        pos = new Position(1, 1);
        Piece piece = new Piece(Type.SQUARE, pos);
        assertEquals(new Position(1, 0), piece.move(Direction.UP));


        // test move down
        pos = new Position(1, 1);
        piece = new Piece(Type.SQUARE, pos);
        assertEquals(new Position(1, 2), piece.move(Direction.DOWN));

        // test move left
        pos = new Position(1, 1);
        piece = new Piece(Type.SQUARE, pos);
        assertEquals(new Position(0, 1), piece.move(Direction.LEFT));

        // test move right
        pos = new Position(1, 1);
        piece = new Piece(Type.SQUARE, pos);
        assertEquals(new Position(2, 1), piece.move(Direction.RIGHT));
    }

    @Test
    void getOccupiedPositions() {
        Piece piece;
        Position pos;

        // test MAIN piece occupied positions
        pos = new Position(1, 1);
        piece = new Piece(Type.MAIN, pos);
        ArrayList<Position> expected = new ArrayList<>();
        expected.add(new Position(1, 1));
        expected.add(new Position(1, 2));
        expected.add(new Position(2, 1));
        expected.add(new Position(2, 2));

        assertTrue(expected.containsAll(piece.getOccupiedPositions())
                && piece.getOccupiedPositions().containsAll(expected));

        // test WIDE piece occupied positions
        pos = new Position(1, 1);
        piece = new Piece(Type.WIDE, pos);
        expected = new ArrayList<>();
        expected.add(new Position(1, 1));
        expected.add(new Position(2, 1));

        assertTrue(expected.containsAll(piece.getOccupiedPositions())
                && piece.getOccupiedPositions().containsAll(expected));

        // test TALL piece occupied positions
        pos = new Position(1, 1);
        piece = new Piece(Type.TALL, pos);
        expected = new ArrayList<>();
        expected.add(new Position(1, 1));
        expected.add(new Position(1, 2));

        assertTrue(expected.containsAll(piece.getOccupiedPositions())
                && piece.getOccupiedPositions().containsAll(expected));

        // test SQUARE piece occupied positions
        pos = new Position(1, 1);
        piece = new Piece(Type.SQUARE, pos);
        expected = new ArrayList<>();
        expected.add(new Position(1, 1));

        assertTrue(expected.containsAll(piece.getOccupiedPositions())
                && piece.getOccupiedPositions().containsAll(expected));
    }
}