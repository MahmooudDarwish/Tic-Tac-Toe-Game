package screens.user_home_screen;

import components.CustomLabel;
import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
import models.Response;
import tictactoegame.TicTacToeGame;
import models.ServerStatusChecker;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

public class UserHomeController implements Initializable {

    @FXML
    private VBox screenContainer;
    @FXML
    private HBox topLabelContainer;

    private OnlinePlayer player;
    private Response response;
    private OnlineLoginPlayerHolder onlineLoginPlayerHolder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        player = onlineLoginPlayerHolder.getPlayer();

        onlineLoginPlayerHolder.startServerListener();

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

        ServerStatusChecker.startPeriodicServerMessageUpdate(onlineLoginPlayerHolder);
    }

    private void handlePlayButtonAction() {
        TicTacToeGame.changeRoot(AppConstants.gameLoppyPath);
    }

    private void handleLogOutButtonAction() {
        Thread logoutThread = new Thread(() -> {
            player.setAction("logout");
            String json = JsonUtil.toJson(player);
            try {
                response = JsonSender.sendJsonAndReceiveResponse(json);

            } catch (IOException e) {
                handlePopup("Server May Be Down", AppConstants.warningIconPath, "Connection Timed Out.\nPlease try again later.");
            }
        });

        logoutThread.start();
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        XOLabel popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitle, 150, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }
}
