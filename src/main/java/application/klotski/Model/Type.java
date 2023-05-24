package application.klotski.Model;

public enum Type {
    MAIN(0),
    WIDE(1),
    TALL(2),
    SQUARE(3);

    public final int width;
    public final int height;

    private Type(int id) {
        width = getWidth(id);
        height = getHeight(id);
    }

    private int getHeight(int id) {
        switch (id) {
            case 0, 2 -> { return 2; }
            case 1, 3 -> { return 1; }
        }

        // unreachable code
        return 0;
    }

    private int getWidth(int id) {
        switch (id) {
            case 0, 1 -> { return 2; }
            case 2, 3 -> { return 1; }
        }

        // unreachable code
        return 0;
    }
}
