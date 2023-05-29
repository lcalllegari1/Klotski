package application.klotski.View;

import application.klotski.Controller.ConfigurationController;
import application.klotski.Controller.LoadController;
import application.klotski.KlotskiApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import static application.klotski.View.View.FXML_DIR_PATH;

public class MenuView {

    @FXML
    void play(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_DIR_PATH + "PuzzleSelectionView.fxml"));
        View.switchScene(event, loader);

        // get the view handler of the scene
        PuzzleSelectionView view = loader.getController();
        // create a new controller to handle this view
        ConfigurationController controller = new ConfigurationController(view);
        // link the view to the controller to handle events
        view.setController(controller);
    }

    @FXML
    void load(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_DIR_PATH + "LoadGame.fxml"));
        View.switchScene(event, loader);

        // get the view handler of the scene
        LoadGameView view = loader.getController();
        // create a new controller to handle this view
        LoadController controller = new LoadController(view);
    }
}
