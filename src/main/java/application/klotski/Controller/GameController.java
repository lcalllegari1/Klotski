package application.klotski.Controller;

import application.klotski.Model.Direction;
import application.klotski.Model.Game;
import application.klotski.Model.Position;
import application.klotski.Model.Puzzle;
import application.klotski.View.GameView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;

public class GameController {
    private record MouseInput(double x, double y, MouseEvent event) {}

    private MouseInput mouseInput;
    private final Game game;
    private final GameView view;

    public GameController(GameView view, Puzzle puzzle) {
        this.view = view;
        this.game = Game.getInstance();

        load(puzzle);
    }

    private void load(Puzzle config) {
        game.init(config);
        view.displayConfig(config);
    }

    public void mousePressedEvent(MouseEvent event) {
        mouseInput = new MouseInput(event.getSceneX(), event.getSceneY(), event);
    }

    public void mouseReleasedEvent(MouseEvent event) {
        // input parsing
        double deltaX = event.getSceneX() - mouseInput.x;
        double deltaY = event.getSceneY() - mouseInput.y;
        Rectangle source = (Rectangle) mouseInput.event.getSource();

        Position origin = new Position(GridPane.getColumnIndex(source), GridPane.getRowIndex(source));
        Direction direction = getDirection(deltaX, deltaY);

        if (direction != null) {
            Position target = game.move(origin, direction);

            if (target.equals(origin)) {
                // the piece did not actually move
                view.displayMoveDeniedAnimation(source, direction);
            } else {
                // game updates
                game.addSnapshot();
                game.incrementMoveCount();

                // view updates
                view.updateMoveCounter(game.getMoveCount());
                view.enableUndoBtn();
                view.disableRedoBtn();

                GridPane.setConstraints(source, target.getCol(), target.getRow());
            }
        }
    }

    public void undoActionHandler() {
        Puzzle puzzle = game.undo();

        if (!game.isUndoAllowed())
            view.disableUndoBtn();

        view.update(puzzle, game.getMoveCount());
    }

    public void redoActionHandler() {
        Puzzle puzzle = game.redo();

        if (!game.isRedoAllowed())
            view.disableRedoBtn();

        view.update(puzzle, game.getMoveCount());
    }

    public void resetActionHandler() {
        game.reset();
        view.update(game.getPuzzle(), 0);
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
}
