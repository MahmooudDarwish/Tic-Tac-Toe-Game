package handlingplayerrequests;

import components.CustomPopup;
import components.XOLabel;
import enumpackages.EnumPlayerAction.Action;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import models.OnlineLoginPlayerHolder;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayerHolder;
import models.Player;
import models.Player;
import models.Request;
import models.Response;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;
import utils.jsonutil.ResponseCallback;

public class PlayerRequestHandler implements Serializable, PlayerRequestHandlerInterface, ResponseCallback {

    private static final Logger logger = Logger.getLogger(PlayerRequestHandler.class.getName());
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Request request;
    private RequestStatus requestStatus;
    private Response response;

    private List<Player> players = new ArrayList();

    private AllOnlinePlayer allOnlinePlayer;
    private StopPeriodicPlayerListUpdateInterface stopPeriodicPlayerListUpdate;
    private static HandelPlayerMoveInterface handelPlayerMove;
    private JsonSender jsonSender;

    public PlayerRequestHandler(RequestStatus requestStatus, AllOnlinePlayer allOnlinePlayer, StopPeriodicPlayerListUpdateInterface stopPeriodicPlayerListUpdate,HandelPlayerMoveInterface handelPlayerMove) {
        this.requestStatus = requestStatus;
        this.allOnlinePlayer = allOnlinePlayer;
        this.stopPeriodicPlayerListUpdate = stopPeriodicPlayerListUpdate;
        this.handelPlayerMove=handelPlayerMove;
        jsonSender = JsonSender.getInstance(this);
        
    }

