package application.klotski.Model;

import application.klotski.KlotskiApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static application.klotski.Model.Puzzle.CONFIG_EXTENSION;
import static application.klotski.Model.Puzzle.IMG_EXTENSION;

public class Game {

    private static Game game;

    private int id;
    private int moveCounter;
    private Puzzle config;
    private final ArrayList<Position> empties = new ArrayList<>();
    private History history;
    private static final Position win = new Position(1, 3);

    private Game() {}

    public static Game getInstance() {
        if (game == null)
            game = new Game();

        return game;
    }

    public void init(Puzzle config) {
        this.config = config;
        id = 0;
        moveCounter = 0;
        initializeHistory();
        initializeEmpties();
    }

    public void setMoveCounter(int count) {
        this.moveCounter = count;
    }

    public int getMoveCount() {
        return moveCounter;
    }

    public void incrementMoveCount() {
        moveCounter++;
    }

    public Puzzle getPuzzle() {
        return config;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = Math.max(0, id);
    }

    public void initializeHistory() {
        history = new History(config.createSnapshot());
    }

    private void initializeEmpties() {
        if (this.config == null)
            return;

        for (int i = 0; i < Position.NUM_ROWS; i++) {
            for (int j = 0; j < Position.NUM_COLS; j++) {
                empties.add(new Position(j, i));
            }
        }

        for (Piece piece : config.getPieces()) {
            empties.removeAll(piece.getOccupiedPositions());
        }
    }

    public String getCurrentToken() {
        return config.createToken();
    }

    public String getName() {
        return config.getName();
    }

    public Position move(Position origin, Direction direction) {
        Piece mover = getPiece(origin);

        if (isMovable(mover, direction))
            mover.move(direction);

        return mover.getLocation();
    }

    private boolean isMovable(Piece piece, Direction direction) {
        Piece shadow = new Piece(piece.getType(), new Position(piece.getLocation()));

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
        history.add(config.createSnapshot());
    }

    public void setCurrentConfiguration(String next) {
        config.restoreSnapshot(next);
        initializeEmpties();
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

    public boolean isGameOver() {
        return config.getMainLocation().equals(win);
    }

    private Piece getPiece(Position location) {
        return config.getPiece(location);
    }

    public void reset() {
        config.reset();
        moveCounter = 0;
        initializeEmpties();
        initializeHistory();
    }

    public ArrayList<String> download() {
        return history.download();
    }

    public void upload(String filename, int index) {
        ArrayList<Snapshot> snapshots = new ArrayList<>();
        File history = new File(
                Objects.requireNonNull(KlotskiApplication.class.getResource("data/saves/history/")).getFile() + filename
        );

        Scanner fileReader;

        try {
            fileReader = new Scanner(history);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified file could not be found: " + e);
        }

        while (fileReader.hasNextLine()) {
            String token = fileReader.nextLine();
            snapshots.add(new Snapshot(config, token));
        }

        this.history.upload(snapshots, index);
    }

}
