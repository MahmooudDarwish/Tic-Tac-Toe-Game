package screens.signup_screen;

import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import components.XOPasswordField;
import components.XOTextField;
import handlingplayerrequests.PlayerRequestHandler;
import handlingplayerrequests.RequestStatus;
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
import java.util.regex.Pattern;
import models.Player;
import screens.login_screen.LoginScreenController;
import utils.jsonutil.JsonSender;

/**
 * FXML Controller class for the signup screen.
 */
public class SignupScreenController implements Initializable , RequestStatus {

    @FXML
    private VBox screenContainer;
    private XOTextField userNameField;
    private XOPasswordField passwordField;
    private XOPasswordField confirmPasswordField;
    private XOButton registerBtn;
    private XOButton loginBtn;
    private XOLabel passwordErrorLabel;
    private XOLabel confirmPasswordErrorLabel;
    private static final Logger LOGGER = Logger.getLogger(SignupScreenController.class.getName());
    private CustomPopup cp;
    private XOLabel popupResponseMessageLabel;
    private PlayerRequestHandler requestHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeUIComponents();
        requestHandler = new PlayerRequestHandler(this,null,null,null);
        updateRegisterButtonState();

        // Add listeners to update the button state when the fields change
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());
        confirmPasswordField.textProperty().addListener((observable, oldValue, newValue) -> updateRegisterButtonState());
    }

    private void initializeUIComponents() {
        userNameField = new XOTextField("Enter Your Username", 400, 50);
        passwordField = new XOPasswordField("Enter Your Password", 400, 50);
        confirmPasswordField = new XOPasswordField("Repeat Your Password", 400, 50);

        passwordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 80, false);
        confirmPasswordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 10, false);

        registerBtn = new XOButton("Register", this::handleRegisterButtonAction, AppConstants.xIconPath, 200, 40, AppConstants.buttonClickedTonePath);
        loginBtn = new XOButton("Login", this::handleLoginButtonAction, AppConstants.oIconPath, 200, 40, AppConstants.buttonClickedTonePath);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, confirmPasswordField, passwordErrorLabel, confirmPasswordErrorLabel, registerBtn, loginBtn);
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
        return userNameField.getText().isEmpty() || passwordField.getText().isEmpty() || confirmPasswordField.getText().isEmpty();
    }

    private boolean isPasswordValid(String password) {
        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$";
        return Pattern.matches(passwordPattern, password);
    }

    private boolean isPasswordConfirmed(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }

    private void handleLoginButtonAction() {
        LOGGER.info("Navigating to login screen.");
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 60, true);
        cp = new CustomPopup(popupTitle, 150, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    private void handleRegisterButtonAction() {
        LOGGER.info("Register button clicked.");
        String userName = userNameField.getText();
        String password = passwordField.getText();
        requestHandler.register(userName, password);
  
        
    }

    @Override
    public void onSuccess(String msg) {
            TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

    @Override
    public void onFailure(String msg) {
            handlePopup("Failed", AppConstants.warningIconPath, msg);
    }
}
