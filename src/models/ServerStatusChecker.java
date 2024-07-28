package models;

import javafx.animation.PauseTransition;
import screens.lobby_screen_mode.LobbyScreenUiController;

import javafx.application.Platform;

import javafx.util.Duration;

import components.CustomPopup;

import components.XOButton;
import components.XOLabel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.SequentialTransition;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

import utils.jsonutil.JsonSender;

import utils.jsonutil.JsonUtil;

public class ServerStatusChecker {

    private static PauseTransition pause;

    public static void startPeriodicServerMessageUpdate(OnlineLoginPlayerHolder onlineLoginPlayerHolder) {

        if (pause != null) {
            pause.stop();
        }

        pause = new PauseTransition(Duration.seconds(3));

        pause.setOnFinished(event -> {
            Response response = onlineLoginPlayerHolder.getServerMessage();
            if (response != null) {
                System.out.println(response.getMessage());

            }
            System.out.println("Checking server status...");
            if (response != null && "Server is Down".equals(response.getMessage())) {
                handleNetworkPopup();
                onlineLoginPlayerHolder.clearServerMessage();
                pause.stop();
            } else if (response != null && "Logout successful".equals(response.getMessage())) {
                onlineLoginPlayerHolder.clearServerMessage();
                onlineLoginPlayerHolder.clearPlayer();
                onlineLoginPlayerHolder.stopServerListener();
                TicTacToeGame.changeRoot(AppConstants.loginPath);
                pause.stop();
            } else if (response != null && response.getMessage().contains("wants to play with you")) {
                handleWantToPlayPopup();
                pause.stop();
            } else if (response != null && response.getMessage().contains("move")) {
                System.out.println("move heree ========================================");
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

                        TicTacToeGame.changeRoot(AppConstants.loginPath);

                        OnlineLoginPlayerHolder.getInstance().stopServerListener();

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

    private static void handleWantToPlayPopup() {

        String message = OnlineLoginPlayerHolder.getInstance().getServerMessage().getMessage();
        OnlineLoginPlayerHolder.getInstance().clearServerMessage();
        OnlinePlayer player = OnlineLoginPlayerHolder.getInstance().getPlayer();

        String playerName = message.split(" wants to play with you")[0];

        OnlineLoginPlayerHolder.getInstance().clearServerMessage();

        // Create and play animation sequence
        SequentialTransition sequentialTransition = new SequentialTransition();
        PauseTransition pause = new PauseTransition(Duration.seconds(5));
        XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.warningIconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup("Want to play", 170, 600, true);
        cp.addContent(popupResponseMessageLabel);
        XOButton yesButton = new XOButton("Yes",
                () -> {
                    try {
                        // LobbyScreenUiController.fetchPlayersThread.stop();
                        System.out.println("Yes Clicked");
                        player.setMessage(playerName);
                        player.setAction("yes");
                        String json = JsonUtil.toJson(player);
                        JsonSender.sendJsonAndReceiveResponse(json);
                        OnlinePlayerHolder onlinePlayerHolder = OnlinePlayerHolder.getInstance();
                        OnlinePlayer onlinePlyerSender = new OnlinePlayer();
                        onlinePlyerSender.setUserName(playerName);
                        onlinePlayerHolder.setXPlayer(onlinePlyerSender);
                        onlinePlayerHolder.setOPlayer(player);
                        System.out.println("response.............player sender name" + playerName);
                        TicTacToeGame.changeRoot(AppConstants.onlinegameBoardScreenPath);
                        cp.close();
                        sequentialTransition.stop(); // Stop the sequence if Yes is clicked
                    } catch (IOException ex) {
                        Logger.getLogger(ServerStatusChecker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                },
                AppConstants.xIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);
        XOButton noButton = new XOButton("No",
                () -> {
                    try {
                        player.setMessage(playerName);
                        player.setAction("no");
                        String json = JsonUtil.toJson(player);
                        JsonSender.sendJsonAndReceiveResponse(json);
                        cp.close();
                        sequentialTransition.stop(); // Stop the sequence if No is clicked
                    } catch (IOException ex) {
                        Logger.getLogger(ServerStatusChecker.class.getName()).log(Level.SEVERE, null, ex);
                    }
                },
                AppConstants.xIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);
        cp.addContent(yesButton);
        cp.addContent(noButton);
        cp.show();
        pause.setOnFinished(event -> {
            try {
                player.setMessage(playerName);
                player.setAction("no");
                String json = JsonUtil.toJson(player);
                JsonSender.sendJsonAndReceiveResponse(json);
                cp.close();
            } catch (IOException ex) {
                Logger.getLogger(ServerStatusChecker.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        sequentialTransition.getChildren().add(pause);
        sequentialTransition.setOnFinished(event -> System.out.println("Want to play"));
        sequentialTransition.play();

    }

}
