package application.klotski.View;

import application.klotski.Controller.ConfigurationController;
import application.klotski.Controller.GameController;
import application.klotski.KlotskiApplication;
import application.klotski.Model.FilePuzzle;
import application.klotski.Model.Puzzle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;

public class PuzzleSelectionView extends View {

    private ConfigurationController configurationController;

    public void setController(ConfigurationController controller) {
        this.configurationController = controller;
    }

    @FXML
    private Pane puzzle_pane;

    public void displayConfigs(ArrayList<Puzzle> configs) {
        int count = configs.size();
        int numCols = 3;
        GridPane grid = new GridPane();
        grid.setVgap(10);
        grid.setHgap(10);

        for (int i = 0; i < count; i++) {
            Button btn = createConfigBtn(configs.get(i), i + 1);
            int col = i % numCols;
            int row = i / numCols;

            grid.add(btn, col, row);
        }
        double width = (numCols - 1) * 10 + (numCols * 145);
        double center = (puzzle_pane.getWidth() - width) / 2;
        grid.setLayoutX(center);
        puzzle_pane.getChildren().add(grid);
    }

    private Button createConfigBtn(Puzzle config, int index) {
        Button btn = new Button("");
        btn.setUserData(index);
        Image img = config instanceof FilePuzzle filePuzzle ? filePuzzle.getImage() :  new Image("file:///" + KlotskiApplication.class.getResource("assets/imgs/configurations/default.png"));
        btn.setStyle("-fx-background-image: url('" + ((FilePuzzle)config).getImage().getUrl() + "');-fx-background-size: cover");
        Rectangle clip = new Rectangle(145, 145);
        clip.setArcWidth(25);
        clip.setArcHeight(25);
        btn.setClip(clip);
        btn.setOnAction(this::setConfigBtnAction);
        btn.getStyleClass().add("config_btn");


        return btn;
    }

    private void setConfigBtnAction(ActionEvent event) {
        int id = (int)((Button) event.getSource()).getUserData();
        Puzzle puzzle = configurationController.getConfig(id);

        // switch to GameView scene
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_PATH + "GameView.fxml"));
        try {
            switchScene(event, loader);
        } catch (IOException e) {
            throw new RuntimeException("Could not switch to the desired scene: " + e);
        }

        // get the view handler of the scene
        GameView view = loader.getController();
        // create a new controller to handle this view
        GameController controller = new GameController(view, puzzle);
        // link the view to the controller to handle events
        view.setController(controller);
    }

}
