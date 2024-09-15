package screens.login_screen;

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
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;

/**
 * FXML Controller class for the login screen.
 */
public class LoginScreenController implements Initializable ,RequestStatus {

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
    private PlayerRequestHandler requestHandler;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    private static final Logger LOGGER = Logger.getLogger(LoginScreenController.class.getName());

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initializeComponents();
        configureComponents();
        setUpListeners();
    }

    private void initializeComponents() {
        userNameField = new XOTextField("Enter Your UserName", 400, 50);
        passwordField = new XOPasswordField("Enter Your Password", 400, 50);
        passwordErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 80, false);
        requestHandler = new PlayerRequestHandler(this,null,null,null);

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
    }

    private void configureComponents() {
        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(userNameField, passwordField, passwordErrorLabel, loginBtn, registerBtn, backBtn);
    }

    private void setUpListeners() {
        userNameField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> validateInput());
    }

    /**
     * Validate the input fields and enable/disable the login button.
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
        popupResponseMessageLabel = new XOLabel(iconPath, message, 500, 40, true);
        cp = new CustomPopup(popupTitle, 160, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    /**
     * Handle the login button action.
     */
    private void handleLoginButtonAction() {
        LOGGER.info("Login button clicked.");
        String userName = userNameField.getText();
        String password = passwordField.getText();
        requestHandler.login(userName, password);
        } 
    

    private void handleRegisterButtonAction() {
        LOGGER.info("Register button clicked. Navigating to Signup screen.");
        TicTacToeGame.changeRoot(AppConstants.signupModePath); // Switch to sign up screen
    }

    private void handleBackButtonAction() {
        LOGGER.info("Back button clicked. Navigating to Main Menu screen.");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath); // Switch to main menu screen
    }

    @Override
    public void onSuccess(String msg) {           
        TicTacToeGame.changeRoot(AppConstants.userHomePath);
    }

    @Override
    public void onFailure(String msg) {
        handlePopup("Warning", AppConstants.warningIconPath, msg); 
        
    }
}
