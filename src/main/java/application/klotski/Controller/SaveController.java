package application.klotski.Controller;

import application.klotski.KlotskiApplication;
import application.klotski.Model.Game;
import application.klotski.Model.Puzzle;
import application.klotski.View.View;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class SaveController {

    public static final String SAVES_DIR_PATH = "/application/klotski/data/saves/";
    public static final String SAVES_DIR_NAME = "saves";
    public static final String HISTORY_DIR_NAME = "history";
    public static final String IMGS_DIR_NAME = "imgs";

    private static final String IMG_EXTENSION = ".png";
    private static final String HISTORY_EXTENSION = ".hs";

    private final DatabaseConnector database = DatabaseConnector.getInstance();

    public boolean save() {
        // make sure that the saves directories exist
        init();

        // connect to the database
        database.connect();
        // get model information to save the game
        Game game = Game.getInstance();
        int id = game.getId();
        // check if the game is already in the database
        if (id != 0 && database.contains(id)) {
            // then we overwrite the record with updated information
            // 1) get the database information
            // 2) update the game information (date, move_count)
            DatabaseConnector.Record record = database.fetch(id);

            if (record == null) {
                // something went wrong, thus the save cannot be completed
                return false;
            }

            createImg(game.getPuzzle(), record.config());
            createHistory(game.download(), record.history());

            return database.update(id, getCurrentTime(), game.getMoveCount());

        } else {
            // then we create a new record to save the game
            id = database.createRecord(new DatabaseConnector.Record(
                    0,
                    getCurrentTime(),
                    game.getPuzzle().getName(),
                    game.getMoveCount(),
                    createImg(game.getPuzzle()),
                    createHistory(game.download())
            ));
            game.setId(id);
            return id != 0;
        }
    }

    private void init() {
        String path = Objects.requireNonNull(
                KlotskiApplication.class.getResource("/application/klotski/data/")
        ).getPath();

        // create the saves directory
        File dir = new File(path + SAVES_DIR_NAME);
        dir.mkdir();
        File imgs = new File(dir, IMGS_DIR_NAME);
        imgs.mkdir();
        File history = new File(dir, HISTORY_DIR_NAME);
        history.mkdir();
    }

    private String createImg(Puzzle config) {
        String filename = UUID.randomUUID() + IMG_EXTENSION;
        createImg(config, filename);
        return filename;
    }

    private void createImg(Puzzle config, String filename) {
        WritableImage image = View.createImg(config);
        File img = new File(Objects.requireNonNull(
                KlotskiApplication.class.getResource(SAVES_DIR_PATH + IMGS_DIR_NAME)
        ).getPath() + filename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", img);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    private String createHistory(ArrayList<String> history) {
        String filename = UUID.randomUUID() + HISTORY_EXTENSION;
        createHistory(history, filename);
        return filename;
    }

    private void createHistory(ArrayList<String> history, String filename) {
        File file = new File(Objects.requireNonNull(
                KlotskiApplication.class.getResource(SAVES_DIR_PATH + HISTORY_DIR_NAME)
        ).getPath() + filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String token : history) {
                writer.println(token);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not write history to the specified file.");
        }
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    public static void delete(String history, String img) {
        File file = new File(Objects.requireNonNull(
                KlotskiApplication.class.getResource(SAVES_DIR_PATH + HISTORY_DIR_NAME)
        ).getPath() + history);

        if (file.delete()) {
            System.out.println("history deleted");
        }

        file = new File(Objects.requireNonNull(
                KlotskiApplication.class.getResource(SAVES_DIR_PATH + IMGS_DIR_NAME)
        ).getPath() + img);

        if (file.delete()) {
            System.out.println("img deleted");
        }
    }

}
