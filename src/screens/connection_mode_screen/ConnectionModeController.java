/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.connection_mode_screen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import components.XOButton;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mahmoud
 */
public class ConnectionModeController implements Initializable {

    @FXML
    private VBox buttonContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Create the custom buttons and add them to the VBox
        XOButton offlineBtn = new XOButton("Offline",
                () -> handleOfflineButtonAction(),
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton onlineBtn = new XOButton("Online", () -> handleOnlineButtonAction(), AppConstants.oIconPath, 200, 40, AppConstants.buttonClickedTonePath);
        XOButton backBtn = new XOButton("Back",
                () -> handleBackButtonAction(),
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(offlineBtn, onlineBtn,backBtn);

    }

    private void handleOfflineButtonAction() {
        System.out.println("Navigate to Connection mode screen");
        TicTacToeGame.changeRoot(AppConstants.gameModePath);

    }

    private void handleOnlineButtonAction() {
        System.out.println("Navigate to Login");
        TicTacToeGame.changeRoot(AppConstants.enterServerIpScreen);
    }
    private void handleBackButtonAction() {
        System.out.println("Navigate to start Screen");
        TicTacToeGame.changeRoot(AppConstants.startScreenPath);
    }

}
