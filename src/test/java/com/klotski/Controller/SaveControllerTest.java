package com.klotski.Controller;

import com.klotski.Model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;
import java.io.File;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SaveControllerTest {

    SaveController saver = new SaveController();
    Game game = Game.getInstance();
    Puzzle puzzle;

    @BeforeEach
    void setUp() {
        puzzle = new Puzzle(8, "MAIN:(0,0);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);");
        game.init(puzzle);
    }

    @Test
    void save() {
        // Try to save a new game
        assertTrue(saver.save());

        int id = game.getId();
        // make sure the game now has a valid id, meaning
        // it has been successfully saved as a record inside
        // the database
        assertNotEquals(0, id);

        DatabaseConnector database = DatabaseConnector.getInstance();
        database.connect();
        // Try to get the info of the saved game from the database
        DatabaseConnector.Match result = database.getMatch(id);

        database.close();

        // compare the saved information with the actual game
        // information (match_id, score, config_id)
        assertEquals(id, result.match_id());
        assertEquals(game.getScore(), result.score());
        assertEquals(game.getConfigId(), result.config_id());

        // try to read the history file and compare the contents
        ArrayList<String> expected = game.download();

        for (int i = 0; i < expected.size(); i++) {
            assertEquals(expected.get(i), Puzzle.getTokenAt(i, result.history()));
        }

        // Modify the game
        game.move(new Position(0, 0), Direction.DOWN);
        game.move(new Position(2, 0), Direction.LEFT);
        game.move(new Position(1, 0), Direction.LEFT);
        game.move(new Position(2, 1), Direction.UP);
        game.move(new Position(2, 2), Direction.UP);
        game.move(new Position(2, 1), Direction.RIGHT);

        // Try to update a saved game
        assertTrue(saver.save());

        database.connect();
        // Try to get the info of the saved game from the database
        result = database.getMatch(id);

        database.close();

        int updatedId = game.getId();

        // The update should not modify the game id, meaning
        // it should not affect any other database record than
        // the one referring to that game.
        assertEquals(id, updatedId);

        // compare the updated information with the actual game
        // information (match_id, score, config_id)
        assertEquals(game.getScore(), result.score());
        assertEquals(game.getConfigId(), result.config_id());

        database.connect();
        database.deleteMatch(id);
        database.close();
    }

    @Test
    void init() {
        // try the init method
        saver.init();

        // at this point we should have the folders (history & imgs)
        assertTrue(saver.getHistory().isDirectory());
        assertTrue(saver.getImgs().isDirectory());
    }

    @Test
    void delete() {
        // try to create a temporary file to delete
        assertTrue(saver.save()); // then we saved the history file

        int id = game.getId();

        // get the history file and check if it exists
        DatabaseConnector database = DatabaseConnector.getInstance();
        database.connect();
        String filename = database.getMatch(id).history();
        database.close();

        File history = new File(saver.getHistory(), filename);

        assertTrue(history.isFile());

        // delete the game
        saver.delete(game.getId());

        // check the database does not contain the game anymore
        database.connect();
        assertNull(database.getMatch(game.getId()));
        database.close();
    }
}