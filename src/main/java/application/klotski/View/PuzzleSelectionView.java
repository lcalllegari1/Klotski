package application.klotski.View;

import application.klotski.Controller.ConfigurationController;
import application.klotski.Controller.GameController;
import application.klotski.KlotskiApplication;
import application.klotski.Model.Puzzle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import javafx.event.ActionEvent;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ArrayList;

import static application.klotski.Model.Puzzle.IMG_EXTENSION;
import static application.klotski.Model.Puzzle.IMG_DIR_PATH;
import static application.klotski.View.View.FXML_DIR_PATH;

public class PuzzleSelectionView {

    @FXML
    private Pane puzzle_pane;

    private ConfigurationController configurationController;

    public void setController(ConfigurationController controller) {
        this.configurationController = controller;
    }

    public void displayConfigs(ArrayList<Puzzle> configs) {
        int numCols = 3;
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        for (int i = 0; i < configs.size(); i++) {
            Button btn = createConfigBtn(configs.get(i).getName(), i);
            int col = i % numCols;
            int row = i / numCols;

            grid.add(btn, col, row);
        }
        double width = (numCols - 1) * 10 + (numCols * 165);
        double center = (puzzle_pane.getWidth() - width) / 2;
        grid.setLayoutX(center);
        puzzle_pane.getChildren().add(grid);
    }

    private Button createConfigBtn(String name, int index) {
        Button btn = new Button("");
        btn.setUserData(index);
        btn.setOnAction(this::setConfigBtnAction);
        URL url = KlotskiApplication.class.getResource(IMG_DIR_PATH + name + IMG_EXTENSION);
        btn.setStyle("-fx-background-image: url('" + url + "'); -fx-background-size: cover");
        Rectangle clip = new Rectangle(165, 165);
        clip.setArcHeight(25);
        clip.setArcWidth(25);
        btn.setClip(clip);
        btn.getStyleClass().add("config_btn");
        return btn;
    }

    private void setConfigBtnAction(ActionEvent event) {
        int index = (int) ((Button) event.getSource()).getUserData();
        Puzzle config = configurationController.getConfig(index);

        // switch scene
        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "GameView.fxml")
        );
        View.switchScene(event, loader);

        // get the view handler of the scene
        GameView view = loader.getController();
        // create a new controller to handle this view
        GameController controller = new GameController(view, config);
        // link the view to the controller to handle events
        view.setController(controller);
    }

}
