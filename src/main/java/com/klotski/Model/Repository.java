package com.klotski.Model;

import java.util.ArrayList;

public class Repository {
    private final ArrayList<Puzzle> configs = new ArrayList<>();

    public void add(Puzzle config) {
        configs.add(config);
    }

    public ArrayList<Puzzle> getConfigs() {
        return configs;
    }
}
