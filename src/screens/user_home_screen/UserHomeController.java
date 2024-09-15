package screens.user_home_screen;

import components.CustomLabel;
import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import handlingplayerrequests.PlayerRequestHandler;
import handlingplayerrequests.RequestStatus;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.OnlineLoginPlayerHolder;
import models.Player;
import screens.signup_screen.SignupScreenController;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

public class UserHomeController implements Initializable, RequestStatus {

    private PlayerRequestHandler requestHandler;
    private static final Logger LOGGER = Logger.getLogger(SignupScreenController.class.getName());

    @FXML
    private VBox screenContainer;
    @FXML
    private HBox topLabelContainer;

    private Player player;
    //private Response response;
    private OnlineLoginPlayerHolder onlineLoginPlayerHolder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        player = onlineLoginPlayerHolder.getPlayer();
        requestHandler = new PlayerRequestHandler(this,null,null,null);

        //onlineLoginPlayerHolder.startServerListener();
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

        //ServerStatusChecker.startPeriodicServerMessageUpdate(onlineLoginPlayerHolder);
    }

    private void handlePlayButtonAction() {
        TicTacToeGame.changeRoot(AppConstants.gameLoppyPath);
    }

    private void showMessage(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void handleLogOutButtonAction() {
        LOGGER.info("LogOut button clicked.");

        Text areUSure = new Text("Are you Sure?");
        areUSure.setFont(Font.font("", FontWeight.BOLD, 24));
        areUSure.setFill(Color.GREY);
        areUSure.setTextAlignment(TextAlignment.CENTER);

        CustomPopup cp = new CustomPopup("Resign", 130, 350, true);
        cp.addContent(areUSure);

        // Create the "Yes" button with the logout action
        XOButton yesButton = new XOButton("Yes",
                () -> {
                    // Perform the logout operation
                    requestHandler.logout();
                    // Close the popup after logout (optional)
                    cp.close();
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath
        );

        cp.addContent(yesButton);
        cp.addCancelButton("No");
        // Show the popup
        cp.show();
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        XOLabel popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitle, 150, 600, true);
        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    @Override
    public void onSuccess(String msg) {
        TicTacToeGame.changeRoot(AppConstants.loginPath);
    }

    @Override
    public void onFailure(String msg) {
        handlePopup("Warning", AppConstants.warningIconPath, msg);

    }
}
