/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.user_home_screen;

import components.CustomLabel;
import components.XOButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mahmoud
 */
public class UserHomeController implements Initializable {

    @FXML
    private VBox screenContainer;
    @FXML
    private HBox topLabelContainer;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        CustomLabel userName = new CustomLabel("User Name", AppConstants.userIconPath);
        CustomLabel points = new CustomLabel("Points", AppConstants.cupIconPath);

        XOButton playBtn = new XOButton("Play",
                () -> handlePlayButtonAction(),
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton historyBtn = new XOButton("History",
                () -> handleHistoryButtonAction(),
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton logOutBtn = new XOButton("Log out",
                () -> handleLogOutButtonAction(),
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        topLabelContainer.getChildren().addAll(userName, points);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(playBtn, historyBtn, logOutBtn);

    }

    private void handlePlayButtonAction() {

    }

    private void handleHistoryButtonAction() {

    }

    private void handleLogOutButtonAction() {
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

}
