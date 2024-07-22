/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.login_screen;

import components.CustomPopup;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import components.XOButton;
import components.XOLabel;
import components.XOPasswordField;
import components.XOTextField;
import models.OnlinePlayer;
import models.Response;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox screenContainer;
    private OnlinePlayer player;
    private XOTextField userNameField;
    private XOPasswordField passwordField;
    private XOButton registerBtn;
    private XOButton loginBtn;
    private XOButton backBtn;
    private Response response;
    private CustomPopup cp;
    private XOLabel popupResponseMessageLabel;
    private XOLabel passwordErrorLabel;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        player = new OnlinePlayer();
        userNameField = new XOTextField("Enter Your UserName", 400, 50);
        passwordField = new XOPasswordField("Enter Your Password", 400, 50);
        passwordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 80, false);

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
        loginBtn.setDisable(true); // Initially disable the login button

        backBtn = new XOButton("Back",
                this::handleBackButtonAction,
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        // Add listeners to the text fields to enable/disable the login button
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, passwordErrorLabel, loginBtn, registerBtn, backBtn);
    }

    /**
     * Validate the input fields and enable/disable the login button
     */
    private void validateInput() {
        String userName = userNameField.getText();
        String password = passwordField.getText();
        boolean isPasswordValid = PASSWORD_PATTERN.matcher(password).matches();
        passwordErrorLabel.setLabelVisible(!isPasswordValid);
        if (!isPasswordValid) {
            passwordErrorLabel.setText(
                    "Password must be 8+ chars long and include:\n"
                    + "uppercase, lowercase, digit, and special char."
            );
        }
        loginBtn.setDisable(userName.isEmpty() || !isPasswordValid);
    }
    private void handlePopup(String popupTitel,String iconePath,String message) {
        popupResponseMessageLabel = new XOLabel(iconePath, message, 250, 80, true);
        cp = new CustomPopup(popupTitel, 130, 600,true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    /**
     * Handle the login button action
     */
    private void handleLoginButtonAction() {
        System.out.println("Navigate to Home screen");

        // Set player credentials
        player.setUserName(userNameField.getText());
        player.setPassword(passwordField.getText());
        player.setAction("login");

        // Convert player object to JSON
        String json = JsonUtil.toJson(player);
        System.out.println("JSON that client will send to server: " + json);

        try {
            // Send JSON to server and receive response
            response = JsonSender.sendJsonAndReceiveResponse(json,AppConstants.getServerIp(), 5006); // Adjust server address and port as needed
            System.out.println(response.toString());

            // Handle server response
            if (!response.isSuccess())
            {
                handlePopup("Try Again",AppConstants.warningIconPath,response.getMessage());
            }
            else
            {
                // Change application state
                TicTacToeGame.changeRoot(AppConstants.connectionModePath);
            }
                    
        } catch (Exception e) {
            // Handle connection error
            handlePopup("Server May Be Down",AppConstants.warningIconPath,"Connection Timed Out.\nPlease try again later.");
            e.printStackTrace();
        }
    }

    /**
     * Handle the register button action
     */
    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Register screen");
        TicTacToeGame.changeRoot(AppConstants.signupModePath);
    }

    /**
     * Handle the back button action
     */
    private void handleBackButtonAction() {
        System.out.println("Navigate to Home screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }
}
