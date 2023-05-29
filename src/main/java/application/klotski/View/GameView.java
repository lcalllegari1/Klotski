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
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import static application.klotski.View.View.FXML_DIR_PATH;

public class GameView {

    private GameController gameController;

    public void setController(GameController controller) {
        this.gameController = controller;
    }

    public GameView() {
        initializeAnimations();
    }

    @FXML
    private GridPane grid;

    @FXML
    private Label move_count_lbl;

    @FXML
    private Label win_lbl;

    @FXML
    private Button redo_btn;

    @FXML
    private Button undo_btn;

    @FXML
    private Button save_btn;

    @FXML
    private void backToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "MenuView.fxml")
        );
        View.switchScene(event, loader);
    }

    @FXML
    private void undo(ActionEvent event) {
        gameController.undoActionHandler();
    }

    @FXML
    private void redo(ActionEvent event) {
        gameController.redoActionHandler();
    }

    @FXML
    private void reset(ActionEvent event) {
        gameController.resetActionHandler();
    }

    @FXML
    private void save(ActionEvent event) {
        gameController.save();
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

    public void disableSaveBtn() {
        save_btn.setDisable(true);
    }

    public void enableSaveBtn() {
        save_btn.setDisable(false);
    }

    public void updateMoveCounter(int count) {
        move_count_lbl.setText(String.valueOf(count));
    }

    public void update(Puzzle config, int count) {
        clear();
        updateMoveCounter(count);
        display(config);
    }

    private void clear() {
        grid.getChildren().clear();
    }

    public void display(Puzzle config) {
        for (Piece piece : config.getPieces()) {
            grid.getChildren().add(createRect(piece));
        }
    }

    private Rectangle createRect(Piece piece) {
        int spacing = 5;
        int size = 100;
        Rectangle rect = new Rectangle(
                piece.getWidth() * size + ((piece.getWidth() - 1) * spacing),
                piece.getHeight() * size + ((piece.getHeight() - 1) * spacing)
        );
        rect.getStyleClass().add(
                piece.getType() == Type.MAIN ? "main_piece" : "piece"
        );

        GridPane.setHalignment(rect, HPos.LEFT);
        GridPane.setValignment(rect, VPos.TOP);
        GridPane.setConstraints(
                rect,
                piece.getLocation().getCol(),
                piece.getLocation().getRow()
        );

        rect.setOnMousePressed(
                mouseEvent -> gameController.mousePressedEvent(mouseEvent)
        );
        rect.setOnMouseReleased(
                mouseEvent -> gameController.mouseReleasedEvent(mouseEvent)
        );

        return rect;
    }

    public void displayWinMessage() {
        win_lbl.setVisible(true);
    }

    public void hideWinMessage() {
        win_lbl.setVisible(false);
    }

    // animations
    private final TranslateTransition xMoveDeniedAnimation = new TranslateTransition(Duration.millis(30));
    private final TranslateTransition yMoveDeniedAnimation = new TranslateTransition(Duration.millis(30));

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
