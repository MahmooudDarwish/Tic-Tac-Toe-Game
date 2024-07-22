package screens.game_board_screen;

import components.CustomPopup;
import components.XOButton;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.OfflinePlayer;
import models.OfflinePlayerHolder;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Omar
 */
public class GameBoardController implements Initializable {

    private boolean xTurn;

    private OfflinePlayer xPlayer;
    private OfflinePlayer oPlayer;
    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private StackPane xPlayerStack;
    @FXML
    private StackPane oPlayerStack;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OfflinePlayerHolder offlinePlayerHolder = OfflinePlayerHolder.getInstance();

        XOButton resignButton = new XOButton("Resign",
                () -> {
                },
                AppConstants.xIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton recordButton = new XOButton("Record",
                () -> {
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);

        xTurn = true;
        xPlayer = offlinePlayerHolder.getXPlayer();
        oPlayer = offlinePlayerHolder.getOPlayer();

        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(20));

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                ImageView x = new ImageView(AppConstants.xIconPath);
                ImageView o = new ImageView(AppConstants.oIconPath);
                x.setFitHeight(50);
                x.setFitWidth(50);
                o.setFitHeight(50);
                o.setFitWidth(50);
                Button borderButton = new Button();
                borderButton.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
                borderButton.setPrefSize(100, 100);
                borderButton.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
                borderButton.setContentDisplay(ContentDisplay.CENTER);
                gp.add(borderButton, j, i);

                borderButton.setOnAction(e -> {
                    if (xTurn) {
                        xTurn = false;
                        borderButton.setGraphic(x);

                    } else {
                        xTurn = true;
                        borderButton.setGraphic(o);
                    }
                    borderButton.setDisable(true);
                });
            }
        }
  
        Text xPlayerText = new Text(xPlayer.getName());
        xPlayerText.setFont(Font.font("", FontWeight.BOLD, 24));

        Text oPlayerText = new Text((oPlayer != null ? oPlayer.getName() : "AI"));
        oPlayerText.setFont(Font.font("", FontWeight.BOLD, 24));

        Text scoreText = new Text("2 — 3");
        scoreText.setFont(Font.font("", FontWeight.BOLD, 24));
        scoreText.setFill(Color.GREY);

        HBox buttonContainer = new HBox(20, resignButton, recordButton);
        buttonContainer.setAlignment(Pos.CENTER);

        VBox scoreBox = new VBox(20, buttonContainer, scoreText);
        scoreBox.setAlignment(Pos.CENTER);

        ImageView xPlayerImage = new ImageView(AppConstants.xIconPath);
        xPlayerImage.setOpacity(0.5);
        xPlayerImage.setFitHeight(200);
        xPlayerImage.setFitWidth(200);

        ImageView oPlayerImage = new ImageView(AppConstants.oIconPath);
        oPlayerImage.setOpacity(0.5);
        oPlayerImage.setFitHeight(200);
        oPlayerImage.setFitWidth(200);

        xPlayerStack.getChildren().addAll(xPlayerImage, xPlayerText);
        oPlayerStack.getChildren().addAll(oPlayerImage, oPlayerText);

        HBox mainLayout = new HBox(50, xPlayerStack, gp, oPlayerStack);
        mainLayout.setAlignment(Pos.CENTER);

        VBox rootLayout = new VBox(20, scoreBox, mainLayout);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPadding(new Insets(20));
        rootLayout.setPrefHeight(766);
        rootLayout.setPrefWidth(1366);

        AnchorPane.getChildren().add(rootLayout);
    }
    private void showVideoPopUp(String winnerName, String videoPath) {

        File videoFile = new File(videoPath);

        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Set the desired width and height for the MediaView
        mediaView.setFitWidth(450);
        mediaView.setFitHeight(300);

        String popUpTitle = winnerName + " Wins";
        Text winnerText = new Text(popUpTitle);

        winnerText.setFont(Font.font("", FontWeight.BOLD, 24));
        winnerText.setFill(Color.GREY);
        winnerText.setTextAlignment(TextAlignment.CENTER);

        CustomPopup popup = new CustomPopup(popUpTitle, 400, 450, false);

        XOButton playAgainButton = new XOButton("Play Again",
                () -> {
                    popup.close();
                    mediaPlayer.dispose();
                    resetGame();
                },
                AppConstants.xIconPath,
                100,
                40,
                AppConstants.buttonClickedTonePath);

        XOButton exitButton = new XOButton("Exit",
                () -> {
                    popup.close();
                    mediaPlayer.dispose();
                    navigateGameModeScreen();
                },
                AppConstants.oIconPath,
                100,
                40,
                AppConstants.buttonClickedTonePath);

        popup.addContent(winnerText);
        popup.addContent(mediaView);
        popup.addContent(playAgainButton);
        popup.addContent(exitButton);

        popup.show();
        mediaPlayer.play();
    }
}
