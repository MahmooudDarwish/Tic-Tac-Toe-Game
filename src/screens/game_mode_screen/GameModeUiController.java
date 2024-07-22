package screens.game_mode_screen;

import components.CustomPopup;
import components.XOButton;
import components.XOTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import utils.constants.AppConstants;
import java.net.URL;
import java.util.ResourceBundle;
import models.OfflinePlayer;
import models.OfflinePlayerHolder;
import tictactoegame.TicTacToeGame;

/**
 * FXML Controller class
 *
 * @author Mahmoud
 */
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
        CustomPopup cp = new CustomPopup("Enter your name", 130, 350, true);

        cp.addContent(xPlayer);
        cp.addContent(new XOButton("Play",
                () -> {
                    OfflinePlayerHolder playerHolder = OfflinePlayerHolder.getInstance();
                    playerHolder.setXPlayer(new OfflinePlayer(xPlayer.getText()));
                    playerHolder.clearOPlayer();
                    if (!xPlayer.getText().isEmpty()) {
                        cp.close();
                        navigateToAiModeScreen();
                    }

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

        CustomPopup cp = new CustomPopup("Enter your names", 200, 350, true);
        cp.addContent(xPlayerName);
        cp.addContent(oPlayerName);
        cp.addContent(new XOButton("Play",
                () -> {
                    OfflinePlayerHolder playerHolder = OfflinePlayerHolder.getInstance();
                    playerHolder.setXPlayer(new OfflinePlayer(xPlayerName.getText()));
                    playerHolder.setOPlayer(new OfflinePlayer(oPlayerName.getText()));

                    if (!xPlayerName.getText().isEmpty() && !oPlayerName.getText().isEmpty()) {
                        cp.close();
                        navigateToGameScreen();
                    }

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
        TicTacToeGame.changeRoot(AppConstants.gameBoardScreenPath);
    }

    private void navigateToAiModeScreen() {
        TicTacToeGame.changeRoot(AppConstants.aiModeScreenPath);
    }
}
