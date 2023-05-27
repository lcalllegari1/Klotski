package application.klotski.Controller;

import application.klotski.KlotskiApplication;
import application.klotski.View.LoadGameView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

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
        database.close();
        view.displayGames(records);
    }

    public static String getCurrentConfigToken(int index, String historyFileName) {
        File history = new File(Objects.requireNonNull(KlotskiApplication.class.getResource("data/saves/history/")).getFile() + historyFileName);
        Scanner fileReader;

        try {
            fileReader = new Scanner(history);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified could not be found: " + e);
        }

        int count = 0;
        String token = null;
        while (fileReader.hasNextLine()) {
            token = fileReader.nextLine();
            if (count == index) {
                return token;
            }
            count++;
        }

        // unreachable code
        return null;
    }



}
