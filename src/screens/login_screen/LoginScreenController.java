/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.login_screen;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import tictactoegame.components.XOButton;
import tictactoegame.components.XOPasswordField;
import tictactoegame.components.XOTextField;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox buttonContainer;
    @FXML
    private VBox textFiledContainer;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        XOTextField userNameField=new XOTextField ("Enter Your UserName");
        XOPasswordField passwordField=new XOPasswordField ("Enter Your Password");
        
        textFiledContainer.setSpacing(20);
        textFiledContainer.getChildren().addAll(userNameField, passwordField);
                
        
        // Create the custom buttons and add them to the VBox
        XOButton loginBtn = new XOButton("Login", () -> handleOfflineButtonAction(), AppConstants.xIconPath );
        XOButton vsFriendBtn = new XOButton("Register", () -> handleOnlineButtonAction(), AppConstants.oIconPath);
        
        buttonContainer.setSpacing(20);
        buttonContainer.getChildren().addAll(loginBtn, vsFriendBtn);
        
        
    }    
    
    private void handleOfflineButtonAction() {
        System.out.println("Navigate to Connection mode screen");
    }
    
    private void handleOnlineButtonAction() {
        System.out.println("Navigate to Login");
    }
}
