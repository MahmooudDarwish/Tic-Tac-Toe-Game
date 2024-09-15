package utils.jsonutil;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import static com.sun.corba.se.spi.presentation.rmi.StubAdapter.request;
import components.CustomPopup;
import components.XOButton;
import components.XOLabel;
import enumpackages.EnumPlayerAction;
import handlingplayerrequests.HandleWantToPlayResponseInterface;
import handlingplayerrequests.PlayerRequestHandler;
import models.Response;
import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayerHolder;
import models.Player;
import models.Request;

public class JsonSender {

    private final Gson gson = new Gson();
    private Socket socket;
    private PrintWriter writer;
    private BufferedReader reader;
    String jsonString = null;
    private boolean running = false;
    private Thread listenerThread = null;
    private final Logger logger = Logger.getLogger(JsonSender.class.getName());
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    public Boolean isServerDown = false;
    private Request request;
    private Timeline timeline;
    private static volatile JsonSender instance; // Use volatile for safe publication across threads
    private ResponseCallback responseCallback;
    private HandleWantToPlayResponseInterface handleWantToPlayResponse;

    // Private constructor to prevent direct instantiation
    public JsonSender(ResponseCallback responseCallback) {
        this.responseCallback = responseCallback;
    }

    // Double-checked locking to create singleton instance
    public static JsonSender getInstance(ResponseCallback responseCallback) {
        if (instance == null) { // First check (without locking)
            synchronized (JsonSender.class) {
                if (instance == null) { // Second check (with locking)
                    instance = new JsonSender(responseCallback);
                }
            }
        }
        return instance;
    }

    public void init() throws IOException {

        if (socket == null || socket.isClosed()) {
            socket = new Socket(AppConstants.getServerIp(), AppConstants.getServerPort());
            writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
    }

    public void sendJsonAndReceiveResponseAsync(String jsonData, ResponseCallback responseCallback) {
        if (executorService.isShutdown()) {
            // If the executor service was shut down, restart it
            restartExecutorService();
        }
        executorService.submit(() -> {
            if (writer == null || reader == null) {
                throw new IllegalStateException("Socket connection is not initialized.");
            }
            writer.println(jsonData);
            writer.flush(); // Ensure data is sent immediately

        });
        this.responseCallback = responseCallback;

    }

    public void startListeningForMessages() {
        if (!running) {
            running = true;
            if (executorService.isShutdown()) {
                restartExecutorService();
            }

            executorService.submit(() -> {
                System.out.println("Start Listening For Messages..................");
                try {
                    String line;
                    while (running && (line = reader.readLine()) != null) {
                        Response response = gson.fromJson(line, Response.class);
                        logger.info("------------==================-----------===startListeningForMessages=======" + response.getMessage());

                        handleServerMessage(response);
                    }
                } catch (IOException e) {
                    if (running) {
                        logger.log(Level.SEVERE, "Error reading from server", e);
                        isServerDown = true;
                        handleNetworkPopup(); // Show popup when server is down
                    }
                }
            });
        }
    }

    private void handleServerMessage(Response response) {
        isServerDown = false;
        String message = response.getMessage();

        if ("Server is down".equals(message)) {
            logger.info("------------==================-----------==========" + response.getMessage());
            handleNetworkPopup();

        } else if (message.contains("wants to play with you")) {
            logger.info("------------==================---------wants to play with you--==========" + response.getMessage());

            handleWantsToPlayPopup(message);

        } else if (message.equals("has started the game")) {
            logger.info("------------==================---------Accept--==========" + response.getMessage());
            isServerDown = true;

            OnlinePlayerHolder players = OnlinePlayerHolder.getInstance();
            players.setXPlayer(response.getPlayers().get(0));
            players.setOPlayer(response.getPlayers().get(1));
            TicTacToeGame.changeRoot(AppConstants.onlinegameBoardScreenPath);
        } else if (message.equals("declined the game request")) {
            logger.info("------------==================---------Refuse--==========" + response.getMessage());

        } else if (message.contains("move")) {
            logger.info("------------==================---------move--llllll==========" + response.getMessage());

            Platform.runLater(() -> {
                PlayerRequestHandler.getMove(response.getMessage());
            });

        } else {
            responseCallback.onResponse(response);
        }
    }

    private void shutdownExecutorService() {
        executorService.shutdown();  // Initiates an orderly shutdown
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                executorService.shutdownNow();  // Force shutdown if tasks are still running
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();  // Ensure that tasks are interrupted
            Thread.currentThread().interrupt();  // Preserve interrupt status
        }
    }

