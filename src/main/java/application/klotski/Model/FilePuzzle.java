package application.klotski.Model;

import application.klotski.KlotskiApplication;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

public final class FilePuzzle extends Puzzle {

    private static final String IMG_DIR_PATH = "/application/klotski/assets/imgs/configurations/";
    private static final String EXTENSION = ".png";

    private final File file;
    private final Image img;

    public FilePuzzle(File file) {
        this(file, IMG_DIR_PATH + "default.png");
    }

    public FilePuzzle(File file, String img) {
        this.file = file;
        this.img = new Image(Objects.requireNonNullElse(KlotskiApplication.class.getResource(IMG_DIR_PATH + img + EXTENSION), KlotskiApplication.class.getResource(IMG_DIR_PATH + "default" + EXTENSION)).toString());
        read();
    }

    private void read() {
        Scanner fileReader;

        try {
            fileReader = new Scanner(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("The specified could not be found: " + e);
        }

        while (fileReader.hasNextLine()) {
            // scan the next line in the file
            Scanner lineReader = new Scanner(fileReader.nextLine());

            // parse line information
            Type type = getType(lineReader.next());
            Position location = getPosition(lineReader.next());

            pieces.add(new Piece(type, location));
        }
    }

    public Image getImage() {
        return this.img;
    }

    @Override
    public void reset() {
        super.reset();
        read();
    }

}
