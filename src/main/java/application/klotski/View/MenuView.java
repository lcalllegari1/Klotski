package application.klotski.View;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MenuView {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}