package application.klotski.Model;

public record Snapshot(Puzzle puzzle, String token) {

    public void restore() {
        puzzle.restoreSnapshot(this.token);
    }
}
