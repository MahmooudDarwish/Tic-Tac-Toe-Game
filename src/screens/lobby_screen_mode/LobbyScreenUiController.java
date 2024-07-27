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
import javafx.application.Platform;
import javafx.scene.layout.HBox;
import tictactoegame.TicTacToeGame;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
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
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.InOnlineResponse;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayer;
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
    private Response response;
    private OnlineLoginPlayerHolder onlineLoginPlayerHolder;
    private InOnlineResponse inOnlineResponse;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        player = onlineLoginPlayerHolder.getPlayer();

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
        String jsonRequest = "{\"action\":\"gamelobby\"}";
        System.out.println("Sending JSON: " + jsonRequest);

        inOnlineResponse = JsonSender.sendJsonAndReceivePlayersList(jsonRequest, AppConstants.getServerIp(), 5006);
        if (inOnlineResponse != null) {
            System.out.println("Received players list: " + inOnlineResponse.getPlayers());
            updateListViewWithPlayers(inOnlineResponse.getPlayers());
        } else {
            handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or no players found.");
        }
        /*
        onlineLoginPlayerHolder.startGetOnlinePlayerThread();
        List<OnlinePlayer> players = onlineLoginPlayerHolder.getPlayers();

        if (players != null) {
            System.out.println("Received players list: " + players);

            // Update ListView with players on the JavaFX Application Thread
            Platform.runLater(() -> updateListViewWithPlayers(players));
        } else {
            Platform.runLater(() -> handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or no players found."));
        }
         */

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
            //onlineLoginPlayerHolder.stopGetOnlinePlayerThread();

        }
    }

    private void updateListViewWithPlayers(List<OnlinePlayer> players) {
        if (players != null) {
            ObservableList<OnlinePlayer> items = FXCollections.observableArrayList(players);

            // Set the items to the ListView
            playersView.setItems(items);
            playersView.setStyle(
                    "-fx-border-width: 0;"
                    + "-fx-padding: 0;"
                    + "-fx-background-image: url('/assets/images/BluredXOImage.jpg');"
                    + "-fx-background-repeat: no-repeat;"
                    + "-fx-background-size: cover;"
                    + "-fx-background-color: blue;" // Background color
            );
            listVeiwProperties(playersView);
        } else {
            System.out.println("No players found or an error occurred.");
        }

        if (!anchorPane.getChildren().contains(backBtn)) {
            AnchorPane.setBottomAnchor(backBtn, 20.0);  // Distance from the bottom
            AnchorPane.setLeftAnchor(backBtn, 600.0); // Center horizontally
            anchorPane.getChildren().add(backBtn);
        }
    }

    private void handlePopup(String popupTitle, String iconPath, String message) {
        XOLabel popupResponseMessageLabel = new XOLabel(iconPath, message, 250, 80, true);
        CustomPopup cp = new CustomPopup(popupTitle, 130, 600, true);

        cp.addContent(popupResponseMessageLabel);
        cp.addCancelButton("OK");
        cp.show();
    }

    private void listVeiwProperties(ListView<OnlinePlayer> listVeiw) {
        // Set a custom CellFactory
        listVeiw.setCellFactory(lv -> new ListCell<OnlinePlayer>() {
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
                            // Get the selected item from the ListView
                            OnlinePlayer selectedItem = listVeiw.getSelectionModel().getSelectedItem();
                            try {
                                player.setAction("wanttoplay");
                                player.setMessage(selectedItem.getUserName());
                                //String jsonRequest = "{\"action\":\"wanttoplay\"}";
                                //System.out.println("Sending JSON: " + jsonRequest);
                                // Convert player object to JSON
                                String json = JsonUtil.toJson(player);
                                System.out.println("Sending JSON: " + json);
                                // Send JSON and receive response
                                response = JsonSender.sendJsonAndReceiveResponse(json, AppConstants.getServerIp(), 5006, true);
                            } catch (IOException ex) {
                                Logger.getLogger(LobbyScreenUiController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (response != null) {
                                if (response.isDone()) {
                                    System.out.println(response);
                                    System.out.println("Yes We can Play");
                                } else {
                                    handlePopup("Error", AppConstants.warningIconPath, selectedItem.getUserName()+"Is busy");

                                }
                            } else {
                                handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or"+ selectedItem.getUserName()+ "no players found.");
                            }

                            // Print or use the selected item
                            System.out.println("Selected Item: " + selectedItem.getUserName() + "       " + selectedItem.getPoints());
                            System.out.println("Navigate to Play Record Screen");
                        } else {
                            setStyle(DEFAULT_STYLE);
                        }
                    });
                }
            }
        });
    }

    private void handlBackButtonAction() {
        // Stop the periodic update before navigating away
        stopPeriodicPlayerListUpdate();

        System.out.println("Navigate to player history screen");
        TicTacToeGame.changeRoot(AppConstants.userHomePath);
    }
}
