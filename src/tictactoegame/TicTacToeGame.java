package tictactoegame;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class TicTacToeGame extends Application {

    private static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting application...");

        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource(AppConstants.startScreenPath));

        Scene scene = new Scene(root);
        primaryStage.setMinWidth(1366);
        primaryStage.setMinHeight(766);

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(this::handleWindowClose);

        primaryStage.show();
    }

    private void handleLogOut() {
        OnlinePlayer player = OnlineLoginPlayerHolder.getInstance().getPlayer();
        if (player != null) {
            Thread logoutThread = new Thread(() -> {
                try {
                    player.setAction("logout");
                    String json = JsonUtil.toJson(player);
                    JsonSender.sendJsonAndReceiveResponse(json);
                } catch (IOException ex) {
                    Logger.getLogger(TicTacToeGame.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            logoutThread.start();

        }
        System.exit(0);

    }

    private void handleWindowClose(WindowEvent event) {
        handleLogOut();
    }

    public static void changeRoot(String fxmlFile) {
        try {
            Parent newRoot = FXMLLoader.load(TicTacToeGame.class.getResource(fxmlFile));
            primaryStage.getScene().setRoot(newRoot);
        } catch (IOException ex) {
            System.out.println("Error loading FXML: " + ex.getMessage());
            Logger.getLogger(TicTacToeGame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