    public List<Player> getListOnlinePlayer() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players != null ? players : Collections.emptyList();
    }

    @Override
    public void login(String userName, String password) {
        if (userName != null && password != null) {
            try {
                // Initialize JsonSender
                jsonSender.init();
                jsonSender.startListeningForMessages();
                executorService.submit(() -> {

                    Player player = new Player(userName, password);
                    request = new Request(Action.login, player);

                    String playerRequest = JsonUtil.toJson(request);
                    logger.info("User request: " + playerRequest);

                    jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                        try {
                            if (response != null) {
                                if (response.isDone()) {
                                    OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
                                    onlineLoginPlayerHolder.setPlayer(response.getPlayer());
                                    Platform.runLater(() -> requestStatus.onSuccess("Login Success"));
                                } else {
                                    Platform.runLater(() -> requestStatus.onFailure("Login Failed: " + response.getMessage()));
                                    closeResources();

                                }
                            } else {
                                Platform.runLater(() -> requestStatus.onFailure("Failed to connect to server"));
                                closeResources();

                            }
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Exception in response callback", e);
                            Platform.runLater(() -> requestStatus.onFailure("Exception occurred: " + e.getMessage()));
                            closeResources();

                        }
                    });

                });
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "IOException during login", ex);
                Platform.runLater(() -> requestStatus.onFailure("IOException occurred: " + ex.getMessage()));
                closeResources();
            }
        } else {
            requestStatus.onFailure("Invalid credentials");
        }
    }

    @Override
    public void register(String userName, String password) {
        if (userName != null && password != null) {
            executorService.submit(() -> {
                try {
                    Player player = new Player(userName, password);
                    request = new Request(Action.register, player);

                    String playerRequest = JsonUtil.toJson(request);
                    logger.info("User request: " + playerRequest);

                    // Initialize JsonSender
                    jsonSender.init();
                    jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                        try {
                            if (response != null) {
                                if (response.isDone()) {
                                    Platform.runLater(() -> requestStatus.onSuccess("Register successful"));
                                } else {
                                    Platform.runLater(() -> requestStatus.onFailure("Register Failed: " + response.getMessage()));
                                }
                            } else {
                                Platform.runLater(() -> requestStatus.onFailure("Failed to connect to server"));
                            }
                        } catch (Exception e) {
                            logger.log(Level.SEVERE, "Exception in response callback", e);
                            Platform.runLater(() -> requestStatus.onFailure("Exception occurred: " + e.getMessage()));
                        }
                    });

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "IOException during Register", ex);
                    Platform.runLater(() -> requestStatus.onFailure("IOException occurred: " + ex.getMessage()));
                }
            });
        } else {
            requestStatus.onFailure("Invalid credentials");
        }
        closeResources();

    }

    @Override
    public void logout() {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        Player player = onlineLoginPlayerHolder.getPlayer();

        if (player == null) {
            logger.info("No player found for logout.");
            Platform.runLater(() -> requestStatus.onFailure("No player found for logout."));
        } else {
            executorService.submit(() -> {

                request = new Request(Action.logout, player);

                String playerRequest = JsonUtil.toJson(request);
                logger.info("User request: " + playerRequest);

                // Initialize JsonSender
                jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                    try {
                        if (response != null) {
                            if (response.isDone()) {
                                Platform.runLater(() -> {
                                    requestStatus.onSuccess("Logout successful");
                                });
                                onlineLoginPlayerHolder.clearPlayer();
                                jsonSender.stopListeningForMessages();
                                closeResources();
                            } else {
                                Platform.runLater(() -> requestStatus.onFailure("Logout Failed: " + response.getMessage()));
                            }
                        } else {
                            Platform.runLater(() -> requestStatus.onFailure("Failed to connect to server"));
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in response callback", e);
                        Platform.runLater(() -> requestStatus.onFailure("Exception occurred: " + e.getMessage()));
                    }
                });

            });

        }
    }

    @Override
    public void getAllOnlinePlayers() {
        if (jsonSender.isServerDown == true) {
            System.out.println("getAllOnlinePlayers isServerDown");
            stopPeriodicPlayerListUpdate.stopPeriodicPlayerListUpdate();
            return;

        }
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        Player player = onlineLoginPlayerHolder.getPlayer();

        if (player == null) {
            logger.info("No player found for logout.");
            Platform.runLater(() -> requestStatus.onFailure("No player found for logout."));
        } else {
            executorService.submit(() -> {
                request = new Request(Action.gamelobby, player);
                String playerRequest = JsonUtil.toJson(request);
                logger.info("User request: " + playerRequest);

                // Initialize JsonSender
                jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                    try {
                        if (response != null) {
                            if (response.isDone()) {
                                System.out.println("ssssssssssssssssssssssssssssssssssss" + response.getPlayers().size());
                                Platform.runLater(() -> {
                                    requestStatus.onSuccess("Fetch Online Players successful");
                                });
                                allOnlinePlayer.setAllOnlinePlayer(response.getPlayers());

                            } else {
                                Platform.runLater(() -> requestStatus.onFailure("Fetch Online Players Failed: " + response.getMessage()));
                            }
                        } else {
                            Platform.runLater(() -> requestStatus.onFailure("Failed to connect to server"));
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in response callback", e);
                        Platform.runLater(() -> requestStatus.onFailure("Exception occurred: " + e.getMessage()));
                    }
                });

            });
        }
    }

    @Override
    public void wantToPaly(String namePlayerThatIWillPlayWhitHim) {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        Player player = onlineLoginPlayerHolder.getPlayer();

        if (player == null) {
            logger.info("No player found for logout.");
            Platform.runLater(() -> requestStatus.onFailure("No player found for logout."));
        } else {
            executorService.submit(() -> {
                request = new Request(Action.wanttoplay, player, namePlayerThatIWillPlayWhitHim);
                String playerRequest = JsonUtil.toJson(request);
                logger.info("User request: " + playerRequest);

                // Initialize JsonSender
                jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                    try {
                        if (response != null) {
                            if (response.isDone()) {
                                Platform.runLater(() -> {
                                    requestStatus.onSuccess(" Player Resive Your Request successful");
                                });
                            } else {
                                Platform.runLater(() -> requestStatus.onFailure("Your Request Failed: " + response.getMessage()));
                            }
                        } else {
                            Platform.runLater(() -> requestStatus.onFailure("Failed to connect to server"));
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in response callback", e);
                        Platform.runLater(() -> requestStatus.onFailure("Exception occurred: " + e.getMessage()));
                    }
                });

            });
        }
    }

    @Override
    public void sendMove(String move,String namePlayerThatIWillPlayWhitHim) {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        Player player = onlineLoginPlayerHolder.getPlayer();

        if (player == null) {
            logger.info("No player found for logout.");
            Platform.runLater(() -> requestStatus.onFailure("No player found for logout."));
        } else {
            executorService.submit(() -> {
                request = new Request(Action.move, player, namePlayerThatIWillPlayWhitHim,move);
                String playerRequest = JsonUtil.toJson(request);
                logger.info("User request: " + playerRequest);

                // Initialize JsonSender
                jsonSender.sendJsonAndReceiveResponseAsync(playerRequest, response -> {
                    try {
                        if (response != null) {
                            if (response.isDone()) {
                            } else {
                            }
                        } else {
                        }
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Exception in response callback", e);
                    }
                });

            });
        }
    }
    
    public static void getMove(String move){
        Platform.runLater(() ->handelPlayerMove.setPlayerMove(move));
        
    }

    private void closeResources() {
        try {
            jsonSender.close(); // Assuming JsonSender has a close() method for resource cleanup
            jsonSender.cleanup();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "Failed to close JsonSender resources", ex);
        }
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    logger.severe("ExecutorService did not terminate");
                }
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void onResponse(Response response) {
        this.response = response;
    }


}

