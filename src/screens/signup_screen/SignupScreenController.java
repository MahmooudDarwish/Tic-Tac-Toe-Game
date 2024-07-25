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
import enumpackages.statusenum.EnumStatus.Status;
import java.net.URL;
import java.util.ResourceBundle;
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
        screenContainer.getChildren().addAll(userNameField, passwordField, confirmPasswordField, passwordErrorLabel,confirmPasswordErrorLabel, registerBtn, loginBtn);

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
    
    private void handlePopup(String popupTitel,String iconePath,String message) {
        popupResponseMessageLabel = new XOLabel(iconePath, message, 250, 80, true);
        cp = new CustomPopup(popupTitel, 130, 600,true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }
    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Register screen");

        player.setUserName(userNameField.getText());
        player.setPassword(passwordField.getText());
        player.setPoints(0);
        player.setStatus(Status.OFFLINE);
        player.setAction("register");
        String json = JsonUtil.toJson(player);
        System.out.println(json);
        try {
            // Send JSON to server and receive response
            response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006); // Adjust server address and port as needed
            System.out.println(response.toString());

            // Handle server response
            if (!response.isSuccess())
            {
                handlePopup("Try Again",AppConstants.warningIconPath,response.getMessage());
            }
            else
            {
                handlePopup("Welcome to the Ultimate Tic Tac Toe Challenge!",AppConstants.doneIconPath,response.getMessage());
                // Change application state
                   TicTacToeGame.changeRoot(AppConstants.loginPath);
            }
        } catch (Exception e) {
            // Handle connection error
            handlePopup("Server May Be Down",AppConstants.warningIconPath,"Connection Timed Out.\nPlease try again later.");
            e.printStackTrace();
        }

    }
}