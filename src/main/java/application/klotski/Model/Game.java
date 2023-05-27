package application.klotski.Model;

import application.klotski.KlotskiApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Game {
    // static instance of this class to implement Singleton pattern
    private static Game game;

    private int moveCounter;
    private Puzzle puzzle;
    private final ArrayList<Position> empties = new ArrayList<>();
    private History history;
    private int id;

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
        id = 0;
        moveCounter = 0;
        initializeEmpties();
        initializeHistory();
    }

    public void setMoveCounter(int index) {
        this.moveCounter = index;
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = Math.max(0, id);
    }

    public String getInitialConfigToken() {
        return history.getInitialConfigToken();
    }

    public String getInitialConfigFile() {
        if (puzzle instanceof FilePuzzle filePuzzle) {
            return filePuzzle.getFileName();
        }
        return "";
    }

    public String getInitialConfigImg() {
        if (puzzle instanceof FilePuzzle filePuzzle) {
            return filePuzzle.getImgFileName();
        }
        return "";
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

    public ArrayList<String> download() {
        return history.download();
    }

    public void upload(String filename, int index) {
        ArrayList<Snapshot> snapshots = new ArrayList<>();
        File history = new File(Objects.requireNonNull(KlotskiApplication.class.getResource("data/saves/history/")).getFile() + filename);

        Scanner fileReader;

        try {
            fileReader = new Scanner(history);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified could not be found: " + e);
        }

        while (fileReader.hasNextLine()) {
            String token = fileReader.nextLine();
            snapshots.add(new Snapshot(this.puzzle, token));
        }

        this.history.upload(snapshots, index);
    }
}
