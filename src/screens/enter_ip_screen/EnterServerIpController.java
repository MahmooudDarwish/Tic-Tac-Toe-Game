/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.enter_ip_screen;

import components.XOButton;
import components.XOLabel;
import components.XOTextField;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

public class EnterServerIpController implements Initializable {

    @FXML
    private VBox screenContainer;
    private XOTextField serverIp;
    private XOButton loginBtn;
    private XOButton backBtn;
// Define the IP address pattern (IPv4 and IPv6)
    private static final Pattern IP_ADDRESS_PATTERN = Pattern.compile(
            "^(([0-9]{1,3}\\.){3}[0-9]{1,3})$|^((?:[a-fA-F0-9]{1,4}:){7}[a-fA-F0-9]{1,4})$");
    private XOLabel ipErrorLabel;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        serverIp = new XOTextField("Enter Server IP", 400, 50);
        ipErrorLabel = new XOLabel(AppConstants.warningIconPath, "", 500, 80, false);

        loginBtn = new XOButton("Login",
                this::handleloginBtnButtonAction,
                AppConstants.xIconPath,
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
        serverIp.textProperty().addListener((observable, oldValue, newValue) -> validateInput());

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(serverIp,ipErrorLabel, loginBtn, backBtn);

    }

    private void handleloginBtnButtonAction() {
        AppConstants.setServerIp(serverIp.getText());
        System.out.println("Server IP" + AppConstants.getServerIp());
        System.out.println("Navigate to Login screen");
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

    /**
     * Handle the back button action
     */
    private void handleBackButtonAction() {
        System.out.println("Navigate to Home screen");
        TicTacToeGame.changeRoot(AppConstants.connectionModePath);
    }

    private void validateInput() {
        String serverIAdress = serverIp.getText();
        boolean isServerIpValid = IP_ADDRESS_PATTERN.matcher(serverIAdress).matches();
        ipErrorLabel.setLabelVisible(!isServerIpValid);
        if (!isServerIpValid) {
            ipErrorLabel.setText(
                    "Enter Valid IP For Server"
            );
        }
        loginBtn.setDisable(serverIAdress.isEmpty() || !isServerIpValid);
    }
}