    private void closeJsonSenderResources() {
        try {
            cleanup();
            close();  // Close the JsonSender
            stopListeningForMessages();  // Stop listening for messages
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleNetworkPopup() {
        Task<Void> networkCheckTask = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    CustomPopup cp = new CustomPopup("Warning", 170, 600, false);
                    XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.warningIconPath, "Server is Down", 250, 80, true);
                    XOButton okButton = new XOButton("Ok",
                            () -> {
                                TicTacToeGame.changeRoot(AppConstants.loginPath);
                                // Close JsonSender resources in the background
                                isServerDown = true;
                                shutdownExecutorService();
                                closeJsonSenderResources();
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
                return null;
            }
        };

        new Thread(networkCheckTask).start();
    }

    public String extractPlayerName(String message) {
        String phrase = "wants to play with you";

        // Check if the message contains the phrase
        if (message.contains(phrase)) {
            // Get the index of the phrase in the message
            int endIndex = message.indexOf(phrase);

            // Extract the substring before the phrase
            return message.substring(0, endIndex).trim();
        } else {
            // If the message does not contain the phrase, return null or an empty string
            return null;
        }
    }

    private void handleWantsToPlayPopup(String message) {
        Task<Void> wantsToPlayTask = new Task<Void>() {
            @Override
            protected Void call() {
                Platform.runLater(() -> {
                    // Create a vertical layout (VBox) to ensure all elements are laid out correctly
                    VBox layout = new VBox(10);  // 10px spacing between elements
                    layout.setAlignment(Pos.CENTER);
                    layout.setPadding(new Insets(20));  // Add padding for better UI

                    CustomPopup cp = new CustomPopup("Playing Request", 300, 600, false); // Increased popup size
                    XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.palyIcone2Path, message, 250, 80, true);

                    // "Yes" button
                    XOButton yesButton = new XOButton("Yes",
                            () -> {
                                // Handle Yes action
                                logger.info("User accepted the play request.");
                                sendPlayerRequest(EnumPlayerAction.Action.yes, message);
                                stopCountdown(); // Stop the countdown when Yes is clicked
                                cp.close();
                            },
                            AppConstants.xIconPath,
                            140,
                            40,
                            AppConstants.buttonClickedTonePath
                    );

                    // "No" button
                    XOButton noButton = new XOButton("No",
                            () -> {
                                // Handle No action
                                logger.info("User declined the play request.");
                                sendPlayerRequest(EnumPlayerAction.Action.no, message);
                                stopCountdown(); // Stop the countdown when Yes is clicked

                                cp.close();
                            },
                            AppConstants.oIconPath,
                            140,
                            40,
                            AppConstants.buttonClickedTonePath
                    );

                    // Countdown label
                    XOLabel countdownLabel = new XOLabel("", "10", 50, 30, true);
                // Countdown logic
                      timeline= new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                        int timeLeft = Integer.parseInt(countdownLabel.getText()) - 1;
                        countdownLabel.setText(String.valueOf(timeLeft));
                        if (timeLeft <= 0) {
                            stopCountdown(); // Stop the countdown when time runs out
                            cp.close();
                            sendAutoDeclineRequest(message);  // Auto-decline if time runs out
                        }
                    }));
                    timeline.setCycleCount(Timeline.INDEFINITE); // Set to indefinite to run until manually stopped
                    timeline.play();

                    // Add content to layout (VBox)
                    layout.getChildren().addAll(popupResponseMessageLabel, countdownLabel, yesButton, noButton);

                    // Set layout in the popup
                    cp.addContent(layout);

                    // Show the popup
                    cp.show();
                });
                return null;
            }
        };
        executorService.submit(wantsToPlayTask);
    }

    // Method to stop the countdown
    private void stopCountdown() {
        if (timeline != null) {
            timeline.stop();
        }
    }

