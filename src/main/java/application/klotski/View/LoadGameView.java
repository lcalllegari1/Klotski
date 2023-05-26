package application.klotski.View;

import application.klotski.Controller.LoadGameController;
import application.klotski.KlotskiApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;

import java.io.IOException;

public class LoadGameView extends View {

    private LoadGameController loadGameController;

    @FXML
    private Pane pane;

    @FXML
    private void backToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource("FXML/MenuView.fxml"));
        try {
            switchScene(event, loader);
        } catch (IOException e) {
            throw new RuntimeException("Could not switch to the desired scene: " + e);
        }
    }

    public void setController(LoadGameController controller) {
        this.loadGameController = controller;
    }

}
