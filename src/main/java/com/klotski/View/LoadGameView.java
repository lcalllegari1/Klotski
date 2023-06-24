package com.klotski.View;

import com.klotski.Controller.DatabaseConnector;
import com.klotski.Controller.GameController;
import com.klotski.Controller.SaveController;
import com.klotski.KlotskiApplication;
import com.klotski.Model.Game;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;

import com.klotski.Controller.DatabaseConnector.Match;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import javax.imageio.ImageIO;

import static com.klotski.View.View.FXML_DIR_PATH;


public class LoadGameView {

    @FXML
    private VBox panel;

    @FXML
    void backToMenu(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "MenuView.fxml")
        );
        View.switchScene(event, loader);
    }

    public void display(ArrayList<Match> matches) {
        for (Match match : matches) {
            panel.getChildren().add(
                    createCard(match)
            );
        }
    }

    private VBox createCard(Match match) {
        VBox card = VBox(10);
        card.setStyle("-fx-background-color: #134EF2; -fx-background-radius: 10; -fx-padding: 10");
        HBox body = createBody(match);
        HBox footer = createFooter(match.match_id());
        card.getChildren().addAll(body, footer);
        return card;
    }

    private HBox createBody(Match match) {
        HBox body = HBox(15);
        VBox info = createInfo(match.date(), match.score());

        StackPane initial = createInitialImage("/com/klotski/assets/imgs/configurations/config_" + match.config_id() + ".png");
        StackPane current = createCurrentImage(new SaveController().getImgs().getPath() + "/" + match.img());
        body.getChildren().addAll(initial, current, info);
        return body;
    }

    private HBox createFooter(int id) {
        HBox footer = HBox(15);
        Region space = new Region();
        space.setMinWidth(160);
        Label initial = createLabel("INITIAL\nCONFIGURATION");
        Label current = createLabel("CURRENT\nCONFIGURATION");
        initial.setAlignment(Pos.CENTER);
        current.setAlignment(Pos.CENTER);
        Button play = createPlayBtn(id);
        Button delete = createDeleteBtn(id);
        footer.getChildren().addAll(initial, current, space, delete, play);
        return footer;
    }

    private Label createLabel(String text) {
        Label label = new Label(text);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setMinWidth(137);
        label.setMinHeight(50);
        label.setStyle("-fx-text-fill: white; -fx-font-family: 'Rasa'; -fx-font-weight: bold; -fx-font-size: 16; ");
        return label;
    }

    private StackPane createCurrentImage(String url) {
        ImageView img = new ImageView("file:///" + url);
        img.setFitWidth(125);
        img.setFitHeight(125);

        StackPane image = new StackPane(img);
        image.setStyle("-fx-border-color: white; -fx-border-radius: 10; -fx-padding: 5");
        return image;
    }

    private StackPane createInitialImage(String url) {
        InputStream stream = getClass().getResourceAsStream(url);
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(stream);
        } catch (IOException e) {
            System.out.println("Could not read the image: " + e.getMessage());
        }

        ImageView img = new ImageView(SwingFXUtils.toFXImage(bufferedImage, null));
        img.setFitWidth(125);
        img.setFitHeight(125);
        StackPane image = new StackPane(img);
        image.setStyle("-fx-border-color: white; -fx-border-radius: 10; -fx-padding: 5");
        return image;
    }

    private VBox createInfo(String date, int score) {
        VBox info = VBox(30);
        info.setPadding(new Insets(30, 0, 0, 0));
        Label dateLabel = new Label("DATE: " + date);
        Label scoreLabel = new Label("SCORE: " + score);
        dateLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Rasa'; -fx-font-weight: bold; -fx-font-size: 22; ");
        scoreLabel.setStyle("-fx-text-fill: white; -fx-font-family: 'Rasa'; -fx-font-weight: bold; -fx-font-size: 22; ");
        info.getChildren().addAll(dateLabel, scoreLabel);
        return info;
    }

    private Button createPlayBtn(int id) {
        Button btn = new Button("PLAY");
        btn.setUserData(id);
        btn.setMinSize(150, 40);
        btn.getStyleClass().add("play_saved_game_btn");
        btn.setOnAction(this::setPlayBtnAction);
        btn.setFocusTraversable(false);
        return btn;
    }

    private Button createDeleteBtn(int id) {
        Button btn = new Button("");
        btn.setUserData(id);
        btn.setMinSize(40, 40);
        btn.getStyleClass().add("play_saved_game_btn");
        btn.setOnAction(this::setDeleteBtnAction);
        btn.setFocusTraversable(false);
        URL url = KlotskiApplication.class.getResource("/com/klotski/assets/icons/bin.png");
        btn.setStyle("-fx-background-image: url('" + url + "'); -fx-background-size: cover");
        return btn;
    }

    private void setPlayBtnAction(ActionEvent event) {
        int id = (int) ((Button) event.getSource()).getUserData();
        FXMLLoader loader = new FXMLLoader(
                KlotskiApplication.class.getResource(FXML_DIR_PATH + "GameView.fxml")
        );
        View.switchScene(event, loader);

        GameView view = loader.getController();
        GameController controller = new GameController(view, id);
        view.setController(controller);
    }

    private void setDeleteBtnAction(ActionEvent event) {
        int id = (int) ((Button) event.getSource()).getUserData();
        SaveController saver = new SaveController();
        saver.delete(id);
        panel.getChildren().remove(
                (VBox) ((Button) event.getSource()).getParent().getParent()
        );
    }

    private VBox VBox(int spacing) {
        VBox res = new VBox();
        res.setSpacing(spacing);
        return res;
    }

    private HBox HBox(int spacing) {
        HBox res = new HBox();
        res.setSpacing(spacing);
        return res;
    }

}
