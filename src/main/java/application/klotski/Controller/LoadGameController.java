package application.klotski.Controller;

import application.klotski.View.LoadGameView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class LoadGameController {

    private final LoadGameView view;
    private final DatabaseConnector database;

    public LoadGameController(LoadGameView view) {
        this.view = view;
        database = new DatabaseConnector();
        database.connect();

        load();
    }

    private void load() {
        HashMap<Integer, DatabaseConnector.Record> records = database.fetch();
        for (Map.Entry<Integer, DatabaseConnector.Record> entry : records.entrySet()) {
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        // TODO: create view for each saved game
    }

    public void closeConnection() {
        database.close();
    }




}
