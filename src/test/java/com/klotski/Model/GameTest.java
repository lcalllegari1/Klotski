package com.klotski.Model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    Game game = Game.getInstance();
    Puzzle puzzle = new Puzzle(8, "MAIN:(0,0);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);");

    @Test
    void init() {
        int expectedScore = 0;
        int expectedId = 0;

        game.init(puzzle);
        assertEquals(puzzle, game.getConfig());
        assertEquals(expectedId, game.getId());
        assertEquals(expectedScore, game.getScore());
    }


    @Test
    void upload() {
        game.init(puzzle);

        String filename = "test.hs";
        int index = 24;
        game.upload(filename, index);

        ArrayList<String> actual = game.download();
        for (int i = 0; i < actual.size(); i++) {
            assertEquals(Puzzle.getTokenAt(i, filename), actual.get(i));
        }
    }

    @Test
    void incrementScore() {
        int score = 50;
        game.setScore(50);
        game.incrementScore();

        assertEquals(score + 1, game.getScore());
    }

    @Test
    void setConfig() {
        String config = "MAIN:(1,0);WIDE:(1,2);TALL:(0,0);TALL:(3,0);TALL:(0,2);TALL:(3,2);SQUARE:(0,4);SQUARE:(1,3);SQUARE:(2,3);SQUARE:(3,4);";

        game.setConfig(config);

        assertEquals(config, game.getCurrentToken());
    }

    @Test
    void move() {
        game.init(puzzle);

        // try to move the main piece down
        assertEquals(new Position(0, 1), game.move(new Position(0, 0), Direction.DOWN));

        // try to move piece to an invalid position, and make sure
        // that the returned position is the same as the starting position,
        // so that the piece did not actually move.
        assertEquals(new Position(2, 2), game.move(new Position(2, 2), Direction.RIGHT));
    }
}