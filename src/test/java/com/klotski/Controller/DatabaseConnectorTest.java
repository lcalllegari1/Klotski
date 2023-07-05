package com.klotski.Controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseConnectorTest {

    DatabaseConnector database;
    DatabaseConnector.Match match;

    @BeforeEach
    void setUp() {
        // create a connection to operate on the database
        database = DatabaseConnector.getInstance();
        database.connect();

        match = new DatabaseConnector.Match(
                0,
                0,
                "2023-02-07 16:48:28",
                "test.hs",
                "test.png",
                8
        );

    }

    @AfterEach
    void tearDown() {
        // close the connection to the database
        database.close();
    }

    @Test
    void getMatch() {
        // insert a new match in the database
        int id = database.createMatch(match);
        DatabaseConnector.Match result = database.getMatch(id);

        // check if the inserted match can be successfully
        // retrieved.
        assertEquals(match.score(), result.score());
        assertEquals(match.date(), result.date());
        assertEquals(match.history(), result.history());
        assertEquals(match.img(), result.img());
        assertEquals(match.config_id(), result.config_id());

        // delete the temporary match from the database.
        assertTrue(database.deleteMatch(id));

        // try to get a match with an invalid id

        id = -1;

        result = database.getMatch(id);
        assertNull(result);
    }

    @Test
    void createMatch() {
        int id = database.createMatch(match);

        // id must be > 0 if the match was correctly added to the database
        assertNotEquals(0, id);

        // remove the match from the database after the test
        database.deleteMatch(id);
    }

    @Test
    void updateMatch() {
        int id = database.createMatch(match);
        int count = 10;
        String date = "2023-02-07 18:28:28";

        // update the record in the database
        database.updateMatch(id, count, date);

        DatabaseConnector.Match result = database.getMatch(id);

        assertEquals(count, result.score());
        assertEquals(date, result.date());

        database.deleteMatch(id);
    }

    @Test
    void deleteMatch() {
        int id = database.createMatch(match);

        assertTrue(database.deleteMatch(id));
    }

    @Test
    void getConfig() {
        // try to get a valid configuration
        int id = 2;
        assertNotNull(database.getConfig(id));

        // try to get an invalid configuration
        id = -1;
        assertNull(database.getConfig(id));
    }

    @Test
    void fetchAllConfigs() {
        // checks that all the records from the
        // Configs table are returned.
        assertFalse(database.fetchAllConfigs().isEmpty());
    }
}