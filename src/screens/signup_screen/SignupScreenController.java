/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.signup_screen;

import components.XOButton;
import components.XOPasswordField;
import components.XOTextField;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class SignupScreenController implements Initializable {

    @FXML
    private VBox screenContainer;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        XOTextField userNameField = new XOTextField("Enter Your UserName", 400, 50);
        XOPasswordField passwordField = new XOPasswordField("Enter Your Password", 400, 50);
        XOPasswordField confirmPasswordField = new XOPasswordField("Repeat Your Password", 400, 50);
        compareInputsFields(passwordField, confirmPasswordField);

        XOButton registerBtn = new XOButton("Register",
                () -> handleRegisterButtonAction(),
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton loginBtn = new XOButton("Login",
                () -> handleLoginButtonAction(),
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        compareInputsFields(passwordField, confirmPasswordField);
        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, confirmPasswordField, registerBtn, loginBtn);
    }

// Create a binding to compare the text in PasswordField and confirmPasswordField
    private void compareInputsFields(PasswordField passwordField, PasswordField confirmPasswordField) {
        String confirmpasstext1 = confirmPasswordField.getText();
        String passtext2 = passwordField.getText();

        if (confirmpasstext1.equals(passtext2)) {
            System.out.println("Passwords are equal");
        } else {
            System.out.println("Passwords are not equal");
        }
    }

    private void handleLoginButtonAction() {
        System.out.println("Navigate to login screen");
        TicTacToeGame.changeRoot(AppConstants.loginPath);

    }

    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Register screen");
        ///if registeration done from server
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }
}
