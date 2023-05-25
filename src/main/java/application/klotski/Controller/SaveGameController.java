package application.klotski.Controller;

import application.klotski.KlotskiApplication;
import application.klotski.Model.Game;
import application.klotski.Model.Puzzle;
import application.klotski.View.View;
import javafx.scene.image.WritableImage;

import javafx.embed.swing.*;

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
import java.util.concurrent.atomic.AtomicReferenceArray;

public class SaveGameController {

    // to save game history
    private static final String HISTORY_SAVES_DIR = "/application/klotski/data/saves/history/";
    private static final String IMGS_SAVES_DIR = "/application/klotski/data/saves/imgs/";
    private static final String HISTORY_DIR_NAME = "history";
    private static final String IMGS_DIR_NAME = "imgs";
    private static final String SAVES_DIR_NAME = "saves";

    private static final String IMG_EXTENSION = ".png";
    private static final String HISTORY_EXTENSION = ".hs";

    private static final DatabaseConnector database = new DatabaseConnector();

    // returns true if save has been completed successfully, false otherwise
    public static boolean save() {
        init();

        // connect to the database
        database.connect();

        // get model information
        Game game = Game.getInstance();
        int gameId = game.getId();

        // we prepare the minimum required information
        String name = getCurrentTime();
        int move_count = game.getMoveCount();
        String curr_config_img = createImg(game.getPuzzle());
        String history_file = createHistory(game.download());

        // if it does not exist in database, then create a new record
        if (gameId == 0 || !database.contains(gameId)) {
            // then we create a new record
            gameId = database.createRecord(new DatabaseConnector.Record(
                    name,
                    move_count,
                    game.getInitialConfigToken(),
                    game.getInitialConfigFile(),
                    game.getInitialConfigImg(),
                    curr_config_img,
                    history_file
            ));
            game.setId(gameId);
            return gameId != 0;
        } else {
            // the gameId is already in the database, so we just update
            // the corresponding record
            DatabaseConnector.Record record = database.fetch(gameId);
            if (record == null) {
                return false;
            }
            // the record is now fetched
            String old_config_img = record.curr_config_img();
            String old_history_file = record.history_file();
            // clear the old file, since we are setting the new ones
            clear(old_config_img, old_history_file);
            // record update
            record = new DatabaseConnector.Record(
                    name,
                    move_count,
                    record.init_config_token(),
                    record.init_config_file(),
                    record.init_config_img(),
                    curr_config_img,
                    history_file
            );
            return database.update(gameId, record);
        }
    }

    private static String createImg(Puzzle puzzle) {
        WritableImage image = View.createImg(puzzle);

        String filename = UUID.randomUUID().toString() + IMG_EXTENSION;
        File img = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(IMGS_SAVES_DIR)).getPath() + filename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", img);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }

        return filename;
    }

    private static String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd_HH-mm-ss");
        return formatter.format(now);
    }

    // init directories
    private static void init() {
        String path = Objects.requireNonNull(KlotskiApplication.class.getResource("data")).getPath();
        File dir = new File(path + SAVES_DIR_NAME);
        dir.mkdir();
        dir = new File(path + SAVES_DIR_NAME + "/" + IMGS_DIR_NAME);
        dir.mkdir();
        dir = new File(path + SAVES_DIR_NAME + "/" + HISTORY_DIR_NAME);
        dir.mkdir();
    }

    private static String createHistory(ArrayList<String> history) {
        String filename = UUID.randomUUID().toString() + HISTORY_EXTENSION;
        File file = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(HISTORY_SAVES_DIR)).getPath() + filename);

        try (PrintWriter writer = new PrintWriter(file)) {
            for (String token : history) {
                writer.println(token);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Could not write history to the specified file.");
        }

        return filename;
    }

    private static void clear(String img, String history) {
        File file = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(IMGS_SAVES_DIR)).getPath() + img);
        file.delete();
        file = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(HISTORY_SAVES_DIR)).getPath() + history);
        file.delete();
    }


}
