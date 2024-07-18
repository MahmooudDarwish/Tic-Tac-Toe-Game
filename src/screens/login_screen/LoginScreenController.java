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
import javafx.scene.layout.VBox;
import components.XOButton;
import components.XOPasswordField;
import components.XOTextField;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox screenContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        XOTextField userNameField = new XOTextField("Enter Your UserName", 400, 50);
        XOPasswordField passwordField = new XOPasswordField("Enter Your Password", 400, 50);
       
        XOButton loginBtn = new XOButton("Login", () -> handleLoginButtonAction(), AppConstants.xIconPath, 200, 40);
        XOButton registerBtn = new XOButton("Register", () -> handleRegisterButtonAction(), AppConstants.oIconPath, 200, 40);
        XOButton backBtn = new XOButton("Back", () -> handleRegisterButtonAction(), AppConstants.backIconPath, 200, 40);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, loginBtn, registerBtn,backBtn);
        screenContainer.getChildren().addAll(userNameField, passwordField, loginBtn, registerBtn);
    }

    private void handleLoginButtonAction() {
        System.out.println("Navigate to Home screen");
    }

    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Register screen");
    }
}
