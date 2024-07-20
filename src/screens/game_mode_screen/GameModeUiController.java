package screens.game_mode_screen;

import components.CustomPopup;
import components.XOButton;
import components.XOTextField;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import utils.constants.AppConstants;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.OfflinePlayer;
import models.OfflinePlayerHolder;
import screens.playing_screen.PlayingScreenController;
import tictactoegame.TicTacToeGame;

public class GameModeUiController implements Initializable {

    @FXML
    private VBox buttonContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XOButton playComputerBtn = new XOButton("Play PC",
                this::handlePlayPCButtonAction,
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath
        );
        XOButton playFriendBtn = new XOButton("Play Friend",
                this::handlePlayFriendButtonAction,
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton backBtn = new XOButton("Back",
                this::handleBackButtonAction,
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(playComputerBtn, playFriendBtn, backBtn);
    }

    private void handlePlayPCButtonAction() {
        XOTextField xPlayer = new XOTextField("Player1 name", 300, 30);
        CustomPopup cp = new CustomPopup("Enter your name", 130, 350);

        cp.addContent(xPlayer);
        cp.addContent(new XOButton("Play",
                () -> {
                    OfflinePlayerHolder playerHolder = OfflinePlayerHolder.getInstance();
                    playerHolder.setXPlayer(new OfflinePlayer(xPlayer.getText()));
                    playerHolder.clearOPlayer();
                    cp.close();
                    navigateToGameScreen();
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath));
        cp.addCancelButton("Cancel");

        cp.show();
    }

    private void handlePlayFriendButtonAction() {
        XOTextField xPlayerName = new XOTextField("X Player name", 300, 30);
        XOTextField oPlayerName = new XOTextField("O Player name", 300, 30);

        CustomPopup cp = new CustomPopup("Enter your names", 200, 350);
        cp.addContent(xPlayerName);
        cp.addContent(oPlayerName);
        cp.addContent(new XOButton("Play",
                () -> {
                    OfflinePlayerHolder playerHolder = OfflinePlayerHolder.getInstance();
                    playerHolder.setXPlayer(new OfflinePlayer(xPlayerName.getText()));
                    playerHolder.setOPlayer(new OfflinePlayer(oPlayerName.getText()));
                    navigateToGameScreen();
                    cp.close();
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath));
        cp.addCancelButton("Cancel");
        cp.show();
    }

    private void handleBackButtonAction() {
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }

    private void navigateToGameScreen() {
        TicTacToeGame.changeRoot(AppConstants.playScreenPath);
    }
}
