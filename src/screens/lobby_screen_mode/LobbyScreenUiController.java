package screens.lobby_screen_mode;

import components.CustomPopup;
import components.XOLabel;
import components.XOButton;
import javafx.collections.FXCollections;
import javafx.scene.control.ListCell;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.scene.layout.HBox;
import tictactoegame.TicTacToeGame;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.media.AudioClip;
import models.OnlinePlayer;
import utils.constants.AppConstants;
import utils.jsonutil.JsonSender;

public class LobbyScreenUiController implements Initializable {

    @FXML
    AnchorPane anchorPane;
    private List<OnlinePlayer> players;
    @FXML
    private ListView<OnlinePlayer> playersView;
    private XOButton backBtn;
    private AudioClip hoverSound;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
    }

    private void handleFetchPlayersButtonAction() {
        // Create a JSON request with action "gamelobby"
        String jsonRequest = "{\"action\":\"gamelobby\"}";
        System.out.println("Sending JSON: " + jsonRequest);

        // Send JSON and receive response
        players = JsonSender.sendJsonAndReceivePlayersList(jsonRequest, AppConstants.getServerIp(), 5006);
        if (players != null) {
            System.out.println("Received players list: " + players);
            updateListViewWithPlayers(players);
        } else {
            handlePopup("Error", AppConstants.warningIconPath, "Failed to connect to server or no players found.");
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

        // Add back button to layout at the center bottom
        AnchorPane.setBottomAnchor(backBtn, 20.0);  // Distance from the bottom
        AnchorPane.setLeftAnchor(backBtn, 600.0); // Center horizontally
        anchorPane.getChildren().add(backBtn);
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
                } else {
                    nameLabel.setText("Player Name: " + item.getUserName());
                    scoreLabel.setText("Score: " + item.getPoints());

                    scoreLabel.setPadding(new Insets(0, 150, 0, 0));
                    nameLabel.setPadding(new Insets(0, 0, 0, 150));

                    HBox.setHgrow(spacer, Priority.ALWAYS);
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
        System.out.println("Navigate to player history screen");
        TicTacToeGame.changeRoot(AppConstants.userHomePath);
    }
}
