package com.klotski.Model;

import com.klotski.KlotskiApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static com.klotski.Controller.SaveController.HISTORY_DIR_NAME;
import static com.klotski.Controller.SaveController.SAVES_DIR_NAME;

public class Game {

    private static final Position win = new Position(1, 3);

    private int id;
    private int score;
    private Puzzle config;
    private History history;
    private final ArrayList<Position> empties = new ArrayList<>();

    private static Game game;

    private Game() {}

    public static Game getInstance() {
        if (game == null)
            game = new Game();
        return game;
    }

    // Initializes a new game with the specified configuration "config".
    public void init(Puzzle config) {
        this.config = config;
        this.score = 0;
        this.id = 0;

        initializeHistory();
        initializeEmpties();
    }

    public void reset() {
        config.reset();
        score = 0;

        initializeHistory();
        initializeEmpties();
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

    private void initializeHistory() {
        history = new History(config.createSnapshot());
    }

    public void addSnapshot() {
        history.add(config.createSnapshot());
    }

    // Returns the list of tokens of this game.
    public ArrayList<String> download() {
        return history.download();
    }

    // Uploads the specified file named "filename" to the
    // history of this game (Load Game feature usage).
    public void upload(String filename, int index) {
        ArrayList<Snapshot> snapshots = new ArrayList<>();

        // search for dev ide build path
        // search for jar build path
        File file = new File(KlotskiApplication.class.getResource(
                "/com/klotski/data/saves/history/"
        ).getPath() + filename);
        System.out.println(file);

        Scanner reader;

        try {
            reader = new Scanner(file);
        } catch (FileNotFoundException e) {
            try {
                String path = new File(
                        KlotskiApplication.class.getProtectionDomain()
                                .getCodeSource()
                                .getLocation()
                                .toURI()
                ).getParent();
                file = new File(path + "/" + SAVES_DIR_NAME + "/" + HISTORY_DIR_NAME + "/" + filename);
                System.out.println(path + SAVES_DIR_NAME + HISTORY_DIR_NAME + filename);
                reader = new Scanner(file);
            } catch (Exception exception) {
                System.out.println("Could not find the specified file: " + e.getMessage());
                return;
            }
        }

        while (reader.hasNextLine()) {
            String token = reader.nextLine();
            snapshots.add(new Snapshot(config, token));
        }

        history.upload(snapshots, index);
    }

    public int getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public Piece getPiece(Position position) {
        return config.getPiece(position);
    }

    public Puzzle getConfig() {
        return config;
    }

    public void setConfig(String token) {
        config.restoreSnapshot(token);
        initializeEmpties();
    }

    public int getConfigId() {
        return config.getId();
    }

    public String getCurrentToken() {
        return config.createSnapshot().token();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setScore(int score) {
        this.score = score;
    }

    private boolean isMovable(Piece piece, Direction direction) {
        Piece shadow = new Piece(piece.getType(), new Position(piece.getPosition()));

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

    public Position move(Position origin, Direction direction) {
        Piece mover = getPiece(origin);

        if (isMovable(mover, direction)) {
            mover.move(direction);
            addSnapshot();
            incrementScore();
        }

        return mover.getPosition();
    }

    public void undo() {
       history.undo();

       score--;
       initializeEmpties();
    }

    public void redo() {
        history.redo();

        score++;
        initializeEmpties();
    }

    public boolean isUndoAllowed() {
        return history.isUndoAllowed();
    }

    public boolean isRedoAllowed() {
        return history.isRedoAllowed();
    }

    public boolean isGameOver() {
        return config.getMainPiecePosition().equals(win);
    }

}
