package com.klotski.Model;

import java.util.ArrayList;
import java.util.Objects;

public class Piece {

    private final Type type;
    private final Position position;

    public Piece(Type type, Position position) {
        this.type = type;
        this.position = position;
    }

    public Type getType() {
        return type;
    }

    public Position getPosition() {
        return position;
    }

    public int getWidth() {
        return type.width;
    }

    public int getHeight() {
        return type.height;
    }

    // Moves this Piece in the specified direction changing its location.
    public Position move(Direction direction) {
        switch (direction) {
            case UP -> position.decrementRow();
            case DOWN -> position.incrementRow();
            case LEFT -> position.decrementCol();
            case RIGHT -> position.incrementCol();
        }
        return position;
    }

    // Returns an ArrayList of Position objects that represents the occupied
    // positions of this Piece based on its location and its width & height.
    public ArrayList<Position> getOccupiedPositions() {
        ArrayList<Position> positions = new ArrayList<>();
        for (int i = 0; i < type.height; i++) {
            // through rows
            for (int j = 0; j < type.width; j++) {
                // through columns
                positions.add(new Position(
                        position.getCol() + j, position.getRow() + i)
                );
            }
        }
        return positions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return type == piece.type && position.equals(((Piece) o).getPosition());
    }
}
