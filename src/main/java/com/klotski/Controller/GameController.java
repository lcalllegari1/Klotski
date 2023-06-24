package com.klotski.Controller;

import com.klotski.Model.Direction;
import com.klotski.Model.Game;
import com.klotski.Model.Position;
import com.klotski.Model.Puzzle;
import com.klotski.View.GameView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

import com.klotski.Controller.DatabaseConnector.Match;

public class GameController {

    private record MouseInput(double x, double y, MouseEvent event) {}
    private MouseInput mouseInput;

    private final Game game;
    private final GameView view;

    private final SaveController saver = new SaveController();

    public GameController(GameView view, Puzzle config) {
        this.view = view;
        this.game = Game.getInstance();

        load(config);
    }

    public GameController(GameView view, int id) {
        this.view = view;
        this.game = Game.getInstance();

        DatabaseConnector database = DatabaseConnector.getInstance();
        database.connect();

        // get the match data from the database
        Match match = database.getMatch(id);
        int configId = match.config_id();

        // get the configuration of the saved game
        Puzzle config = new Puzzle(configId, database.getConfig(configId).token());

        // restore the config status of the last snapshot
        config.restoreSnapshot(
            Puzzle.getTokenAt(match.score(), match.history())
        );

        // load the match
        load(id, config, match.score(), match.history());
        database.close();
    }

    public void load(Puzzle config) {
        game.init(config);
        view.display(config);
        checkAndSetUndoRedo();
    }

    public void load(int id, Puzzle config, int score, String history) {
        game.init(config);
        game.setId(id);
        game.setScore(score);
        game.upload(history, score);

        checkAndSetUndoRedo();
        checkAndSetWinMessage();

        view.update(config, score);
    }

    private void checkAndSetWinMessage() {
        if (game.isGameOver()) {
            view.displayWinMessage();
            view.disableNextMoveBtn();
        } else {
            view.hideWinMessage();
            view.enableNextMoveBtn();
        }
    }

    private void checkAndSetUndoRedo() {
        if (game.isUndoAllowed()) {
            view.enableUndoBtn();
        } else {
            view.disableUndoBtn();
        }

        if (game.isRedoAllowed()) {
            view.enableRedoBtn();
        } else {
            view.disableRedoBtn();
        }
    }

    public void mousePressedEvent(MouseEvent event) {
        mouseInput = new MouseInput(event.getSceneX(), event.getSceneY(), event);
    }

    public void mouseReleasedEvent(MouseEvent event) {
        double deltaX = event.getSceneX() - mouseInput.x;
        double deltaY = event.getSceneY() - mouseInput.y;

        Rectangle source = (Rectangle) mouseInput.event.getSource();
        Position origin = new Position(
                GridPane.getColumnIndex(source),
                GridPane.getRowIndex(source)
        );
        Direction direction = getDirection(deltaX, deltaY);

        if (direction != null) {
            move(source, origin, direction);
        }
    }

    private void move(Rectangle source, Position origin, Direction direction) {
        Position target = game.move(origin, direction);

        if (target.equals(origin)) {
            // then the piece could not move to the specified direction,
            // thus show the denied move animation.
            view.displayMoveDeniedAnimation(source, direction);
        } else {
            // the move has been completed, thus update the view.
            view.setScore(game.getScore());
            view.enableSaveBtn();
            checkAndSetUndoRedo();
            checkAndSetWinMessage();

            // update the position of the moved piece inside the grid.
            GridPane.setConstraints(
                    source, target.getCol(), target.getRow()
            );
        }
    }

    private Direction getDirection(double deltaX, double deltaY) {
        if (deltaX == 0 && deltaY == 0)
            return null;

        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            return deltaX > 0 ? Direction.RIGHT : Direction.LEFT;
        } else {
            return deltaY > 0 ? Direction.DOWN : Direction.UP;
        }
    }

    public void save() {
        if (saver.save()) {
            System.out.println("saved");
            view.disableSaveBtn();
        } else {
            System.out.println("not saved");
        }
    }

    public void reset() {
        game.reset();
        checkAndSetUndoRedo();
        checkAndSetWinMessage();
        view.enableSaveBtn();
        view.update(game.getConfig(), 0);
    }

    public void undo() {
        game.undo();

        checkAndSetUndoRedo();
        checkAndSetWinMessage();
        view.enableSaveBtn();
        view.update(game.getConfig(), game.getScore());
    }

    public void redo() {
        game.redo();

        checkAndSetUndoRedo();
        checkAndSetWinMessage();
        view.enableSaveBtn();
        view.update(game.getConfig(), game.getScore());
    }

    public void nextMove() {
        String next = NextMoveController.next(
                "config_" + game.getConfigId(),
                game.getCurrentToken()
        );

        if (next.equals("NULL")) {
            // the next move was not found, thus
            // perform an undo.
            if (game.isUndoAllowed()) {
                undo();
            }
        } else {
            // perform the suggested next move
            game.setConfig(next);
            game.addSnapshot();
            game.incrementScore();

            view.setScore(game.getScore());
            view.display(game.getConfig());
            view.enableSaveBtn();
            checkAndSetUndoRedo();
            checkAndSetWinMessage();
        }
    }
}
