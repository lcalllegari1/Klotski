package application.klotski.Model;

import java.util.ArrayList;

public class PuzzleRepository {
    private final ArrayList<Puzzle> configs = new ArrayList<>();

    public void addConfig(Puzzle config) {
        configs.add(config);
    }

    public ArrayList<Puzzle> getConfigs() {
        return this.configs;
    }

}
