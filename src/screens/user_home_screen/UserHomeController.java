/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.user_home_screen;

import components.CustomLabel;
import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
import models.Response;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

/**
 * FXML Controller class
 *
 * @author Mahmoud
 */
public class UserHomeController implements Initializable {

    @FXML
    private VBox screenContainer;
    @FXML
    private HBox topLabelContainer;

    private OnlinePlayer player;
    private Response response;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        player = onlineLoginPlayerHolder.getPlayer();

        CustomLabel userName = new CustomLabel("User Name: " + player.getUserName(), AppConstants.userIconPath);
        CustomLabel points = new CustomLabel("Points: " + player.getPoints(), AppConstants.cupIconPath);

        XOButton playBtn = new XOButton("Play",
                () -> handlePlayButtonAction(),
                AppConstants.xIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        XOButton logOutBtn = new XOButton("Log out",
                () -> handleLogOutButtonAction(),
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        topLabelContainer.getChildren().addAll(userName, points);

        screenContainer.setSpacing(20);
        screenContainer.getChildren().addAll(playBtn, logOutBtn);

    }

    private void handlePlayButtonAction() {

    }

    private void handleLogOutButtonAction() {

        player.setAction("logout");
        String json = JsonUtil.toJson(player);
        System.out.println(json);
        try {
            // Send JSON to server and receive response
            response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006); // Adjust server address and port as needed
            System.out.println(response.toString());

            // Handle server response
            if (!response.isDone()) {
                handlePopup("Try Again", AppConstants.warningIconPath, response.getMessage());
            } else {
                //handlePopup("Welcome to the Ultimate Tic Tac Toe Challenge!", AppConstants.doneIconPath, response.getMessage());
                // Change application state
                TicTacToeGame.changeRoot(AppConstants.loginPath);
            }
        } catch (Exception e) {
            // Handle connection error
            handlePopup("Server May Be Down", AppConstants.warningIconPath, "Connection Timed Out.\nPlease try again later.");
            e.printStackTrace();
        }
    }

    private void handlePopup(String popupTitel, String iconePath, String message) {

        XOLabel popupResponseMessageLabel = new XOLabel(iconePath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitel, 130, 600, true);

        cp.addContent(popupResponseMessageLabel);

        cp.addCancelButton(
                "OK");
        cp.show();
    }

}
