package application.klotski.Controller;

import application.klotski.View.LoadGameView;

import java.util.ArrayList;

public class LoadController {
    private final LoadGameView view;
    private final DatabaseConnector database = DatabaseConnector.getInstance();

    public LoadController(LoadGameView view) {
        this.view = view;

        load();
    }

    private void load() {
        // fetch the data from the database
        database.connect();
        ArrayList<DatabaseConnector.Record> records = database.fetch();
        database.close();

        // display the data
        view.display(records);
    }

}
