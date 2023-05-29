package application.klotski;

import application.klotski.Controller.DatabaseConnector;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

import static application.klotski.View.View.FXML_DIR_PATH;

public class KlotskiApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader Loader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_DIR_PATH + "MenuView.fxml"));

        Scene scene = new Scene(Loader.load());
        URL style = KlotskiApplication.class.getResource(
                "/application/klotski/style/style.css");
        scene.getStylesheets().add(style != null ? style.toExternalForm() : null);

        Image icon = new Image(KlotskiApplication.class.getResource("/application/klotski/assets/icons/") + "klotski.png");

        stage.getIcons().add(icon);
        stage.setResizable(false);
        stage.setTitle("KLOTSKI");
        stage.setScene(scene);
        stage.show();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            DatabaseConnector.getInstance().close();
        }));
    }

    public static void main(String[] args) {
        launch();
    }
}