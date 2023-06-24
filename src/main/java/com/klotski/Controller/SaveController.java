package com.klotski.Controller;

import com.klotski.KlotskiApplication;
import com.klotski.Model.Game;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.UUID;

import com.klotski.Controller.DatabaseConnector.Match;
import com.klotski.Model.Puzzle;
import com.klotski.View.View;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;

public class SaveController {
    public static final String DIR_PATH = "com/klotski/data/saves/";

    public static final String SAVES_DIR_NAME = "saves";
    public static final String HISTORY_DIR_NAME = "history";
    public static final String IMG_DIR_NAME = "imgs";

    public static final String HISTORY_EXTENSION = ".hs";
    public static final String IMG_EXTENSION = ".png";

    private final DatabaseConnector database = DatabaseConnector.getInstance();
    private File history;
    private File imgs;

    public SaveController() {
        init();
    }

    public boolean save() {
        database.connect();

        Game game = Game.getInstance();
        int id = game.getId();
        Match match;

        if (id == 0 || (match = database.getMatch(id)) == null) {
            // the game does not belong to the database
            match = new Match(
                    0,
                    game.getScore(),
                    getCurrentTime(),
                    createHistory(game.download()),
                    createImg(game.getConfig()),
                    game.getConfigId()
            );

           id = database.createMatch(match);
           game.setId(id);
           return id != 0;
        } else {
            // the game is in the database, we just need to update it.
            updateHistory(match.history(), game.download());
            updateImg(match.img(), game.getConfig());

            return database.updateMatch(
                    match.match_id(), game.getScore(), getCurrentTime()
            );
        }
    }

    public void init() {
        // create saves directories if needed
        String path = KlotskiApplication.class.getProtectionDomain()
                .getCodeSource().getLocation().getPath();

        File dir = new File(path + DIR_PATH);

        if (!dir.mkdir() && !dir.isDirectory()) {
            // then we're running from within the jar file.
            dir = new File(new File(path).getParent() + "/" + SAVES_DIR_NAME);
            dir.mkdir();
        }

        imgs = new File(dir, IMG_DIR_NAME);
        history = new File(dir, HISTORY_DIR_NAME);
        imgs.mkdir();
        history.mkdir();
    }


    public File getHistory() {
        return history;
    }

    public File getImgs() {
        return imgs;
    }

    private String createHistory(ArrayList<String> tokens) {
        String filename = UUID.randomUUID() + HISTORY_EXTENSION;
        writeHistory(filename, tokens);
        return filename;
    }

    private void updateHistory(String filename, ArrayList<String> tokens) {
        writeHistory(filename, tokens);
    }

    private void writeHistory(String filename, ArrayList<String> tokens) {
        File file = new File(history, filename);
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String token : tokens) {
                writer.println(token);
            }
            writer.flush();
        } catch (FileNotFoundException e) {
            System.out.println("Could not write the history file: " + e.getMessage());
        }
    }

    private String createImg(Puzzle config) {
        String filename = UUID.randomUUID().toString() + IMG_EXTENSION;
        writeImg(filename, config);
        return filename;
    }

    private void updateImg(String filename, Puzzle config) {
        writeImg(filename, config);
    }

    private void writeImg(String filename, Puzzle config) {
        WritableImage image = View.createImg(config);

        File file = new File(imgs, filename);

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            System.out.println("Failed to save screenshot: " + e.getMessage());
        }
    }

    private String getCurrentTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy-MM-dd HH:mm:ss");
        return formatter.format(now);
    }

    public void delete(int id) {
        database.connect();
        Match match = database.getMatch(id);
        database.deleteMatch(id);
        // delete the history file
        File file = new File(history, match.history());
        file.delete();

        // delete the img file
        file = new File(imgs, match.img());
        file.delete();
    }


}
