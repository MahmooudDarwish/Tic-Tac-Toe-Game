/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.ai_mode_screen;

import components.XOButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class AiModeController implements Initializable {

    @FXML
    private VBox screenContainer;
    private XOButton easyBtn;
    private XOButton mediumBtn;
    private XOButton hardBtn;
    private XOButton backBtn;

    private static String aiMode;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        easyBtn = new XOButton("Easy Mode",
                this::handEasyButtonAction,
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        mediumBtn = new XOButton("Medium Mode",
                this::handleMediumButtonAction,
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        hardBtn = new XOButton("Hard Mode",
                this::handleHardButtonAction,
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        backBtn = new XOButton("Back",
                this::handleBackButtonAction,
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(easyBtn, mediumBtn, hardBtn, backBtn);
    }

    private void handEasyButtonAction() {
        System.out.println("Navigate to EasyAi Mode");
        aiMode = "easy";
        TicTacToeGame.changeRoot(AppConstants.gameBoardScreenPath);
    }

    private void handleMediumButtonAction() {
        System.out.println("Navigate to MediumAi Mode");
        aiMode = "medium";
        TicTacToeGame.changeRoot(AppConstants.gameBoardScreenPath);
    }

    private void handleHardButtonAction() {
        System.out.println("Navigate to HardAi Mode");
        aiMode = "hard";
        TicTacToeGame.changeRoot(AppConstants.gameBoardScreenPath);
    }

    public static String getAiMode() {
        return aiMode;
    }

    private void handleBackButtonAction() {
        System.out.println("Navigate to Home screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }

}
