package com.klotski.Controller;

import com.klotski.Model.Puzzle;
import com.klotski.Model.Repository;
import com.klotski.View.PuzzleSelectionView;

import java.util.ArrayList;

import com.klotski.Controller.DatabaseConnector.Config;

public class ConfigurationController {
    private final Repository repository;
    private final PuzzleSelectionView view;

    public ConfigurationController(PuzzleSelectionView view) {
        this.view = view;
        this.repository = new Repository();

        load();
    }

    private void load() {
        DatabaseConnector connector = DatabaseConnector.getInstance();
        connector.connect();
        ArrayList<Config> configs = connector.fetchAllConfigs();

        for (Config config : configs) {
            Puzzle puzzle = new Puzzle(config.config_id(), config.token());
            repository.add(puzzle);
        }
        view.display(repository.getConfigs());
        connector.close();
    }

    public Puzzle getConfig(int index) {
        return repository.getConfigs().get(index);
    }
}
