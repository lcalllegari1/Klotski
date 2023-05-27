package application.klotski.View;

import application.klotski.KlotskiApplication;
import application.klotski.Model.Piece;
import application.klotski.Model.Position;
import application.klotski.Model.Puzzle;
import application.klotski.Model.Type;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
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

    public static WritableImage createImg(Puzzle puzzle) {
        // create new grid
        GridPane grid = new GridPane();

        // config new grid (setting cols & rows)
        for (int i = 0; i < Position.NUM_ROWS; i++) {
            grid.getRowConstraints().add(new RowConstraints(100));
        }
        for (int i = 0; i < Position.NUM_COLS; i++) {
            grid.getColumnConstraints().add(new ColumnConstraints(100));
        }

        grid.setHgap(15);
        grid.setVgap(15);

        for (Piece piece : puzzle.getPieces()) {
            Rectangle rect = createImgRectangle(piece.getType());
            GridPane.setConstraints(rect, piece.getLocation().getCol(), piece.getLocation().getRow());
            grid.getChildren().add(rect);
        }

        // now we have a grid representing the current configuration
        Pane pane = new Pane(grid);
        pane.setStyle("-fx-background-color: #08415C;");
        double grid_width = (Position.NUM_COLS - 1) * 15 + (Position.NUM_COLS * 100);
        double grid_height = (Position.NUM_ROWS - 1) * 15 + (Position.NUM_ROWS * 100);
        double hcenter = (650 - grid_width) / 2;
        double vcenter = (650 - grid_height) / 2;
        grid.setLayoutX(hcenter);
        grid.setLayoutY(vcenter);

        Scene scene = new Scene(pane, 650, 650);

        WritableImage img = new WritableImage(650, 650);
        pane.snapshot(new SnapshotParameters(), img);

        return img;
    }

    public static Rectangle createImgRectangle(Type type) {
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

        Rectangle rect = new Rectangle(width, height, type == Type.MAIN ? Color.rgb(255, 111, 0) : Color.rgb(255, 222, 104));
        GridPane.setValignment(rect, VPos.TOP);
        GridPane.setHalignment(rect, HPos.LEFT);

        return rect;
    }
}
