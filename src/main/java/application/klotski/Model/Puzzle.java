package application.klotski.Model;

import application.klotski.KlotskiApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import static application.klotski.Controller.SaveController.*;

public class Puzzle {
    public static final String CONFIG_DIR_PATH = "/application/klotski/data/configurations/";
    public static final String IMG_DIR_PATH = "/application/klotski/assets/imgs/configurations/";
    public static final String CONFIG_EXTENSION = ".dat";
    public static final String IMG_EXTENSION = ".png";

    private final ArrayList<Piece> pieces;
    private final String name;

    public Puzzle(String name) {
        this.pieces = new ArrayList<>();
        this.name = name;

        // fill the arraylist with pieces
        read();
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public String getName() {
        return name;
    }

    public Piece getPiece(Position location) {
        for (Piece piece : pieces) {
            if (piece.getLocation().equals(location)) {
                return piece;
            }
        }
        return null;
    }

    private void read() {
        Scanner fileReader;
        File file = new File(
                Objects.requireNonNull(KlotskiApplication.class.getResource(CONFIG_DIR_PATH + name + CONFIG_EXTENSION)).getPath()
        );

        try {
            fileReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.println("The specified file could not be found: " + e.getMessage());
            return;
        }

        while (fileReader.hasNextLine()) {
            // read and parse the next line of the file
            Scanner lineReader = new Scanner(fileReader.nextLine());
            Type type = Type.valueOf(lineReader.next());
            Position location = getLocation(lineReader.next());

            pieces.add(new Piece(type, location));
        }
    }

    private Position getLocation(String location) {
        String trimmed = location.substring(1, location.length() - 1);
        String[] parts = trimmed.split(",");

        int col = Integer.parseInt(parts[0]);
        int row = Integer.parseInt(parts[1]);

        return new Position(col, row);
    }

    public Position getMainLocation() {
        for (Piece piece : pieces) {
            if (piece.getType() == Type.MAIN)
                return piece.getLocation();
        }
        return null;
    }

    public void reset() {
        pieces.clear();
        read();
    }

    protected String createToken() {
        String delim = ";";
        StringBuilder token = new StringBuilder();
        for (Piece piece : pieces) {
            token.append(piece.getType()).append(":").append(piece.getLocation()).append(delim);
        }
        return token.toString();
    }

    public Snapshot createSnapshot() {
        return new Snapshot(this, createToken());
    }

    public void restoreSnapshot(String token) {
        pieces.clear();

        Scanner reader = new Scanner(token);
        reader.useDelimiter(";");

        while (reader.hasNext()) {
            Scanner decoder = new Scanner(reader.next());
            decoder.useDelimiter(":");

            Type type = Type.valueOf(decoder.next());
            Position location = getLocation(decoder.next());

            pieces.add(new Piece(type, location));
        }
    }

    public static String getTokenAt(int index, String filename) {
        File history = new File(Objects.requireNonNull(
                KlotskiApplication.class.getResource(SAVES_DIR_PATH + HISTORY_DIR_NAME)
        ).getFile() + filename);

        Scanner fileReader;
        try {
            fileReader = new Scanner(history);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified file could not be found: " + e);
        }

        int count = 0;
        String token = null;
        while (fileReader.hasNextLine()) {
            token = fileReader.nextLine();
            if (count == index) {
                return token;
            }
            count++;
        }
        // unreachable code
        return null;
    }
}
