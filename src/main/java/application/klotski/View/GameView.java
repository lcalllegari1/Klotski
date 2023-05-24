package application.klotski.View;

import application.klotski.Controller.GameController;
import application.klotski.KlotskiApplication;
import application.klotski.Model.Direction;
import application.klotski.Model.Piece;
import application.klotski.Model.Puzzle;
import application.klotski.Model.Type;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.io.IOException;

public class GameView extends View {
    // animations
    private final TranslateTransition xMoveDeniedAnimation = new TranslateTransition(Duration.millis(30));
    private final TranslateTransition yMoveDeniedAnimation = new TranslateTransition(Duration.millis(30));

    private static final int MIN_LENGTH = 95;
    private static final int DEFAULT_MULTIPLIER = 100;

    private GameController gameController;

    public void setController(GameController controller) {
        this.gameController = controller;
    }

    public GameView() {
        initializeAnimations();
    }

    // Event handling
    @FXML
    private GridPane grid;

    @FXML
    private Label move_counter_lbl;

    @FXML
    private Button redo_btn;

    @FXML
    private Button undo_btn;

    @FXML
    private void backToMenu(ActionEvent event) {
        // switch to PuzzleConfigurationMenuView scene
        FXMLLoader menuLoader = new FXMLLoader(KlotskiApplication.class.getResource(FXML_PATH + "MenuView.fxml"));
        try {
            switchScene(event, menuLoader);
        } catch (IOException e) {
            throw new RuntimeException("RuntimeException thrown in MenuView.switchScene(): " + e);
        }
    }

    @FXML
    private void nextMove(ActionEvent event) {
        // TODO
    }

    @FXML
    void resetPuzzle(ActionEvent event) {
        gameController.resetActionHandler();
        disableUndoBtn();
        disableRedoBtn();
    }

    @FXML
    private void saveGame(ActionEvent event) {

    }

    @FXML
    void undo(ActionEvent event) {
        enableRedoBtn();
        gameController.undoActionHandler();
    }

    @FXML
    void redo(ActionEvent event) {
        enableUndoBtn();
        gameController.redoActionHandler();
    }


    // view updates member functions

    public void displayConfig(Puzzle config) {
        for (Piece piece : config.getPieces()) {
            Rectangle rect = createRect(piece);
            displayRect(rect);
        }
    }

    private Rectangle createRect(Piece piece) {
        int width = convertToDisplayedDimension(piece.getWidth());
        int height = convertToDisplayedDimension(piece.getHeight());

        Rectangle rect = new Rectangle(width, height, piece.getType() == Type.MAIN ? Color.rgb(255, 111, 0) : Color.rgb(255, 222, 104));
        rect.setStroke(piece.getType() == Type.MAIN ? Color.rgb(255, 91, 15) : Color.rgb(255, 214, 155));
        rect.getStyleClass().add("game_piece");

        GridPane.setMargin(rect, new Insets(2.5, 2.5, 2.5, 2.5));
        GridPane.setHalignment(rect, HPos.LEFT);
        GridPane.setValignment(rect, VPos.TOP);
        GridPane.setConstraints(rect, piece.getLocation().getCol(), piece.getLocation().getRow());

        rect.setOnMousePressed(mouseEvent -> {gameController.mousePressedEvent(mouseEvent);});
        rect.setOnMouseReleased(mouseEvent -> {gameController.mouseReleasedEvent(mouseEvent);});

        return rect;
    }

    private int convertToDisplayedDimension(int length) {
        return MIN_LENGTH + (DEFAULT_MULTIPLIER * (length - 1));
    }

    private void displayRect(Rectangle rect) {
        grid.getChildren().add(rect);
    }

    public void enableUndoBtn() {
        undo_btn.setDisable(false);
    }

    public void disableUndoBtn() {
        undo_btn.setDisable(true);
    }

    public void enableRedoBtn() {
        redo_btn.setDisable(false);
    }

    public void disableRedoBtn() {
        redo_btn.setDisable(true);
    }

    public void updateMoveCounter(int count) {
        move_counter_lbl.setText(String.valueOf(count));
    }

    public void clearConfig() {
        grid.getChildren().clear();
    }

    public void update(Puzzle puzzle, int moveCount) {
        clearConfig();
        updateMoveCounter(moveCount);
        displayConfig(puzzle);
    }

    // Animation configuration

    private void initializeAnimations() {
        xMoveDeniedAnimation.setFromX(-3);
        xMoveDeniedAnimation.setToX(3);
        xMoveDeniedAnimation.setAutoReverse(true);
        xMoveDeniedAnimation.setCycleCount(3);
        xMoveDeniedAnimation.setOnFinished(event -> xMoveDeniedAnimation.getNode().setTranslateX(0));

        yMoveDeniedAnimation.setFromY(-3);
        yMoveDeniedAnimation.setToY(3);
        yMoveDeniedAnimation.setAutoReverse(true);
        yMoveDeniedAnimation.setCycleCount(3);
        yMoveDeniedAnimation.setOnFinished(event -> yMoveDeniedAnimation.getNode().setTranslateY(0));
    }

    public void displayMoveDeniedAnimation(Rectangle target, Direction direction) {
        if (direction == Direction.UP || direction == Direction.DOWN) {
            yMoveDeniedAnimation.setNode(target);
            yMoveDeniedAnimation.play();
        } else if (direction == Direction.LEFT || direction == Direction.RIGHT) {
            xMoveDeniedAnimation.setNode(target);
            xMoveDeniedAnimation.play();
        }
    }


}
