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
import tictactoegame.TicTacToeGame;
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

        XOButton loginBtn = new XOButton("Login", 
                () -> handleLoginButtonAction()
                , AppConstants.xIconPath,
                200,
                40, 
                AppConstants.buttonClickedTonePath);
        XOButton registerBtn = new XOButton("Register",
                () -> handleRegisterButtonAction(),
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

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, loginBtn, registerBtn, backBtn);
    }

    private void handleLoginButtonAction() {
        System.out.println("Navigate to Home screen");
    }

    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Register screen");
        TicTacToeGame.changeRoot(AppConstants.signupModePath);
    }

    private void handleBackButtonAction() {
        System.out.println("Navigate to Home screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }
}
