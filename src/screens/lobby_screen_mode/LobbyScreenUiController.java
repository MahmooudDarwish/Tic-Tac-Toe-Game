package screens.lobby_screen_mode;

import components.CustomPopup;
import components.XOLabel;
import components.XOButton;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import tictactoegame.TicTacToeGame;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.InOnlineResponse;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
import models.OnlinePlayerHolder;
import models.Response;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

public class LobbyScreenUiController implements Initializable {

    @FXML
    AnchorPane anchorPane;
    @FXML
    private ListView<OnlinePlayer> playersView;
    private XOButton backBtn;
    private AudioClip hoverSound;
    private PauseTransition updatePlayersListTransition;
    private OnlinePlayer player;
    private OnlineLoginPlayerHolder onlineLoginPlayerHolder;
    private OnlinePlayerHolder onlinePlayerHolder;
    private InOnlineResponse inOnlineResponse;
    public static Thread fetchPlayersThread;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        onlinePlayerHolder = OnlinePlayerHolder.getInstance();

        player = onlineLoginPlayerHolder.getPlayer();
        onlinePlayerHolder.setXPlayer(player);

        hoverSound = new AudioClip(getClass().getResource(AppConstants.buttonClickedTonePath).toString());

        backBtn = new XOButton("Back",
                this::handlBackButtonAction,
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        anchorPane.setBackground(
                new Background(new BackgroundImage(new Image(AppConstants.bachgroundImagePath), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT,
                        BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT)));

        handleFetchPlayersButtonAction();
        startPeriodicPlayerListUpdate();
    }

    private void handleFetchPlayersButtonAction() {
        fetchPlayersThread = new Thread(() -> {
            player.setAction("gamelobby");
            String jsonRequest = JsonUtil.toJson(player);
            System.out.println("Sending JSON: " + jsonRequest);
            inOnlineResponse = JsonSender.sendJsonAndReceivePlayersList(jsonRequest);
            Platform.runLater(() -> {
                if (inOnlineResponse != null) {
                    System.out.println("Received players list: " + inOnlineResponse.getPlayers());
                    updateListViewWithPlayers(inOnlineResponse.getPlayers());
                } else {
                    handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or no players found.");
                }
            });
        });

        fetchPlayersThread.start();
    }

    private void startPeriodicPlayerListUpdate() {
        updatePlayersListTransition = new PauseTransition(Duration.seconds(5));
        updatePlayersListTransition.setOnFinished(event -> {
            handleFetchPlayersButtonAction();
            updatePlayersListTransition.playFromStart();
        });
        updatePlayersListTransition.play();
    }

    private void stopPeriodicPlayerListUpdate() {
        if (updatePlayersListTransition != null) {
            updatePlayersListTransition.stop();
            fetchPlayersThread.stop();
        }
    }

    private void updateListViewWithPlayers(List<OnlinePlayer> players) {
        // Clear any existing popup
        removePopup();

        // Check if there are players to display
        if (players != null && !players.isEmpty()) {
            // Convert the list of OnlinePlayer objects to ObservableList
            ObservableList<OnlinePlayer> items = FXCollections.observableArrayList(players);
            playersView.setItems(items);

            // Apply common style to the ListView
            playersView.setStyle(
                    "-fx-border-width: 0;"
                    + "-fx-padding: 0;"
                    + "-fx-background-image: url('/assets/images/BluredXOImage.jpg');"
                    + "-fx-background-repeat: no-repeat;"
                    + "-fx-background-size: cover;"
                    + "-fx-background-color: blue;" // Background color
            );
        } else {
            // Display a popup message
            showPopup("No Online Players Found");
            System.out.println("No players found or an error occurred.");

            // Clear the ListView items if no players are found
            playersView.setItems(FXCollections.emptyObservableList());
        }
        // Apply ListView properties
        listVeiwProperties(playersView);

        // Ensure the back button is added only once
        if (!anchorPane.getChildren().contains(backBtn)) {
            AnchorPane.setBottomAnchor(backBtn, 20.0);  // Distance from the bottom
            AnchorPane.setLeftAnchor(backBtn, 600.0);   // Center horizontally
            anchorPane.getChildren().add(backBtn);
        }

    }

    private void showPopup(String message) {
        // Check if a popup is already displayed
        if (getExistingPopup() == null) {
            // Create a label for the popup message
            Label popupLabel = new Label(message);
            popupLabel.setStyle(
                    "-fx-text-fill: white;"
                    + "-fx-font-size: 20px;"
                    + "-fx-background-color: rgba(0, 0, 0, 0.75);" // Semi-transparent background
                    + "-fx-padding: 10px;"
                    + "-fx-background-radius: 5px;"
            );

            // Create a StackPane to hold the popup
            StackPane popupPane = new StackPane(popupLabel);
            popupPane.setStyle("-fx-background-color: transparent;"); // Transparent background
            popupPane.setPrefSize(300, 100); // Adjust size as needed
            StackPane.setAlignment(popupPane, Pos.CENTER); // Center the popup in the StackPane

            // Add the popup to the existing container
            anchorPane.getChildren().add(popupPane);
        }
    }

