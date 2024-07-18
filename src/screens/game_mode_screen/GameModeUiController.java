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
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Pair;
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
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dialog");
        dialog.setHeaderText("Please enter your names");

         
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField player1 = new TextField();
        player1.setPromptText("player 1");
        

        grid.add(new Label("player 1:"), 0, 0);
        grid.add(player1, 1, 0);
       

        dialog.getDialogPane().setContent(grid);
        dialog.show();
    }
    
    private void handlePlayFriendButtonAction() {
        
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Dialog");
        dialog.setHeaderText("Please enter your names");

         
        ButtonType loginButtonType = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        // Create the username and password labels and fields
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField player1 = new TextField();
        player1.setPromptText("player 1");
        TextField palyer2 = new TextField();
        palyer2.setPromptText("player 2");

        grid.add(new Label("player 1:"), 0, 0);
        grid.add(player1, 1, 0);
        grid.add(new Label("player 2"), 0, 1);
        grid.add(palyer2, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.show();
    }
    private void handleBackButtonAction() {
        System.out.println("back to connection mode screen");
    }
    
    
}
