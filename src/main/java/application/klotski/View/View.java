package application.klotski.View;

import application.klotski.KlotskiApplication;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class View {
    protected static final String FXML_PATH = "/application/klotski/FXML/";
    public void switchScene(ActionEvent event, FXMLLoader scene_loader) throws IOException {
        // scene loading
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(scene_loader.load());
        scene.getStylesheets().add(Objects.requireNonNull(KlotskiApplication.class.getResource("style/style.css")).toExternalForm());

        // stage config
        stage.setResizable(false);
        stage.setTitle("KLOTSKI");
        stage.setScene(scene);
        stage.show();
    }
}
