package com.klotski.Controller;

import com.klotski.View.LoadGameView;
import com.klotski.Controller.DatabaseConnector.Match;

import java.util.ArrayList;

public class LoadController {
    private final LoadGameView view;
    private final DatabaseConnector database = DatabaseConnector.getInstance();

    public LoadController(LoadGameView view) {
        this.view = view;

        load();
    }

    private void load() {
        database.connect();
        ArrayList<Match> matches = database.fetchAllMatches();
        database.close();

        view.display(matches);
    }
}
