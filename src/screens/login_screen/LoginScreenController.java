package screens.login_screen;

import components.CustomPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import components.XOButton;
import components.XOLabel;
import components.XOPasswordField;
import components.XOTextField;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import models.Response;

/**
 * FXML Controller class for the login screen.
 */
public class LoginScreenController implements Initializable {

    @FXML
    private VBox screenContainer;
    private XOTextField userNameField;
    private XOPasswordField passwordField;
    private XOButton registerBtn;
    private XOButton loginBtn;
    private XOButton backBtn;
    private CustomPopup cp;
    private XOLabel popupResponseMessageLabel;
    private XOLabel passwordErrorLabel;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    private void handlePopup(String popupTitle, String iconPath, String message) {
        popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        cp = new CustomPopup(popupTitle, 160, 600, true);
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
        OnlinePlayer player = new OnlinePlayer();
        player.setUserName(userNameField.getText());
        player.setPassword(passwordField.getText());
        player.setAction("login");

        // Convert player object to JSON
        String json = JsonUtil.toJson(player);
        System.out.println("Sending JSON: " + json);

        // Send JSON and receive response
        Response response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006);
        if (response != null) {
            System.out.println("Received response: " + response);
            if (response.isDone()) {
                OnlinePlayer onlinePlayer = response.getPlayer();
                OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
                onlineLoginPlayerHolder.setPlayer(onlinePlayer);
                System.out.println("Go to user home path ");
                TicTacToeGame.changeRoot(AppConstants.userHomePath);
            } else {
                handlePopup("Login Failed", AppConstants.warningIconPath, "Login failed: " + response.getMessage());
            }
        } else {
            handlePopup("Login Error", AppConstants.warningIconPath, "Failed to connect to server.");
        }
    }

    private void handleRegisterButtonAction() {
        System.out.println("Navigate to Signup screen");
        TicTacToeGame.changeRoot(AppConstants.signupModePath); // Switch to sign up screen
    }

    private void handleBackButtonAction() {
        System.out.println("Navigate to Main Menu screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath); // Switch to main menu screen
    }
}
