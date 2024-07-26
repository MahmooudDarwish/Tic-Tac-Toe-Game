/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.game_board_screen.models;

import components.XOButton;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import utils.constants.AppConstants;

/**
 *
 * @author Mahmoud
 */
public class Cell {

    private String player;
    private Button button;

    public Cell() {
        button = new Button();
        button.setPrefSize(100, 100);
        button.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        button.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        button.setContentDisplay(ContentDisplay.CENTER);
    }

    public Button getButton() {
        return button;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player, Image image) {

        this.player = player;
        ImageView imageView = new ImageView(image);
        imageView.setOpacity(0.7);
        imageView.setFitHeight(50);
        imageView.setFitWidth(50);

        button.setGraphic(image != null ? imageView : null);
        if (image != null) {
            button.setDisable(true);
        } else {
            button.setDisable(false);

        }

    }
   
}
