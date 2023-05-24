package application.klotski.Model;

import java.util.ArrayList;

public class Game {
    // static instance of this class to implement Singleton pattern
    private static Game game;

    private int moveCounter;
    private Puzzle puzzle;
    private final ArrayList<Position> empties = new ArrayList<>();
    private History history;

    // private main constructor to prevent multiple objects of type Game
    private Game() {}

    // static method to implement Singleton pattern
    public static Game getInstance() {
        if (game == null)
            game = new Game();

        return game;
    }

    public void init(Puzzle config) {
        puzzle = config;
        initializeEmpties();
        initializeHistory();
    }

    public int getMoveCount() {
        return moveCounter;
    }

    public void incrementMoveCount() {
        moveCounter++;
    }

    public Puzzle getPuzzle() {
        return puzzle;
    }

    // member functions

    public void initializeHistory() {
        history = new History(puzzle.createSnapshot());
    }

    private void initializeEmpties() {
        if (this.puzzle == null)
            return;

        for (int i = 0; i < Position.NUM_ROWS; i++) {
            for (int j = 0; j < Position.NUM_COLS; j++) {
                empties.add(new Position(j, i));
            }
        }

        for (Piece piece : puzzle.getPieces()) {
            empties.removeAll(piece.getOccupiedPositions());
        }
    }

    public Position move(Position origin, Direction direction) {
        Piece mover = getPiece(origin);

        if (isMovable(mover, direction))
            mover.move(direction);

        return mover.getLocation();
    }

    private boolean isMovable(Piece piece, Direction direction) {
        Piece shadow = new Piece(piece);

        empties.addAll(piece.getOccupiedPositions());
        shadow.move(direction);

        boolean movable = empties.containsAll(shadow.getOccupiedPositions());

        if (movable) {
            empties.removeAll(shadow.getOccupiedPositions());
        } else {
            empties.removeAll(piece.getOccupiedPositions());
        }

        return movable;
    }

    public void addSnapshot() {
        history.add(puzzle.createSnapshot());
    }

    public Puzzle undo() {
        Snapshot snapshot = history.undo();
        initializeEmpties();
        moveCounter--;
        return snapshot.puzzle();
    }

    public Puzzle redo() {
        Snapshot snapshot = history.redo();
        initializeEmpties();
        moveCounter++;
        return snapshot.puzzle();
    }

    public boolean isUndoAllowed() {
        return history.isUndoAllowed();
    }

    public boolean isRedoAllowed() {
        return history.isRedoAllowed();
    }

    private Piece getPiece(Position location) {
        return puzzle.getPiece(location);
    }

    public void reset() {
        puzzle.reset();
        moveCounter = 0;
        initializeEmpties();
        initializeHistory();
    }
}
