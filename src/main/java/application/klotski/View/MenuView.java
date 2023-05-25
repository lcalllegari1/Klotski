package application.klotski.View;

import application.klotski.Controller.ConfigurationController;
import application.klotski.KlotskiApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class MenuView extends View {

    @FXML
    void play(ActionEvent event) {
        // loader new scene (PuzzleSelectionView)
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_PATH + "PuzzleSelectionView.fxml"));
        try {
            switchScene(event, loader);
        } catch (IOException e) {
            throw new RuntimeException("Could not switch to the desired scene: " + e);
        }

        // get the view handler of the scene
        PuzzleSelectionView view = loader.getController();
        // create a new controller to handle this view
        ConfigurationController controller = new ConfigurationController(view);
        // link the view to the controller to handle events
        view.setController(controller);
    }

    @FXML
    void load(ActionEvent event) {
        // TODO: load saved game view
        // TODO: Close DATABASE FOR SECURITY REASONS
    }


}