package application.klotski;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class KlotskiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader menuLoader = new FXMLLoader(KlotskiApplication.class.getResource("FXML/MenuView.fxml"));
        Scene scene = new Scene(menuLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(KlotskiApplication.class.getResource("style/style.css")).toExternalForm());

        Image icon = new Image(Objects.requireNonNull(KlotskiApplication.class.getResource("assets/icons/klotski.png")).toString());

        // stage config
        stage.setResizable(false);
        stage.setTitle("KLOTSKI");
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}