package application.klotski.Model;

import java.util.ArrayList;
import java.util.Scanner;

public class Puzzle {
    protected final ArrayList<Piece> pieces = new ArrayList<>();

    public void addPiece(Piece piece) {
        if (piece != null)
            pieces.add(piece);
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public Piece getPiece(Position location) {
        for (Piece piece : pieces) {
            if (piece.getLocation().equals(location)) {
                return piece;
            }
        }

        // unreachable code
        return null;
    }

    public void reset() {
        pieces.clear();
    }

    public Snapshot createSnapshot() {
        return new Snapshot(this, createToken());
    }

    public void restoreSnapshot(String token) {
        pieces.clear();

        Scanner reader = new Scanner(token);
        reader.useDelimiter(";");

        while (reader.hasNext()) {
            String curr = reader.next();

            Scanner encoder = new Scanner(curr);
            encoder.useDelimiter(":");

            Type type = getType(encoder.next());
            Position location = getPosition(encoder.next());

            // it is guaranteed to be defined, since the token is itself created
            // by the class using the same format.
            pieces.add(new Piece(type, location));
        }
    }

    protected String createToken() {
        String delim = ";";
        StringBuilder token = new StringBuilder();
        for (Piece piece : pieces) {
            token.append(piece.getType()).append(":").append(piece.getLocation()).append(delim);
        }
        return token.toString();
    }

    protected Type getType(String type) {
        switch (type) {
            case "MAIN" -> { return Type.MAIN; }
            case "WIDE" -> { return Type.WIDE; }
            case "TALL" -> { return Type.TALL; }
            case "SQUARE" -> { return Type.SQUARE; }
        }

        return null;
    }

    protected Position getPosition(String position) {
        String trimmed = position.substring(1, position.length() - 1);
        String[] parts = trimmed.split(",");

        int col = Integer.parseInt(parts[0]);
        int row = Integer.parseInt(parts[1]);

        return new Position(col, row);
    }

}
