package com.klotski.Model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PositionTest {

    Position pos;

    @BeforeEach
    void setUp() {
        // Create new default Position (invalid state)
        pos = new Position();
    }

    @Test
    void getCol() {
        // test for the default constructor column value
        assertEquals(-1, pos.getCol());

        // col must be in range [0, 3].

        // check first valid column value
        pos = new Position(0, 0);
        assertEquals(0, pos.getCol());

        // check last valid column value
        pos = new Position(3, 0);
        assertEquals(3, pos.getCol());

        // check for invalid values (outside [0, 3])
        pos = new Position(6, 0);
        assertEquals(-1, pos.getCol());

        pos = new Position(-6, 0);
        assertEquals(-1, pos.getCol());
    }

    @Test
    void getRow() {
        // test for the default constructor row value
        assertEquals(-1, pos.getRow());

        // row must be in range [0, 4].

        // check first valid row value
        pos = new Position(0, 0);
        assertEquals(0, pos.getRow());

        // check last valid row value
        pos = new Position(0, 4);
        assertEquals(4, pos.getRow());

        // check for invalid values (outside [0, 4])
        pos = new Position(0, 6);
        assertEquals(-1, pos.getRow());

        pos = new Position(0, -6);
        assertEquals(-1, pos.getRow());
    }

    @Test
    void decrementRow() {
        // valid decrement
        pos = new Position(0, 3);

        pos.decrementRow();
        assertEquals(2, pos.getRow());

        // try to decrement a pos with min row value
        pos = new Position(0, 0);
        pos.decrementRow();
        assertEquals(0, pos.getRow());
    }

    @Test
    void incrementRow() {
        // valid increment
        pos = new Position(0, 2);

        pos.incrementRow();
        assertEquals(3, pos.getRow());

        // try to increment a pos with max row value
        pos = new Position(0, 4);
        pos.incrementRow();
        assertEquals(4, pos.getRow());
    }

    @Test
    void decrementCol() {
        // valid decrement
        pos = new Position(3, 0);

        pos.decrementCol();
        assertEquals(2, pos.getCol());

        // try to decrement a pos with min col value
        pos = new Position(0, 0);
        pos.decrementCol();
        assertEquals(0, pos.getCol());
    }

    @Test
    void incrementCol() {
        // valid increment
        pos = new Position(2, 0);

        pos.incrementCol();
        assertEquals(3, pos.getCol());

        // try to increment a pos with max col value
        pos = new Position(3, 0);
        pos.incrementCol();
        assertEquals(3, pos.getCol());
    }

    @Test
    void testToString() {
        pos = new Position(2, 3);
        assertEquals("(2,3)", pos.toString());
    }

    @Test
    void testEquals() {
        pos = new Position(2, 3);
        // self comparison
        assertEquals(pos, pos);
        // comparison with copy constructor use
        assertEquals(pos, new Position(pos));
        // comparison between two different positions
        assertNotEquals(pos, new Position(3, 2));
    }
}