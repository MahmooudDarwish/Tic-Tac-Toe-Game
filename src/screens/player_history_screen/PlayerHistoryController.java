package screens.player_history_screen;

import components.XOButton;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import utils.game_file_manager.GameFileManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.geometry.Pos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import models.RecordName;
import models.RecordNameHolder;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;

public class PlayerHistoryController implements Initializable {

    @FXML
    private ListView<String> recordesView;
    private List<String> recordesPaths;
    static String selectedRecordName;
    private RecordName recordName;
    private XOButton backBtn;
    private AudioClip hoverSound;

    @FXML
    private AnchorPane anchorPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Load hover sound effect
        hoverSound = new AudioClip(getClass().getResource(AppConstants.buttonClickedTonePath).toString());

        backBtn = new XOButton("Back",
                this::handlBackButtonAction,
                AppConstants.backIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);
// Retrieve the file paths from GameFileManager
        recordesPaths = GameFileManager.listRecordsFile("Records");

        // Optionally remove border and padding
        recordesView.setStyle("-fx-border-width: 0; -fx-padding: 0;");

        if (recordesPaths != null) {
            ObservableList<String> items = FXCollections.observableArrayList(recordesPaths);

            // Set the items to the ListView
            recordesView.setItems(items);
            recordesView.setStyle(
                    "-fx-border-width: 0;"
                    + "-fx-padding: 0;"
                    + "-fx-background-image: url('/assets/images/BluredXOImage.jpg');"
                    + "-fx-background-repeat: no-repeat;"
                    + "-fx-background-size: cover;"
                    + "-fx-background-color: blue;" // Background color
            );
            listVeiwProperties(recordesView);

        } else {
            System.out.println("No files found or an error occurred.");
        }

        // Add back button to layout at the center bottom
        AnchorPane.setBottomAnchor(backBtn, 20.0);  // Distance from the bottom
        AnchorPane.setLeftAnchor(backBtn, 600.0); // Center horizontally
        anchorPane.getChildren().add(backBtn);
    }

    private void listVeiwProperties(ListView<String> listVeiw) {
        // Set a custom CellFactory
        listVeiw.setCellFactory(lv -> new ListCell<String>() {
            private static final String DEFAULT_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String HOVER_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightblue; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";
            private static final String SELECTED_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightGray; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle(DEFAULT_STYLE);
                    setBackground(new Background(new BackgroundFill(
                            new Color(1, 1, 1, 0.5), // White with 50% opacity
                            CornerRadii.EMPTY,
                            Insets.EMPTY
                    )));
                } else {
                    setText(item);
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
                            String selectedItem = recordesView.getSelectionModel().getSelectedItem();

                            // Print or use the selected item
                            System.out.println("Selected Item: " + selectedItem);
                            System.out.println("Navigate to Play Record Screen");
                            recordName = new RecordName(selectedItem);

                            RecordNameHolder recordNameHolder = RecordNameHolder.getInstance();
                            recordNameHolder.setRecordName(recordName);
                            TicTacToeGame.changeRoot(AppConstants.playRecordScreenPath);

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
        TicTacToeGame.changeRoot(AppConstants.startScreenPath);
    }
}
