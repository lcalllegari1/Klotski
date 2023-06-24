package com.klotski.View;

import com.klotski.Controller.ConfigurationController;
import com.klotski.Controller.LoadController;
import com.klotski.KlotskiApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;

import static com.klotski.View.View.FXML_DIR_PATH;

public class MenuView {

    @FXML
    private Button load_btn;

    @FXML
    private Button play_btn;

    @FXML
    void load(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(
           FXML_DIR_PATH + "LoadGame.fxml"
        ));
        View.switchScene(event, loader);

        LoadGameView view = loader.getController();
        LoadController controller = new LoadController(view);
    }

    @FXML
    void play(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(
                FXML_DIR_PATH + "PuzzleSelectionView.fxml"
        ));
        View.switchScene(event, loader);

        PuzzleSelectionView view = loader.getController();
        ConfigurationController controller = new ConfigurationController(view);
        view.setController(controller);
    }

}
