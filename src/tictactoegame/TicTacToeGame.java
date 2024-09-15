package tictactoegame;

import enumpackages.EnumPlayerAction.Action;
import handlingplayerrequests.PlayerRequestHandlerInterface;
import java.io.IOException;
import java.io.Serializable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.OnlineLoginPlayerHolder;
import models.Player;
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
import models.Request;
import models.Response;
import utils.jsonutil.ResponseCallback;

public class TicTacToeGame extends Application implements ResponseCallback{
    private ExecutorService executorService = Executors.newFixedThreadPool(1);

    private static Stage primaryStage;
    private JsonSender jsonSender;

    @Override
    public void start(Stage stage) throws Exception {
        System.out.println("Starting application...");
        jsonSender=JsonSender.getInstance(this);
        
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource(AppConstants.enterServerIpScreen));

        Scene scene = new Scene(root);
        primaryStage.setMinWidth(1366);
        primaryStage.setMinHeight(766);

        primaryStage.setScene(scene);
        primaryStage.setOnCloseRequest(this::handleWindowClose);

        primaryStage.show();
    }
private void handleLogOut() {
         System.out.println("=---------------------------------================handleLogOut 111");

    Player player = OnlineLoginPlayerHolder.getInstance().getPlayer();
    if (player != null) {
                 System.out.println("=---------------------------------================handleLogOut 222");

        // Submit logout task to the executor service
        executorService.submit(() -> {
            Request request = new Request(Action.logout, player);
            String playerRequest = JsonUtil.toJson(request);
         System.out.println("=---------------------------------================playerRequest 3333");

            jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                // This part runs in the background
                if (response != null && response.isDone()) {
                             System.out.println("=---------------------------------================handleLogOut 333");

                    System.out.println("Logout successful.");
                } else {
                    System.err.println("Logout failed.");
                }

                // Close JsonSender resources in the background
                closeJsonSenderResources();
                // Shut down the executor service in the background
                shutdownExecutorService();
            });
        });
    } else {
        // No player to log out, just shut down the executor service in the background
        shutdownExecutorService();
    }
}

private void handleWindowClose(WindowEvent event) {
    // Exit the application immediately, without waiting for the logout process
     // Perform the logout process in the background
   
     System.out.println("=---------------------------------================handleWindowClose");
     handleLogOut();
    Platform.exit(); // Closes the JavaFX application
    System.exit(0);  // Exits the JVM to ensure the window closes immediately

   
}

private void closeJsonSenderResources() {
    try {
        jsonSender.close();  // Close the JsonSender
        jsonSender.stopListeningForMessages();  // Stop listening for messages
    } catch (IOException e) {
        e.printStackTrace();
    }
}

private void shutdownExecutorService() {
    executorService.shutdown();  // Initiate shutdown
    try {
        if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
            executorService.shutdownNow();  // Force shutdown if tasks are still running
        }
    } catch (InterruptedException e) {
        executorService.shutdownNow();  // Ensure that tasks are interrupted
        Thread.currentThread().interrupt();  // Preserve interrupt status
    }
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

    @Override
    public void onResponse(Response response) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