/*
 /*
    public void handleLoginRequest(String userName, String password) {
        if (userName != null && password != null) {
            try {
                logger.info("Attempting login for user: " + userName);
                this.player = new OnlinePlayer(userName, password);
                this.action = Action.login;
                String request = JsonUtil.toJson(this);
                logger.info("User request : " + request);

                // Initialize JsonSender
                JsonSender.init();

                // Call the asynchronous method with a callback
                JsonSender.sendJsonAndReceiveResponseAsync(request, response -> {
                    Platform.runLater(() -> {
                        if (response != null) {
                            if (response.isDone()) {
                                OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
                                onlineLoginPlayerHolder.setPlayer(response.getPlayer());
                                this.isDone = true;
                                this.message = "Login successful";
                                JsonSender.startListeningForMessages();
                            } else {
                                try {
                                    this.isDone = false;
                                    this.message = "Login Failed";
                                    JsonSender.close();
                                } catch (IOException ex) {
                                    Logger.getLogger(PlayerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                        } else {
                            try {
                                this.isDone = false;
                                this.message = "Failed to connect to server";
                                JsonSender.close();
                            } catch (IOException ex) {
                                Logger.getLogger(PlayerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                });
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "An error occurred during login: " + ex.getMessage(), ex);
                this.isDone = false;
                this.message = "An error occurred during login";
            }
        } else {
            this.isDone = false;
            this.message = "Invalid credentials";
        }
    }
    public void handleLoginRequest(String userName, String password) {
        if (userName != null && password != null) {
            executorService.submit(() -> {
                try {
                    logger.info("Attempting login for user: " + userName);
                    this.player = new OnlinePlayer(userName, password);
                    this.action = Action.login;
                    String request = JsonUtil.toJson(this);
                    logger.info("User request: " + request);

                    // Initialize JsonSender
                    JsonSender.init();

                    // Send the request asynchronously
                    JsonSender.sendJsonAndReceiveResponseAsync(request, response -> {
                        Platform.runLater(() -> {
                            if (response != null) {
                                if (response.isDone()) {
                                    OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
                                    onlineLoginPlayerHolder.setPlayer(response.getPlayer());
                                    this.isDone = true;
                                    this.message = "Login successful";
                                    JsonSender.startListeningForMessages();
                                } else {
                                    handleLoginFailure("Login Failed");
                                }
                            } else {
                                handleLoginFailure("Failed to connect to server");
                            }
                        });
                    });

                } catch (IOException ex) {
                    logger.log(Level.SEVERE, "An error occurred during login: " + ex.getMessage(), ex);
                    Platform.runLater(() -> {
                        this.isDone = false;
                        this.message = "An error occurred during login";
                    });
                }
            });
        } else {
            Platform.runLater(() -> {
                this.isDone = false;
                this.message = "Invalid credentials";
            });
        }
    }

    private void handleLoginFailure(String message) {
        try {
            this.isDone = false;
            this.message = message;
            JsonSender.close();
        } catch (IOException ex) {
            logger.log(Level.SEVERE, "An error occurred while closing JsonSender: " + ex.getMessage(), ex);
        }
    }

    public void shutdownExecutor() {
        executorService.shutdown();
    }

    public void handleSignUpRequest(String userName, String password) {
        if (userName != null && password != null) {
            try {
                logger.info("Processing signup for user: " + userName);
                this.player = new OnlinePlayer(userName, password);
                this.action = Action.register;
                String request = JsonUtil.toJson(this);
                logger.info("User request : " + request);

                // Initialize JsonSender
                JsonSender.init();

                // Call the asynchronous method with a callback
                JsonSender.sendJsonAndReceiveResponseAsync(request, response -> {
                    Platform.runLater(() -> {
                        if (response != null) {
                            if (response.isDone()) {
                                this.isDone = true;
                                this.message = "Register successful";
                            } else {
                                this.isDone = false;
                                this.message = "Register Failed";
                            }
                        } else {
                            this.isDone = false;
                            this.message = "Failed to connect to server";
                        }
                    });
                });
            } catch (IOException ex) {
                logger.log(Level.SEVERE, "An error occurred during Register: " + ex.getMessage(), ex);
                this.isDone = false;
                this.message = "An error occurred during Register";
            }
        } else {
            this.isDone = false;
            this.message = "Invalid credentials";
        }
    }

    public void handleLogoutRequest() {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        this.player = onlineLoginPlayerHolder.getPlayer();

        if (player == null) {
            logger.info("No player found for logout.");
            this.message = "No player found for logout.";
            this.isDone = false;
            return;
        }

        logger.info("Attempting logout for user: " + player.getUserName());
        this.action = Action.logout;
        String request = JsonUtil.toJson(this);
        logger.info("User request: " + request);

        // Call the asynchronous method with a callback
        JsonSender.sendJsonAndReceiveResponseAsync(request, response -> {
            Platform.runLater(() -> {
                if (response != null) {
                    if (response.isDone()) {
                        onlineLoginPlayerHolder.clearPlayer();
                        logger.info("Logout successful");
                        this.message = "Logout successful.";
                        this.isDone = true;
                    } else {
                        logger.info("Logout failed: " + response.getMessage());
                        this.message = "Logout failed.";
                        this.isDone = false;
                    }
                } else {
                    logger.info("Failed to connect to server");
                    this.message = "Failed to connect to server.";
                    this.isDone = false;
                }

                try {
                    JsonSender.close();
                } catch (IOException ex) {
                    Logger.getLogger(PlayerRequestHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        });
    }

    public void handleGetOnlinePlayerRequest() {
        try {
            logger.info("Processing Get Online Player");
            initializeRequest();

            String jsonRequest = createJsonRequest();
            logger.info("User request: " + jsonRequest);

            // Call the asynchronous method with a callback
            JsonSender.sendJsonAndReceiveResponseAsync(jsonRequest, this::handleResponse);
        } catch (IOException ex) {
            logger.severe("Error initializing request: " + ex.getMessage());
        }
    }

    private void initializeRequest() throws IOException {
        OnlineLoginPlayerHolder onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        this.player = onlineLoginPlayerHolder.getPlayer();
        this.action = Action.gamelobby;
        JsonSender.init();
    }

    private String createJsonRequest() {
        return JsonUtil.toJson(this);
    }

    private void handleResponse(Response response) {
        if (response != null && response.isDone()) {
            logger.info("Successfully retrieved online players: " + response.getMessage());
            setPlayers(response.getPlayers());
            this.message = "Successfully retrieved online players";
            this.isDone = true;
        } else {
            logger.severe("Failed to retrieve online players: " + (response != null ? response.getMessage() : "No response"));
            this.message = "Failed to retrieve online players: " + (response != null ? response.getMessage() : "No response");
            this.isDone = false;
            setPlayers(null); // Clear the player list on failure
        }
    }

    public void setPlayersFromTask(ArrayList<OnlinePlayer> players) {
        // Implement the logic to set the players, possibly updating the UI or data structures
        this.setPlayers(players);
    }


 */
