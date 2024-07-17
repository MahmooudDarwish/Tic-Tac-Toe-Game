/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.game_mode_screen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.components.XOButton;
import utils.constants.AppConstants;

public class GameModeUiController implements Initializable {
    @FXML
    private VBox buttonContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         XOButton playComputerBtn = new XOButton("Play PC", () -> handlePlayPCButtonAction(), AppConstants.xIconPath );
        XOButton playFriendBtn = new XOButton("Play Friend", () -> handlePlayFriendButtonAction(), AppConstants.oIconPath);
         XOButton backBtn = new XOButton("Back", () -> handleBackButtonAction(), AppConstants.xIconPath );
         
        buttonContainer.getChildren().addAll(playComputerBtn, playFriendBtn,backBtn);
    }    
    private void handlePlayPCButtonAction() {
        System.out.println("Navigate to players bob up");
    }
    
    private void handlePlayFriendButtonAction() {
        System.out.println("Navigate to players bob up");
    }
    private void handleBackButtonAction() {
        System.out.println("back to connection mode screen");
    }
    
    
}
