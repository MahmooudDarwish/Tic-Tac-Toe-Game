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
        player.setAction("logout");
        String json = JsonUtil.toJson(player);
        try {
            response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006, false); // Adjust server address and port as needed
            System.out.println(response.toString());

            if (!response.isDone()) {
                handlePopup("Try Again", AppConstants.warningIconPath, response.getMessage());
            } else {
                onlineLoginPlayerHolder.clearPlayer();
                TicTacToeGame.changeRoot(AppConstants.loginPath);
            }
        } catch (Exception e) {
            handlePopup("Server May Be Down", AppConstants.warningIconPath, "Connection Timed Out.\nPlease try again later.");
            e.printStackTrace();
        }
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        XOLabel popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitle, 150, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }
}
