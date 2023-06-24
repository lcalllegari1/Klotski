package com.klotski.View;

import com.klotski.Controller.ConfigurationController;
import com.klotski.Controller.GameController;
import com.klotski.KlotskiApplication;
import com.klotski.Model.Puzzle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;

import static com.klotski.View.View.FXML_DIR_PATH;

public class PuzzleSelectionView {
    @FXML
    private Pane root;
    private static final int NUM_COLS = 3;

    private ConfigurationController configurationController;

    public void setController(ConfigurationController controller) {
        configurationController = controller;
    }

    public void display(ArrayList<Puzzle> configs) {
        // create the grid that holds all possible configs
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        for (int i = 0; i < configs.size(); i++) {
            int col = i % NUM_COLS;
            int row = i / NUM_COLS;

            grid.add(createConfigBtn(configs.get(i)), col, row);
        }

        double width = (NUM_COLS - 1) * 10 + (NUM_COLS * 165);
        double center = (root.getWidth() - width) / 2;
        grid.setLayoutX(center);
        root.getChildren().add(grid);

    }

    private Button createConfigBtn(Puzzle config) {
        Button btn = new Button("");
        btn.setUserData(config);
        btn.setOnAction(this::setConfigBtnAction);
        URL url = KlotskiApplication.class.getResource(config.getPath());
        btn.setStyle("-fx-background-image: url(" + url + "); -fx-background-size: cover");
        Rectangle clip = new Rectangle(165, 165);
        clip.setArcHeight(25);
        clip.setArcWidth(25);
        btn.setClip(clip);
        btn.getStyleClass().add("config_btn");
        return btn;
    }

    private void setConfigBtnAction(ActionEvent event) {
        Puzzle config = (Puzzle) ((Button) event.getSource()).getUserData();

        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "GameView.fxml")
        );
        View.switchScene(event, loader);

        GameView view = loader.getController();
        GameController controller = new GameController(view, config);
        view.setController(controller);
    }

    @FXML
    void backToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "MenuView.fxml")
        );
        View.switchScene(event, loader);
    }
}
