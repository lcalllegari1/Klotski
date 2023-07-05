package com.klotski.Controller;

import com.klotski.Model.Direction;
import com.klotski.Model.Game;
import com.klotski.Model.Position;
import com.klotski.Model.Puzzle;
import com.klotski.View.GameView;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import org.mockito.*;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class GameControllerTest {

    GameController controller;
    Game game = Game.getInstance();
    @Mock
    GameView view;
    Puzzle puzzle;

    @BeforeEach
    void setUp() throws IOException {
        puzzle = new Puzzle(8, "MAIN:(0,0);WIDE:(2,0);WIDE:(2,1);TALL:(0,3);TALL:(1,3);TALL:(3,3);SQUARE:(2,2);SQUARE:(2,3);SQUARE:(2,4);SQUARE:(3,2);");
        game.init(puzzle);
        view = mock(GameView.class);
        Mockito.doNothing().when(view).display(puzzle);
        Mockito.doNothing().when(view).update(ArgumentMatchers.any(), ArgumentMatchers.anyInt());
        Mockito.doNothing().when(view).setScore(ArgumentMatchers.anyInt());
        Mockito.doNothing().when(view).disableNextMoveBtn();
        Mockito.doNothing().when(view).disableRedoBtn();
        Mockito.doNothing().when(view).disableUndoBtn();
        Mockito.doNothing().when(view).disableSaveBtn();
        Mockito.doNothing().when(view).enableSaveBtn();
        Mockito.doNothing().when(view).enableUndoBtn();
        Mockito.doNothing().when(view).enableRedoBtn();
        Mockito.doNothing().when(view).enableSaveBtn();
        Mockito.doNothing().when(view).enableNextMoveBtn();
        Mockito.doNothing().when(view).displayWinMessage();
        Mockito.doNothing().when(view).hideWinMessage();
        controller = new GameController(view, puzzle);
    }

    @Test
    void loadNewGame() {
        // load the puzzle into a new game
        controller.load(puzzle);

        // check if the game is correctly being initialized
        assertEquals(0, game.getScore());
        assertEquals(0, game.getId());
        assertEquals(puzzle.getConfigToken(), game.getCurrentToken());

        // when loading a new game, the undo/redo should be disabled
        assertFalse(game.isUndoAllowed());
        assertFalse(game.isRedoAllowed());
    }

    @Test
    void loadSavedGame() {

        // Modify the game according to the history file "test.hs"
        game.move(new Position(0, 0), Direction.DOWN);
        game.move(new Position(2, 0), Direction.LEFT);
        game.move(new Position(1, 0), Direction.LEFT);
        game.move(new Position(2, 1), Direction.UP);
        game.move(new Position(2, 2), Direction.UP);
        game.move(new Position(2, 1), Direction.RIGHT);
        game.setScore(6);

        DatabaseConnector database = DatabaseConnector.getInstance();
        database.connect();
        DatabaseConnector.Match match = new DatabaseConnector.Match(
                0,
                game.getScore(),
                "2023-07-01 10:28:28",
                "test.hs",
                "test.png",
                8
        );
        int id = database.createMatch(match);
        database.close();

        controller.load(id, puzzle, match.score(), match.history());

        assertEquals(match.score(), game.getScore());
        assertEquals(match.config_id(), game.getConfigId());

        // In the current game state undo and redo should be allowed,
        // since the test file has 24 moves, and the game is loaded
        // at the index (score) of 6, meaning it is loaded at the state
        // that it was after move 6.
        assertTrue(game.isUndoAllowed());
        assertTrue(game.isRedoAllowed());

        database.connect();
        assertTrue(database.deleteMatch(id));
        database.close();
    }

    @Test
    void reset() {
        game.move(new Position(0, 0), Direction.DOWN);
        game.move(new Position(2, 0), Direction.LEFT);
        game.move(new Position(1, 0), Direction.LEFT);
        game.move(new Position(2, 1), Direction.UP);
        game.move(new Position(2, 2), Direction.UP);
        game.move(new Position(2, 1), Direction.RIGHT);
        game.setScore(6);

        controller.reset();
        assertEquals(0, game.getScore());
        assertEquals(puzzle.getConfigToken(), game.getCurrentToken());
        assertFalse(game.isUndoAllowed());
        assertFalse(game.isRedoAllowed());
    }

    @Test
    void nextMove() {
        // setup to get a valid next move

        game.move(new Position(0, 0), Direction.DOWN);
        game.move(new Position(2, 0), Direction.LEFT);
        game.move(new Position(1, 0), Direction.LEFT);
        game.move(new Position(2, 1), Direction.UP);
        game.move(new Position(2, 2), Direction.UP);
        game.move(new Position(2, 1), Direction.RIGHT);
        game.setScore(6);

        controller.nextMove();
        assertEquals(7, game.getScore());

        game.reset();

        // setup to get an invalid next move (meaning an undo takes place)
        game.move(new Position(0, 0), Direction.DOWN);
        game.move(new Position(2, 0), Direction.LEFT);
        game.move(new Position(1, 0), Direction.LEFT);
        game.move(new Position(2, 1), Direction.UP);
        game.move(new Position(2, 2), Direction.UP);
        game.move(new Position(3, 2), Direction.LEFT);
        game.setScore(6);

        controller.nextMove();
        assertEquals(5, game.getScore());
    }
}