package application.klotski.Controller;

import application.klotski.KlotskiApplication;
import application.klotski.Model.Puzzle;
import application.klotski.Model.Repository;
import application.klotski.View.PuzzleSelectionView;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

import static application.klotski.Model.Puzzle.CONFIG_DIR_PATH;


public class ConfigurationController {

    private final Repository repository;
    private final PuzzleSelectionView view;

    public ConfigurationController(PuzzleSelectionView view) {
        this.view = view;
        this.repository = new Repository();

        load();
    }

    private void load() {
        File dir = new File(Objects.requireNonNull(KlotskiApplication.class.getResource(CONFIG_DIR_PATH)).getPath());
        ArrayList<File> files = new ArrayList<>();

        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isFile()) {
                files.add(file);
            }
        }
        files.sort(
                (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName())
        );

        for (File file : files) {
            String name = file.getName();
            Puzzle config = new Puzzle(name.substring(0, name.lastIndexOf(".")));
            repository.add(config);
        }
        view.displayConfigs(repository.getConfigs());
    }

    public Puzzle getConfig(int index) {
        return repository.getConfigs().get(index);
    }


}