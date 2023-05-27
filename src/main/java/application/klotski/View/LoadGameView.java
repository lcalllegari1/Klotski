package application.klotski.View;

import application.klotski.Controller.DatabaseConnector;
import application.klotski.Controller.GameController;
import application.klotski.Controller.LoadGameController;
import application.klotski.KlotskiApplication;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.event.ActionEvent;
import javafx.scene.text.TextAlignment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LoadGameView extends View {

    private LoadGameController loadGameController;

    @FXML
    private VBox panel;

    @FXML
    private void backToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource("FXML/MenuView.fxml"));
        try {
            switchScene(event, loader);
        } catch (IOException e) {
            throw new RuntimeException("Could not switch to the desired scene: " + e);
        }
    }

    public void setController(LoadGameController controller) {
        this.loadGameController = controller;
    }

    public void displayGames(HashMap<Integer, DatabaseConnector.Record> games) {
        for (Map.Entry<Integer, DatabaseConnector.Record> game : games.entrySet()) {
            panel.getChildren().add(createCard(game.getKey(), game.getValue()));
        }
    }

    private VBox createCard(int id, DatabaseConnector.Record game) {
        VBox card = createVBox(10);
        card.setStyle("-fx-background-color: #08415C; -fx-background-radius: 10; -fx-padding: 10 10 10 10");
        HBox cardBody = createCardBody(game.init_config_img(), game.curr_config_img(), game.name(), game.move_count());
        HBox cardFooter = createCardFooter(id);
        card.getChildren().addAll(cardBody, cardFooter);
        return card;
    }

    private HBox createCardBody(String initConfig, String currConfig, String date, int count) {
       HBox cardBody = createHBox(15);
       VBox cardInfo = createCardInfo(date, count);
       StackPane init_config = createImg(Objects.requireNonNull(KlotskiApplication.class.getResource("/application/klotski/assets/imgs/configurations/")).getFile() + initConfig);
       StackPane curr_config = createImg(Objects.requireNonNull(KlotskiApplication.class.getResource("/application/klotski/data/saves/imgs/")).getFile() + currConfig);
       cardBody.getChildren().addAll(init_config, curr_config, cardInfo);
       return cardBody;
    }

    private HBox createCardFooter(int id) {
        HBox cardFooter = createHBox(15);
        Region spacer = new Region();
        spacer.setMinWidth(150);
        Label init_config = createLabel("INITIAL\nCONFIGURATION");
        Label curr_config = createLabel("CURRENT\nCONFIGURATION");
        init_config.setAlignment(Pos.CENTER);
        curr_config.setAlignment(Pos.CENTER);
        Button btn = createPlayBtn(id);
        cardFooter.getChildren().addAll(init_config, curr_config, spacer, btn);
        return cardFooter;
    }

    private VBox createCardInfo(String date, int count) {
        VBox cardInfo = createVBox(20);
        cardInfo.setPadding(new Insets(25, 0, 0, 0));
        Label date_lbl = createLabel("DATE: " + date);
        Label count_lbl = createLabel("MOVE COUNT: " + count);
        date_lbl.setAlignment(Pos.TOP_LEFT);
        count_lbl.setAlignment(Pos.TOP_LEFT);
        cardInfo.getChildren().addAll(date_lbl, count_lbl);
        return cardInfo;
    }

    private Button createPlayBtn(int id) {
        Button btn = new Button("PLAY");
        btn.setUserData(id);
        btn.setMinSize(150, 35);
        btn.getStyleClass().add("play_saved_game_btn");
        btn.setOnAction(this::setPlayBtnAction);
        btn.setFocusTraversable(false);
        return btn;
    }

    private VBox createVBox(int spacing) {
        VBox res = new VBox();
        res.setSpacing(spacing);
        return res;
    }

    private HBox createHBox(int spacing) {
        HBox res = new HBox();
        res.setSpacing(spacing);
        return res;
    }

    private StackPane createImg(String url) {
        ImageView img = new ImageView("file:///" + url);
        img.setFitWidth(125);
        img.setFitHeight(125);
        StackPane image = new StackPane(img);
        image.setStyle("-fx-border-color: white; -fx-border-radius: 10; -fx-padding: 5, 5, 5, 5");
        return image;
    }

    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setTextAlignment(TextAlignment.CENTER);
        lbl.setMinWidth(135);
        lbl.setStyle("-fx-text-fill: white; -fx-font-family: 'Nunito Black'; -fx-font-size: 14;");
        return lbl;
    }

    private void setPlayBtnAction(ActionEvent event) {
        int id = (int) ((Button) event.getSource()).getUserData();

        FXMLLoader loader = new FXMLLoader(KlotskiApplication.class.getResource("FXML/GameView.fxml"));
        try {
            switchScene(event, loader);
        } catch (IOException e) {
            throw new RuntimeException("Could not switch to the desired scene: " + e);
        }

        // get the view handler of the scene
        GameView view = loader.getController();
        // create a new controller to handle this view
        GameController controller = new GameController(view, id);
        // link the view to the controller to handle events
        view.setController(controller);
    }
}
