package application.klotski.Controller;

import application.klotski.Model.*;
import application.klotski.View.GameView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GameController {

    private record MouseInput(double x, double y, MouseEvent event) {}

    private final SaveController saveController = new SaveController();

    private MouseInput mouseInput;
    private final Game game;
    private final GameView view;

    public GameController(GameView view, int id) {
        this.view = view;
        this.game = Game.getInstance();

        DatabaseConnector database = DatabaseConnector.getInstance();
        database.connect();
        DatabaseConnector.Record record = database.fetch(id);
        // get the file to read the configuration
        Puzzle config = new Puzzle(record.name());
        config.restoreSnapshot(
                Puzzle.getTokenAt(record.move_count(), record.history())
        );
        load(record.id(), config, record.history(), record.move_count());
        database.close();
    }

    public GameController(GameView view, Puzzle config) {
        this.view = view;
        this.game = Game.getInstance();

        load(config);
    }

    private void load(Puzzle config) {
        game.init(config);
        view.update(config, game.getMoveCount());
    }

    private void load(int id, Puzzle config, String history, int count) {
        game.init(config);
        game.setId(id);
        game.upload(history, count);
        game.setMoveCounter(count);

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
        if (game.isGameOver()) {
           view.displayWinMessage();
        }

        view.display(config);
        view.updateMoveCounter(count);
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
            // then there is a valid direction, thus try to move the piece
            Position target = game.move(origin, direction);

            if (target.equals(origin)) {
                // then it means the piece did not actually move
                view.displayMoveDeniedAnimation(source, direction);
            } else {
                // then the piece changed its location, thus update game & view

                // game updates
                game.addSnapshot();
                game.incrementMoveCount();

                // view updates
                view.updateMoveCounter(game.getMoveCount());
                view.enableUndoBtn();
                view.disableRedoBtn();
                view.enableSaveBtn();

                GridPane.setConstraints(
                        source, target.getCol(), target.getRow()
                );

                if (game.isGameOver()) {
                    view.displayWinMessage();
                    view.disableNextMoveBtn();
                } else {
                    view.hideWinMessage();
                    view.enableNextMoveBtn();
                }
            }
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

    public void undoActionHandler() {
        Puzzle puzzle = game.undo();

        if (!game.isUndoAllowed())
            view.disableUndoBtn();

        if (game.isGameOver()) {
            view.displayWinMessage();
            view.disableNextMoveBtn();
        } else {
            view.hideWinMessage();
            view.enableNextMoveBtn();
        }

        view.enableSaveBtn();
        view.enableRedoBtn();
        view.update(puzzle, game.getMoveCount());
    }

    public void redoActionHandler() {
        Puzzle puzzle = game.redo();

        if (!game.isRedoAllowed())
            view.disableRedoBtn();

        if (game.isGameOver()) {
            view.displayWinMessage();
            view.disableNextMoveBtn();
        } else {
            view.hideWinMessage();
            view.enableNextMoveBtn();
        }

        view.enableSaveBtn();
        view.enableUndoBtn();
        view.update(puzzle, game.getMoveCount());
    }

    public void resetActionHandler() {
        game.reset();
        view.disableUndoBtn();
        view.disableRedoBtn();
        view.disableSaveBtn();
        view.hideWinMessage();
        view.enableNextMoveBtn();
        view.update(game.getPuzzle(), 0);
    }

    public void save() {
        if (saveController.save()) {
            System.out.println("saved");
            view.disableSaveBtn();
        } else {
            System.out.println("not saved");
        }
    }

    public void nextMoveActionHandler() {
        String next = NextMoveController.next(
                game.getName(),
                game.getCurrentToken()
        );

        if (!next.equals("NULL")) {
            // then there is a valid next move
            // now next represents the new configuration
            game.setCurrentConfiguration(next);
            // game updates
            game.addSnapshot();
            game.incrementMoveCount();

            // view updates
            view.updateMoveCounter(game.getMoveCount());
            view.display(game.getPuzzle());
            view.enableUndoBtn();
            view.disableRedoBtn();
            view.enableSaveBtn();

            if (game.isGameOver()) {
                view.displayWinMessage();
                view.disableNextMoveBtn();
            } else {
                view.hideWinMessage();
            }
        } else {
            // we could not find a suitable move, thus perform a random move
            if (game.isUndoAllowed()) {
                // perform a normal undo
                undoActionHandler();
            }
        }
    }

}
