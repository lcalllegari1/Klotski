package com.klotski.Model;

import com.klotski.Controller.SaveController;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Puzzle {

    public static final String CONFIG_IMG_PATH = "/com/klotski/assets/imgs/configurations/";
    public static final String CONFIG_FILENAME = "config";
    public static final String DELIM = "_";
    public static final String CONFIG_IMG_EXTENSION = ".png";

    private final ArrayList<Piece> pieces;
    private final int id;
    private final String config;

    public Puzzle(int id, String config) {
        this.pieces = new ArrayList<>();
        this.id = id;
        this.config = config;

        restore(config);
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public int getId() {
        return id;
    }

    public String getPath() {
        return CONFIG_IMG_PATH + CONFIG_FILENAME + DELIM + id + CONFIG_IMG_EXTENSION;
    }

    // Returns the Piece currently occupying the specified
    // "position" parameter, if such piece exists, null otherwise.
    public Piece getPiece(Position position) {
        for (Piece piece : pieces) {
            if (piece.getPosition().equals(position)) {
                return piece;
            }
        }
        return null;
    }

    // Resets this puzzle to its initial state.
    public void reset() {
        restore(config);
    }

    // Restores this puzzle according to the specified
    // "config" String parameter.
    private void restore(String config) {
        pieces.clear();

        Scanner reader = new Scanner(config);
        reader.useDelimiter(";");

        while (reader.hasNext()) {
            Scanner decoder = new Scanner(reader.next());
            decoder.useDelimiter(":");

            Type type = Type.valueOf(decoder.next());
            Position location = getPosition(decoder.next());

            pieces.add(new Piece(type, location));
        }
    }

    // Returns the Position object represented by the specified
    // "position" string. It assumes that the String parameter
    // is always in a valid state and this is guaranteed by the
    // class invariants regarding token creation and handling.
    public Position getPosition(String position) {
        String[] parts = position.substring(1, position.length() - 1).split(",");

        int col = Integer.parseInt(parts[0]);
        int row = Integer.parseInt(parts[1]);

        return new Position(col, row);
    }

    public Position getMainPiecePosition() {
        for (Piece piece : pieces) {
            if (piece.getType() == Type.MAIN)
                return piece.getPosition();
        }
        return null;
    }

    // Creates a new Snapshot representing the current
    // state of this puzzle.
    public Snapshot createSnapshot() {
        return new Snapshot(this, createToken());
    }

    // Restores this puzzle's pieces with the configuration
    // specified by "config".
    public void restoreSnapshot(String config) {
        restore(config);
    }

    // Creates a new String token to represent the current
    // disposition of pieces.
    private String createToken() {
        StringBuilder token = new StringBuilder();
        for (Piece piece : pieces) {
            token.append(piece.getType())
                    .append(":")
                    .append(piece.getPosition())
                    .append(";");
        }
        return token.toString();
    }

    public static String getTokenAt(int index, String filename) {
        SaveController saver = new SaveController();
        File history = new File(saver.getHistory(), filename);
        Scanner reader;

        try {
            reader = new Scanner(history);
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the file to delete: " + e.getMessage());
            return null;
        }

        int count = 0;
        String token;
        while (reader.hasNextLine()) {
            token = reader.nextLine();
            if (count == index) {
                return token;
            }
            count++;
        }
        return null;
    }


}
