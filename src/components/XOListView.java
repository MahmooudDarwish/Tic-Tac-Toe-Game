///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
///*
//package components;
//
//import java.util.List;
//import javafx.collections.ObservableList;
//import javafx.geometry.Insets;
//import javafx.scene.control.ListCell;
//import javafx.scene.control.ListView;
//import javafx.scene.layout.Background;
//import javafx.scene.layout.BackgroundFill;
//import javafx.scene.layout.CornerRadii;
//import javafx.scene.media.AudioClip;
//import javafx.scene.paint.Color;
//import models.RecordName;
//import models.RecordNameHolder;
//import tictactoegame.TicTacToeGame;
//import utils.constants.AppConstants;
//
///**
// *
// * @author Mohammed
// */
//public class XOListView extends ListView {
//
//    private static final String DEFAULT_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-text-fill: black; -fx-font-size: 25px; -fx-font-family: Tahoma;";
//    private static final String HOVER_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightblue; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";
//    private static final String SELECTED_STYLE = "-fx-padding: 20 0; -fx-alignment: CENTER; -fx-background-color: lightGray; -fx-text-fill: red; -fx-font-size: 25px; -fx-font-family: Tahoma;";
//
//    public XOListView(AudioClip hoverSound,ObservableList list,Object obj) {
//
//        // Set a custom CellFactory
//        setCellFactory(lv -> new ListCell<String>() {
//            @Override
//            protected void updateItem(String item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                    setStyle(DEFAULT_STYLE);
//                    setBackground(new Background(new BackgroundFill(
//                            new Color(1, 1, 1, 0.5), // White with 50% opacity
//                            CornerRadii.EMPTY,
//                            Insets.EMPTY
//                    )));
//                } else {
//                    setText(item);
//                    setStyle(isSelected() ? SELECTED_STYLE : DEFAULT_STYLE);
//
//                    // Handle hover effect
//                    setOnMouseEntered(event -> {
//                        if (!isSelected()) {
//                            setStyle(HOVER_STYLE);
//                            hoverSound.play(); // Play sound on hover
//
//                        }
//                    });
//
//                    setOnMouseExited(event -> {
//                        if (!isSelected()) {
//                            setStyle(DEFAULT_STYLE);
//                        }
//                    });
//
//                    // Handle selection effect
//                    selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
//                        if (isNowSelected) {
//                            setStyle(SELECTED_STYLE);
//                            // Get the selected item from the ListView
//                            String selectedItem = list.getSelectionModel().getSelectedItem();
//
//                            // Print or use the selected item
//                            System.out.println("Selected Item: " + selectedItem);
//                            System.out.println("Navigate to Play Record Screen");
//                            recordName = new RecordName(selectedItem);
//
//                            RecordNameHolder recordNameHolder = RecordNameHolder.getInstance();
//                            recordNameHolder.setRecordName(recordName);
//                            TicTacToeGame.changeRoot(AppConstants.playRecordScreenPath);
//
//                        } else {
//                            setStyle(DEFAULT_STYLE);
//                        }
//                    });
//                }
//            }
//        });
//
//    }
//
//}
//
//}
///