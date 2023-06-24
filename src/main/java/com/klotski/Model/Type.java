package com.klotski.Model;

public enum Type {
    MAIN(0),
    WIDE(1),
    TALL(2),
    SQUARE(3);

    public final int width;
    public final int height;

    // Sets the width & height of the type with the specified id
    Type(int id) {
        // MAIN & WIDE are 2 units wide, TALL & SQUARE are 1 unit wide
        width = (id == 0 || id == 1) ? 2 : 1;
        // MAIN & TALL are 2 units tall, WIDE & SQUARE are 1 unit tall
        height = (id == 0 || id == 2) ? 2 : 1;
    }
}
