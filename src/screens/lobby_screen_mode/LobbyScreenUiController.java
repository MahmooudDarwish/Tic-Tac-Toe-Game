package screens.lobby_screen_mode;

import components.CustomPopup;
import components.XOLabel;
import components.XOButton;
import handlingplayerrequests.AllOnlinePlayer;
import handlingplayerrequests.PlayerRequestHandler;
import handlingplayerrequests.RequestStatus;
import handlingplayerrequests.StopPeriodicPlayerListUpdateInterface;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.media.AudioClip;
import javafx.scene.layout.Region;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import models.OnlineLoginPlayerHolder;
import models.OnlinePlayerHolder;
import models.Player;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;

public class LobbyScreenUiController implements Initializable, RequestStatus, AllOnlinePlayer, StopPeriodicPlayerListUpdateInterface {

    @FXML
    private ListView<Player> playersView;
    private XOButton backBtn;
    private AudioClip hoverSound;
    private PauseTransition updatePlayersListTransition;
    private Player player;
    private OnlineLoginPlayerHolder onlineLoginPlayerHolder;
    private OnlinePlayerHolder onlinePlayerHolder;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1); // or any appropriate number of threads
    private PlayerRequestHandler requestHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Set background image using inline CSS
        playersView.setStyle(
                "-fx-background-image: url('/assets/images/BluredXOImage.jpg');"
                + "-fx-background-repeat: no-repeat;"
                + "-fx-background-size: cover;"
                + "-fx-background-position: center center;"
        );

        // Other initialization code...
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        onlinePlayerHolder = OnlinePlayerHolder.getInstance();
        player = onlineLoginPlayerHolder.getPlayer();
        onlinePlayerHolder.setXPlayer(player);
        requestHandler = new PlayerRequestHandler(this, this, this,null);
        hoverSound = new AudioClip(getClass().getResource(AppConstants.buttonClickedTonePath).toString());
        backBtn = new XOButton("Back", this::handleBackButtonAction, AppConstants.backIconPath, 200, 40, AppConstants.buttonClickedTonePath);

        handleFetchPlayersButtonAction();
        startPeriodicPlayerListUpdate();
    }

    private void handleFetchPlayersButtonAction() {
        executorService.submit(() -> {
            requestHandler.getAllOnlinePlayers();
        });
    }

    private void startPeriodicPlayerListUpdate() {
        updatePlayersListTransition = new PauseTransition(Duration.seconds(5));
        updatePlayersListTransition.setOnFinished(event -> {
            handleFetchPlayersButtonAction();
            updatePlayersListTransition.playFromStart();
        });
        updatePlayersListTransition.play();
    }

    public void stopPeriodicPlayerListUpdate() {
        if (updatePlayersListTransition != null) {
            updatePlayersListTransition.stop();
        }
    }

    private void updateListViewWithPlayers(List<Player> players) {
        System.out.println("------------------------------------222222" + players.size());

        if (players != null && !players.isEmpty()) {
            System.out.println("------------------------------------33333333333" + players.size());

            ObservableList<Player> items = FXCollections.observableArrayList(players);
            playersView.setItems(items);
            playersView.setPlaceholder(null); // Remove placeholder if players are present
        } else {
            // Display placeholder message when no players are found
            Label noPlayersLabel = new Label("No Online Players Found");
            System.out.println("------------------------------------No Online Players Found");
            noPlayersLabel.setStyle(
                    "-fx-font-size: 16px; "
                    + "-fx-text-fill: black; "
                    + "-fx-padding: 10px; "
                    + "-fx-background-color: rgba(0, 0, 0, 0.5);" // Semi-transparent background
                    + "-fx-background-radius: 5px;" // Rounded corners for background
            );
            playersView.setPlaceholder(noPlayersLabel);
            playersView.setItems(FXCollections.emptyObservableList());
        }

        // Apply custom styles to ListView cells
        listViewProperties(playersView);
    }

//    private void updateListViewWithPlayers(List<Player> players) {
//        if (players != null && !players.isEmpty()) {
//            ObservableList<Player> items = FXCollections.observableArrayList(players);
//            playersView.setItems(items);
//        } else {
//            Label noPlayersLabel = new Label("No Online Players Found");
//            noPlayersLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: white; -fx-padding: 10px;");
//            playersView.setPlaceholder(noPlayersLabel);
//            playersView.setItems(FXCollections.emptyObservableList());
//        }
//
//        listViewProperties(playersView);
//    }
    private void listViewProperties(ListView<Player> listView) {
        listView.setCellFactory(lv -> new ListCell<Player>() {
            private static final String DEFAULT_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String HOVER_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightblue; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String SELECTED_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightGray; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";

            private HBox hbox = new HBox();
            private Region spacer = new Region();
            private Label nameLabel = new Label();
            private Label scoreLabel = new Label();

            @Override
            protected void updateItem(Player item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(DEFAULT_STYLE);
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

                    // Handle click event (Player selection)
                    setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {  // Single click
                            setStyle(SELECTED_STYLE);
                            onPlayerSelected(item);  // Action when player is selected
                        }
                    });
                }
            }
        });
    }
// Action to perform when a player is selected

    private void onPlayerSelected(Player selectedPlayer) {
        
        requestHandler.wantToPaly(selectedPlayer.getUserName());
    }

    private void handleBackButtonAction() {
        stopPeriodicPlayerListUpdate();
        System.out.println("Navigate to player history screen");
        TicTacToeGame.changeRoot(AppConstants.userHomePath);
    }

    @Override
    public void onSuccess(String msg) {
        // Handle success scenario
    }

    @Override
    public void onFailure(String msg) {
        // Handle failure scenario
    }

    @Override
    public void setAllOnlinePlayer(List<Player> players) {
        Platform.runLater(() -> updateListViewWithPlayers(players)); // Convert Set to List for ListView
    }
}
