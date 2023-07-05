package com.klotski.Controller;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NextMoveControllerTest {

    @Test
    void next() {
        int configId = 8;
        String current = "MAIN:(1,1);WIDE:(0,0);WIDE:(2,0);TALL:(0,1);TALL:(1,3);TALL:(3,3);SQUARE:(3,1);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);";

        // check for a known current state, thus for a valid
        // returned next move
        String result = NextMoveController.next("config_" + configId, current);

        assertNotEquals("NULL", result);

        // check for an invalid configuration token
        result = NextMoveController.next("config_" + configId, "MAIN:(1,1);WIDE:(0,0);WIDE:(2,0);TALL:(0,1);TALL:(1,3);TALL:(3,3);SQUARE:(3,1);SQUARE:(2,4);SQUARE:(2,3);SQUARE:(3,2);");

        assertEquals("NULL", result);
    }
}