package com.klotski.View;

import com.klotski.KlotskiApplication;
import com.klotski.Model.Piece;
import com.klotski.Model.Position;
import com.klotski.Model.Puzzle;
import com.klotski.Model.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class View {

    public static final String FXML_DIR_PATH = "/com/klotski/FXML/";
    public static void switchScene(ActionEvent event, FXMLLoader loader) {
        // scene loading
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
            URL style = KlotskiApplication.class.getResource(
                    "/com/klotski/style/style.css");
            scene.getStylesheets().add(
                    style != null ? style.toExternalForm() : null
            );
        } catch (IOException e) {
            System.out.println(
                    "Could not switch to the desired scene: " + e.getMessage()
            );
        }

        // stage config
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public static WritableImage createImg(Puzzle config) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        int size = 650;

        // config new grid (setting cols & rows)
        for (int i = 0; i < Position.NUM_ROWS; i++) {
            grid.getRowConstraints().add(new RowConstraints(100));
        }
        for (int i = 0; i < Position.NUM_COLS; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(100));
        }

        for (Piece piece : config.getPieces()) {
            Rectangle rect = createRect(piece.getType());
            GridPane.setConstraints(
                    rect,
                    piece.getPosition().getCol(),
                    piece.getPosition().getRow()
            );
            grid.getChildren().add(rect);
        }

        Pane background = new Pane(grid);
        background.setStyle("-fx-background-color: #134EF2");

        double width = (Position.NUM_COLS - 1) * 15 + (Position.NUM_COLS * 100);
        double height = (Position.NUM_ROWS - 1) * 15 + (Position.NUM_ROWS * 100);
        grid.setLayoutX((size - width) / 2);
        grid.setLayoutY((size - height) / 2);

        new Scene(background, size, size);
        WritableImage image = new WritableImage(size, size);
        background.snapshot(new SnapshotParameters(), image);

        return image;
    }

    public static Rectangle createRect(Type type) {
        int width = 0;
        int height = 0;
        switch (type) {
            case MAIN -> {
                width = 215;
                height = 215;
            }
            case TALL -> {
                width = 100;
                height = 215;
            }
            case WIDE -> {
                width = 215;
                height = 100;
            }
            case SQUARE -> {
                width = 100;
                height = 100;
            }
        }

        Rectangle rect = new Rectangle(
                width,
                height,
                type == Type.MAIN ? Color.rgb(255, 77, 50) : Color.rgb(255, 219, 54)
        );

        GridPane.setValignment(rect, VPos.TOP);
        GridPane.setHalignment(rect, HPos.LEFT);

        return rect;
    }
}
