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
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
import components.CustomPopup;
import components.XOButton;
import utils.constants.AppConstants;

public class GameModeUiController implements Initializable {
    @FXML
    private VBox buttonContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        XOButton playComputerBtn = new XOButton("Play PC", () -> handlePlayPCButtonAction(), AppConstants.xIconPath,200,40 );
        XOButton playFriendBtn = new XOButton("Play Friend", () -> handlePlayFriendButtonAction(), AppConstants.oIconPath,200,40);
        XOButton backBtn = new XOButton("Back", () -> handleBackButtonAction(), AppConstants.backIconPath ,200,40);
        
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(playComputerBtn, playFriendBtn,backBtn);

    }    
    private void handlePlayPCButtonAction() {
        ArrayList<Object> content=new ArrayList();
        TextField player1=new TextField();
         player1.setPromptText("player1 name");
        content.add(player1);
        content.add(new XOButton("Play ", () -> {}, AppConstants.xIconPath,200,40));
        content.add(new XOButton("Cancel", () -> {}, AppConstants.xIconPath,200,40));
        CustomPopup cp=new CustomPopup(content,"enter your name");
        cp.show();
    }
    
    private void handlePlayFriendButtonAction() {
         ArrayList<Object> content=new ArrayList();
         TextField player1=new TextField();
         player1.setPromptText("player1 name");
         TextField player2=new TextField();
         player2.setPromptText("player2 name");
        content.add(player1);
        content.add(player2);
        content.add(new XOButton("Play ", () -> {}, AppConstants.xIconPath,200 ,40));
        content.add(new XOButton("Cancel", () -> {}, AppConstants.oIconPath,200,40));
        CustomPopup cp=new CustomPopup(content,"enter your names"); 
       cp.show();
        
       
    }
    private void handleBackButtonAction() {
       
    }

   
    
    
}
