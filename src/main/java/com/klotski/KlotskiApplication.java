package com.klotski;

import com.klotski.Model.Piece;
import com.klotski.Model.Position;
import com.klotski.Model.Type;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

import static com.klotski.View.View.FXML_DIR_PATH;

public class KlotskiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Scene loading

        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(
                        FXML_DIR_PATH + "MenuView.fxml"
                )
        );
        Scene scene = new Scene(loader.load());

        // Set the stylesheet to this scene
        URL style = KlotskiApplication.class.getResource(
                "/com/klotski/style/style.css");
        scene.getStylesheets().add(style != null ? style.toExternalForm() : null);

        // Load the icon for the application
        Image icon = new Image(
                KlotskiApplication.class.getResource(
                        "/com/klotski/assets/icons/"
                ) + "klotski.png"
        );

        // Stage configuration

        stage.getIcons().add(icon);
        stage.setTitle("KLOTSKI");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}