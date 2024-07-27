/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.signup_screen;

import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import components.XOPasswordField;
import components.XOTextField;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

import utils.jsonutil.JsonUtil;
import java.util.regex.Pattern;
import models.OnlinePlayer;
import models.Response;
import utils.jsonutil.JsonSender;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class SignupScreenController implements Initializable {

    @FXML
    private VBox screenContainer;
    private OnlinePlayer player;
    private XOTextField userNameField;
    private XOPasswordField passwordField;
    private XOPasswordField confirmPasswordField;
    private XOButton registerBtn;
    private XOButton loginBtn;
    private XOLabel passwordErrorLabel;
    private XOLabel confirmPasswordErrorLabel;

    private Response response;
    private CustomPopup cp;
    private XOLabel popupResponseMessageLabel;

    /**
     * Initializes the controller class.
     *
     * @Mohammed
     * @
     */
    @Override

    public void initialize(URL url, ResourceBundle rb) {
        player = new OnlinePlayer();
        // TODO
        userNameField = new XOTextField("Enter Your Username", 400, 50);
        passwordField = new XOPasswordField("Enter Your Password", 400, 50);
        confirmPasswordField = new XOPasswordField("Repeat Your Password", 400, 50);

        passwordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 80, false);
        confirmPasswordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 10, false);

        registerBtn = new XOButton("Register",
                this::handleRegisterButtonAction,
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        loginBtn = new XOButton("Login",
                this::handleLoginButtonAction,
                AppConstants.oIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, confirmPasswordField, passwordErrorLabel, confirmPasswordErrorLabel, registerBtn, loginBtn);

        updateRegisterButtonState();

        // Add listeners to update the button state when the fields change
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());

    }

    private void updateRegisterButtonState() {
        boolean areFieldsEmpty = areFieldsEmpty(userNameField, passwordField, confirmPasswordField);
        registerBtn.setDisable(areFieldsEmpty);

        // Reset error labels and their visibility
        passwordErrorLabel.setLabelVisible(false);
        confirmPasswordErrorLabel.setLabelVisible(false);

        if (!areFieldsEmpty) {
            boolean isPasswordValid = isPasswordValid(passwordField.getText());
            boolean isPasswordConfirmed = isPasswordConfirmed(passwordField.getText(), confirmPasswordField.getText());

            if (!isPasswordValid) {
                passwordErrorLabel.setLabelVisible(true);
                passwordErrorLabel.setText(
                        "Password must be 8+ chars long and include:\n"
                        + "uppercase, lowercase, digit, and special char."
                );
            }

            if (!isPasswordConfirmed) {
                confirmPasswordErrorLabel.setLabelVisible(true);
                confirmPasswordErrorLabel.setText("Passwords do not match.");
            }

            // Disable register button if password is invalid or passwords do not match
            registerBtn.setDisable(!isPasswordValid || !isPasswordConfirmed);
        }
    }

    private boolean areFieldsEmpty(XOTextField userNameField, XOPasswordField passwordField, XOPasswordField confirmPasswordField) {
        boolean isEmpty = false;
        if (userNameField.getText().isEmpty()) {
            isEmpty = true;
        }
        if (passwordField.getText().isEmpty()) {
            isEmpty = true;
        }
        if (confirmPasswordField.getText().isEmpty()) {
            isEmpty = true;
        }
        return isEmpty;
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private void handleLoginButtonAction() {
        System.out.println("Navigate to login screen");
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

    private void handlePopup(String popupTitel, String iconePath, String message) {
        popupResponseMessageLabel = new XOLabel(iconePath, message, 250, 60, true);
        cp = new CustomPopup(popupTitel, 150, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    private void handleRegisterButtonAction() {
        try {
            System.out.println("Navigate to Home screen");

            // Set player credentials
            player = new OnlinePlayer();
            player.setUserName(userNameField.getText());
            player.setPassword(passwordField.getText());
            player.setAction("register");

            // Convert player object to JSON
            String json = JsonUtil.toJson(player);
            System.out.println("Sending JSON: " + json);

            // Send JSON and receive response
            response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006, false);
            if (response != null) {
                System.out.println("Received response: " + response);
                if (response.isDone()) {
                    handlePopup("Registration Successful", AppConstants.doneIconPath, "Registration successful.");
                    TicTacToeGame.changeRoot(AppConstants.loginPath); // Switch to login screen
                } else {
                    handlePopup("Registration Failed", AppConstants.warningIconPath, "Registration failed: " + response.getMessage());
                }
            } else {
                handlePopup("Registration Error", AppConstants.warningIconPath, "Failed to connect to server.");
            }
        } catch (IOException ex) {
            Logger.getLogger(SignupScreenController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
