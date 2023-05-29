package application.klotski.Model;

import java.util.ArrayList;

public class Piece {

    private final Type type;
    private final int height;
    private final int width;
    private final Position location;

    public Piece(Type type, Position location) {
        this.type = type;
        this.location = location;
        this.height = type.height;
        this.width = type.width;
    }

    public Type getType() {
        return type;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public Position getLocation() {
        return location;
    }

    public Position move(Direction direction) {
        switch (direction) {
            case UP -> location.decrementRow();
            case DOWN -> location.incrementRow();
            case LEFT -> location.decrementCol();
            case RIGHT -> location.incrementCol();
        }
        return this.location;
    }

    public ArrayList<Position> getOccupiedPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < this.height; i++) {
            // through rows
            for (int j = 0; j < this.width; j++) {
                // through columns
                positions.add(new Position(location.getCol() + j, location.getRow() + i));
            }
        }
        return positions;
    }
}