    private void removePopup() {
        Node existingPopup = getExistingPopup();
        if (existingPopup != null) {
            anchorPane.getChildren().remove(existingPopup);
        }
    }

    private Node getExistingPopup() {
        // Find and return the existing popup from the StackPane
        for (Node node : anchorPane.getChildren()) {
            if (node instanceof StackPane) {
                return node;
            }
        }
        return null;
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        XOLabel popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitle, 130, 600, true);

        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    private void listVeiwProperties(ListView<OnlinePlayer> listView) {
        listView.setCellFactory(lv -> new ListCell<OnlinePlayer>() {
            private static final String DEFAULT_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String HOVER_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightblue; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String SELECTED_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightGray; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";

            private HBox hbox = new HBox();
            private Region spacer = new Region();
            private Label nameLabel = new Label();
            private Label scoreLabel = new Label();

            @Override
            protected void updateItem(OnlinePlayer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(DEFAULT_STYLE);
                    setBackground(new Background(new BackgroundFill(
                            new Color(1, 1, 1, 0.5),
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                    )));
                } else {
                    nameLabel.setText("Player Name: " + item.getUserName());
                    scoreLabel.setText("Score: " + item.getPoints());

                    scoreLabel.setPadding(new Insets(0, 150, 0, 0));
                    nameLabel.setPadding(new Insets(0, 0, 0, 150));

                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    hbox.getChildren().clear();
                    hbox.getChildren().addAll(nameLabel, spacer, scoreLabel);
                    setGraphic(hbox);
                    setStyle(isSelected() ? SELECTED_STYLE : DEFAULT_STYLE);

                    // Handle hover effect
                    setOnMouseEntered(event -> {
                        if (!isSelected()) {
                            setStyle(HOVER_STYLE);
                            hoverSound.play(); // Play sound on hover
                        }
                    });

                    setOnMouseExited(event -> {
                        if (!isSelected()) {
                            setStyle(DEFAULT_STYLE);
                        }
                    });

                    // Handle selection effect
                    selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                        if (isNowSelected) {
                            setStyle(SELECTED_STYLE);
                            OnlinePlayer selectedItem = listView.getSelectionModel().getSelectedItem();
                            // Create and play animation sequence
                            SequentialTransition sequentialTransition = new SequentialTransition();

                            player.setAction("wanttoplay");
                            player.setMessage(selectedItem.getUserName());
                            String json = JsonUtil.toJson(player);
                            System.out.println("Sending JSON: " + json);

                            new Thread(() -> {
                                try {
                                    Response response = JsonSender.sendJsonAndReceiveResponse(json);

                                    if (response != null) {
                                        if (response.isDone()) {
                                            System.out.println("response.............loby" + response);

                                            String message = response.getMessage();
                                            System.out.println(message);
                                            String playerName = message.split(": start game")[0];
                                            System.out.println("response.............player2 name" + playerName);
                                            OnlinePlayer onlinePlyer2 = new OnlinePlayer();
                                            onlinePlyer2.setUserName(playerName);
                                            onlinePlayerHolder.setOPlayer(onlinePlyer2);

                                            System.out.println("Navigate to online game Board screen");
                                            TicTacToeGame.changeRoot(AppConstants.onlinegameBoardScreenPath); // Switch to main menu screen

                                        } else {
                                            handlePopup("Error", AppConstants.warningIconPath, selectedItem.getUserName() + " is busy");
                                        }
                                    } else {
                                        handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or " + selectedItem.getUserName() + " no players found.");
                                    }
                                    System.out.println("Selected Item: " + selectedItem.getUserName() + "       " + selectedItem.getPoints());
                                    System.out.println("Navigate to Play Record Screen");

                                } catch (IOException ex) {
                                    Logger.getLogger(LobbyScreenUiController.class.getName()).log(Level.SEVERE, null, ex);
                                    Platform.runLater(() -> handlePopup("Error", AppConstants.warningIconPath, "Error occurred while processing your request."));
                                }
                            }).start();

                            PauseTransition pause = new PauseTransition(Duration.seconds(5));

                            XOLabel popupResponseMessageLabel = new XOLabel(AppConstants.loadingIcone2Path, selectedItem.getUserName() + " Received Your Message", 250, 80, true);
                            CustomPopup cp = new CustomPopup("Loading", 130, 600, false);
                            cp.addContent(popupResponseMessageLabel);
                            cp.show();

                            pause.setOnFinished(event -> {
                                cp.close();
                            });
                            sequentialTransition.getChildren().add(pause);
                            sequentialTransition.setOnFinished(event -> System.out.println(selectedItem.getUserName() + " Answer Your Message"));
                            sequentialTransition.play();
                        } else {
                            setStyle(DEFAULT_STYLE);
                        }
                    });
                }
            }
        });
    }

    private void handlBackButtonAction() {
        stopPeriodicPlayerListUpdate();
        System.out.println("Navigate to player history screen");
        TicTacToeGame.changeRoot(AppConstants.userHomePath);
    }
}
