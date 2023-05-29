package application.klotski.View;

import application.klotski.KlotskiApplication;
import application.klotski.Model.Piece;
import application.klotski.Model.Position;
import application.klotski.Model.Puzzle;
import application.klotski.Model.Type;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
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
import javafx.event.ActionEvent;

import java.io.IOException;
import java.net.URL;

import static application.klotski.Model.Type.*;

public class View {
    public static final String FXML_DIR_PATH = "/application/klotski/FXML/";

    private static final int SNAP_SIZE = 650;

    /**
     * Switches to a new scene.
     * @param event the event that causes the scene switch.
     * @param loader the provider of the new scene.
     */
    public static void switchScene(ActionEvent event, FXMLLoader loader) {
        // scene loading
        Scene scene = null;
        try {
            scene = new Scene(loader.load());
            URL style = KlotskiApplication.class.getResource(
                    "/application/klotski/style/style.css");
            scene.getStylesheets().add(style != null ? style.toExternalForm() : null);
        } catch (IOException e) {
            System.out.println("Could not switch to the desired scene: " + e.getMessage());
        }

        // stage config
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    public static WritableImage createImg(Puzzle config) {
        GridPane grid = new GridPane();
        grid.setHgap(15);
        grid.setVgap(15);

        // config new grid (setting cols & rows)
        for (int i = 0; i < Position.NUM_ROWS; i++) {
            grid.getRowConstraints().add(new RowConstraints(100));
        }
        for (int i = 0; i < Position.NUM_COLS; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(100));
        }

        for (Piece piece : config.getPieces()) {
            Rectangle rect = createRect(piece.getType());
            GridPane.setConstraints(rect, piece.getLocation().getCol(), piece.getLocation().getRow());
            grid.getChildren().add(rect);
        }


        Pane pane = new Pane(grid);
        pane.setStyle("-fx-background-color: #17255A;");
        double grid_width = (Position.NUM_COLS - 1) * 15 + (Position.NUM_COLS * 100);
        double grid_height = (Position.NUM_ROWS - 1) * 15 + (Position.NUM_ROWS * 100);
        double hcenter = (SNAP_SIZE - grid_width) / 2;
        double vcenter = (SNAP_SIZE - grid_height) / 2;
        grid.setLayoutX(hcenter);
        grid.setLayoutY(vcenter);

        new Scene(pane, SNAP_SIZE, SNAP_SIZE);
        WritableImage img = new WritableImage(SNAP_SIZE, SNAP_SIZE);
        pane.snapshot(new SnapshotParameters(), img);
        return img;
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

        Rectangle rect = new Rectangle(width, height, type == Type.MAIN ? Color.rgb(195, 66, 63) : Color.rgb(255, 222, 104));

        GridPane.setValignment(rect, VPos.TOP);
        GridPane.setHalignment(rect, HPos.LEFT);

        return rect;
    }

}
