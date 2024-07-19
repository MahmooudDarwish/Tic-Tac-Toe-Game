/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.game_mode_screen;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import components.CustomPopup;
import components.XOButton;
import components.XOTextField;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

public class GameModeUiController implements Initializable {

    @FXML
    private VBox buttonContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XOButton playComputerBtn = new XOButton("Play PC",
                () -> handlePlayPCButtonAction(),
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath
        );
        XOButton playFriendBtn = new XOButton("Play Friend",
                () -> handlePlayFriendButtonAction(),
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton backBtn = new XOButton("Back",
                () -> handleBackButtonAction(),
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(playComputerBtn, playFriendBtn, backBtn);

    }

    private void handlePlayPCButtonAction() {
        ArrayList<Object> content = new ArrayList();
        XOTextField player1 = new XOTextField("Player1 name", 300, 30);
        content.add(player1);
        content.add(new XOButton("Play ",
                () -> {
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath));
        CustomPopup cp = new CustomPopup(content, "Enter your name", 130, 350);
        cp.show();
    }

    private void handlePlayFriendButtonAction() {
        ArrayList<Object> content = new ArrayList();
        XOTextField player1 = new XOTextField("Player1 name", 300, 30);
        XOTextField player2 = new XOTextField("Player2 name", 300, 30);
        content.add(player1);
        content.add(player2);
        content.add(new XOButton("Play ",
                () -> {
                },
                AppConstants.oIconPath,
                140,
                40,
                 AppConstants.buttonClickedTonePath));
        CustomPopup cp = new CustomPopup(content, "Enter your names", 200, 350);
        cp.show();
    }

    private void handleBackButtonAction() {
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }

}
