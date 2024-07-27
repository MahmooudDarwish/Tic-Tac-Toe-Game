package models;

import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.util.Duration;
import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

public class ServerStatusChecker {

    public static void startPeriodicServerMessageUpdate(OnlineLoginPlayerHolder onlineLoginPlayerHolder) {
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            Response response = onlineLoginPlayerHolder.getServerMessage();
            if (response != null && "Server is Down".equals(response.getMessage())) {
                handleNetworkPopup();
                onlineLoginPlayerHolder.clearServerMessage();
                pause.stop();
            } else {
                pause.playFromStart();
            }
        });
        pause.play();
    }

    private static void handleNetworkPopup() {
        Platform.runLater(() -> {
            CustomPopup cp = new CustomPopup("Warning", 170, 600, false);
            XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.warningIconPath, "Server is Down", 250, 80, true);

            XOButton okButton = new XOButton("Ok",
                    () -> {
                        OnlineLoginPlayerHolder.getInstance().stopServerListener();
                        TicTacToeGame.changeRoot(AppConstants.loginPath);
                        cp.close();
                    },
                    AppConstants.xIconPath,
                    140,
                    40,
                    AppConstants.buttonClickedTonePath);

            cp.addContent(popupResponseMessageLabel);
            cp.addContent(okButton);
            cp.show();
        });
    }
}
