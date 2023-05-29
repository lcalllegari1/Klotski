package application.klotski.Model;

// class to handle positions of pieces inside the board game
public class Position {

    // class constants
    // values depends on the grid size (see GameView.fxml)
    public static final int NUM_ROWS = 5;
    public static final int NUM_COLS = 4;

    private static final int MIN_ROW_INDEX = 0;
    private static final int MIN_COL_INDEX = 0;
    private static final int MAX_ROW_INDEX = 4;
    private static final int MAX_COL_INDEX = 3;

    // data members
    private int col;
    private int row;

    // constructors

    // default constructor
    // creates a new position that is invalid
    public Position() {
        this.col = this.row = -1;
    }

    // main constructor
    // assures that a position is either valid or invalid, where the invalid
    // state is represented by the value -1 for both col and row, coherently
    // with the default constructor initialization of data members
    public Position(int col, int row) {
        this.col = (col >= MIN_COL_INDEX && col <= MAX_COL_INDEX) ? col : -1;
        this.row = (row >= MIN_ROW_INDEX && row <= MAX_ROW_INDEX) ? row : -1;
    }

    // copy constructor
    public Position(Position pos) {
        this.col = pos.col;
        this.row = pos.row;
    }

    // getters
    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    // setters

    // they guarantee that the value is set only if it is valid with respect to
    // bounds imposed by the class constants.
    private void setCol(int col) {
        if (col >= MIN_COL_INDEX && col <= MAX_COL_INDEX)
            this.col = col;
    }

    private void setRow(int row) {
        if (row >= MIN_ROW_INDEX && row <= MAX_ROW_INDEX)
            this.row = row;
    }

    // member functions

    // UP
    public void decrementRow() {
        setRow(this.row - 1);
    }

    // DOWN
    public void incrementRow() {
        setRow(this.row + 1);
    }

    // LEFT
    public void decrementCol() {
        setCol(this.col - 1);
    }

    // RIGHT
    public void incrementCol() {
        setCol(this.col + 1);
    }

    @Override
    public String toString() {
        return "(" + this.col + "," + this.row + ")";
    }

    // two position are equals if they share the same values for both the col
    // and row data members.
    public boolean equals(Object obj) {
        if (obj instanceof Position pos) {
            return (this.col == pos.col && this.row == pos.row);
        }
        return false;
    }
}