// Method to send player request (used by both Yes and No buttons)
    private void sendPlayerRequest(EnumPlayerAction.Action action, String message) {
        executorService.submit(() -> {
            try {
                String namePlayerThatIWillPlayWith = extractPlayerName(message);
                if (namePlayerThatIWillPlayWith != null) {
                    OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
                    Player player = onlineLoginPlayerHolder.getPlayer();
                    Request request = new Request(action, player, namePlayerThatIWillPlayWith);
                    String playerRequest = JsonUtil.toJson(request);
                    logger.info("User request: " + playerRequest);
                    sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                        // Handle response if needed
                    });
                }
            } catch (Exception ex) {
                logger.severe("Error while sending " + action + " request: " + ex.getMessage());
            }
        });
    }

// Method to send auto-decline when timeout happens
    private void sendAutoDeclineRequest(String message) {
        logger.info("User did not respond in time. Auto-decline.");
        sendPlayerRequest(EnumPlayerAction.Action.no, message);
    }

//    private void handleWantsToPlayPopup(String message) {
//        Task<Void> wantsToPlayTask = new Task<Void>() {
//            @Override
//            protected Void call() {
//                Platform.runLater(() -> {
//                    CustomPopup cp = new CustomPopup("Playing Request", 170, 600, false);
//                    XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.palyIcone2Path, message, 250, 80, true);
//
//                    XOButton yesButton = new XOButton("Yes",
//                            () -> {
//                                // Handle Yes action
//                                logger.info("User accepted the play request.");
//                                OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
//                                Player player = onlineLoginPlayerHolder.getPlayer();
//
//                                executorService.submit(() -> {
//                                    try {
//                                        String namePlayerThatIWillPlayWith = extractPlayerName(message);
//                                        System.out.println("namePlayerThatIWillPlayWith"+namePlayerThatIWillPlayWith);
//                                        if (namePlayerThatIWillPlayWith != null) {
//                                            Request request = new Request(EnumPlayerAction.Action.yes, player, namePlayerThatIWillPlayWith);
//                                            String playerRequest = JsonUtil.toJson(request);
//                                            logger.info("User request: " + playerRequest);
//                                            sendJsonAndReceiveResponseAsync(playerRequest, response -> {
//                                                // Handle the response if needed
//                                            });
//                                        }
//                                    } catch (Exception ex) {
//                                        logger.severe("Error while sending Yes request: " + ex.getMessage());
//                                    }
//                                });
//
//                                cp.close();
//                            },
//                            AppConstants.xIconPath,
//                            140,
//                            40,
//                            AppConstants.buttonClickedTonePath
//                    );
//
//                    XOButton noButton = new XOButton("No",
//                            () -> {
//                                // Handle No action
//                                logger.info("User declined the play request.");
//                                OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
//                                Player player = onlineLoginPlayerHolder.getPlayer();
//
//                                executorService.submit(() -> {
//                                    try {
//                                        String namePlayerThatIWillPlayWith = extractPlayerName(message);
//                                        if (namePlayerThatIWillPlayWith != null) {
//                                            Request request = new Request(EnumPlayerAction.Action.no, player, namePlayerThatIWillPlayWith);
//                                            String playerRequest = JsonUtil.toJson(request);
//                                            logger.info("User request: " + playerRequest);
//                                            sendJsonAndReceiveResponseAsync(playerRequest, response -> {
//                                                // Handle the response if needed
//                                            });
//                                        }
//                                    } catch (Exception ex) {
//                                        logger.severe("Error while sending No request: " + ex.getMessage());
//                                    }
//                                });
//
//                                cp.close(); // Simply close the popup
//                            },
//                            AppConstants.oIconPath,
//                            140,
//                            40,
//                            AppConstants.buttonClickedTonePath
//                    );
//
//                    // Add content to the popup
//                    cp.addContent(popupResponseMessageLabel);
//                    cp.addContent(yesButton);
//                    cp.addContent(noButton);
//                    cp.show();  // Show the popup on the screen
//                });
//                return null;
//            }
//        };
//
//        new Thread(wantsToPlayTask).start();
//    }
    public void stopListeningForMessages() {
        running = false;
        executorService.shutdownNow(); // Initiates an orderly shutdown and attempts to stop all actively executing tasks
        try {
            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                logger.warning("ExecutorService did not terminate in the specified time.");
            }
            System.out.println("Stop Listening For Messages.................." + running);
        } catch (InterruptedException e) {
            logger.warning("Interrupted during ExecutorService shutdown.");
            // (Re-)Cancel if current thread also interrupted
            Thread.currentThread().interrupt();
        }
    }

    public void cleanup() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
            if (writer != null) {
                writer.close();
                writer = null;
            }
        } catch (IOException e) {
            logger.log(Level.WARNING, "Error closing resources", e);
        }
        System.out.println("JsonSender cleanup completed.");
    }

    public void close() throws IOException {
        if (socket != null && !socket.isClosed()) {
            socket.close();
            socket = null;
        }
        System.out.println("JsonSender close socket completed.");
    }

    private void restartExecutorService() {
        shutdownExecutorService(); // Ensure old service is shut down
        executorService = Executors.newFixedThreadPool(2); // Create a new executor service
        logger.info("ExecutorService has been restarted.");
    }

}
/*
     public static void startListeningForMessages() {
    if (listenerThread == null || !listenerThread.isAlive()) {
        running = true;
        listenerThread = new Thread(() -> {
            try {
                String line;
                 synchronized (reader) {
                    while (running && (line = reader.readLine()) != null) {
                        Response response = gson.fromJson(line, Response.class);
                        handleServerMessage(response);
                    }
                }
            } catch (IOException e) {
                if (running) {
                    LOGGER.log(Level.SEVERE, "Error reading from server", e);
                }
            }
        });
        listenerThread.start();
    }
}

public static void stopListeningForMessages() {
    running = false;
    if (listenerThread != null) {
        try {
            listenerThread.join();
        } catch (InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error stopping listener thread", e);
        }
    }
}
 */
// 
//   public static void sendJsonAndReceiveResponseAsync(String jsonData, ResponseCallback callback) {
//    executorService.submit(() -> {
//        try {
//            if (writer == null || reader == null) {
//                throw new IllegalStateException("Socket connection is not initialized.");
//            }
//            
//                writer.println(jsonData);
//                writer.flush(); // Ensure data is sent immediately
//            
//            
//            String jsonString;
//                jsonString = reader.readLine();
//            
//
//            Response response = (jsonString != null)
//                ? gson.fromJson(jsonString, Response.class)
//                : new Response(false, "No response received from the server");
//                
//            callback.onResponse(response);
//                    System.out.println("-----------==========++++++++++"+response.isDone());
//
//
//        } catch (JsonSyntaxException | IOException e) {
//            Response response = new Response(false, "Exception occurred during data transfer: " + e.getMessage());
//            callback.onResponse(response);
//            LOGGER.log(Level.SEVERE, "Error sending JSON and receiving response", e);
//        } finally {
//            // Optional: Close resources if necessary, or handle any additional cleanup
//        }
//        
//    });
//}
// Sends JSON data asynchronously and handles the response
// Sends JSON data asynchronously and handles the response
