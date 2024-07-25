/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.start_screen;

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
public class StartController implements Initializable {
    @FXML
    private VBox buttonContainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        // Create the custom buttons and add them to the VBox
        XOButton playBtn = new XOButton("Play",
                () -> handleplayButtonAction(),
                AppConstants.playNewGameIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton playerhistoryBtn = new XOButton("Player History",
                () -> handleplayerhistoryButtonAction(),
                AppConstants.playRecordsIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(playBtn, playerhistoryBtn);

    }

    private void handleplayButtonAction() {
        System.out.println("Navigate to Connection mode screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);

    }

    private void handleplayerhistoryButtonAction() {
        System.out.println("Navigate to Login");
        TicTacToeGame.changeRoot(AppConstants.playerhistoryPath);
        
    }
}


