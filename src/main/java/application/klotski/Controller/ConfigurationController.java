package application.klotski.Controller;

import application.klotski.KlotskiApplication;
import application.klotski.Model.FilePuzzle;
import application.klotski.Model.Puzzle;
import application.klotski.Model.PuzzleRepository;
import application.klotski.View.PuzzleSelectionView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class ConfigurationController {
    private static final String CONFIG_DIR_PATH = "/application/klotski/data/configurations";

    private final PuzzleSelectionView view;
    private final PuzzleRepository repository;

    public ConfigurationController(PuzzleSelectionView view) {
        this.view = view;
        this.repository = new PuzzleRepository();
        init();
    }

    public void init() {
        File dir = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(CONFIG_DIR_PATH)).getPath());
        ArrayList<File> files = new ArrayList<>();
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                files.add(file);
            }
        }
        files.sort((file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName()));
        for (File file : files) {
            Puzzle config = new FilePuzzle(file, getFileName(file));
            repository.addConfig(config);
        }
        view.displayConfigs(repository.getConfigs());
    }

    public Puzzle getConfig(int id) {
        return repository.getConfigs().get(id - 1);
    }

    private String getFileName(File file) {
        String name = file.getName();
        int index = name.lastIndexOf(".");
        return name.substring(0, index);
    }
}
