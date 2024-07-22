/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.game_board_screen.models;

/**
 *
 * @author Mahmoud
 */

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import utils.constants.AppConstants;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import components.XOButton;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;

public abstract class UiUtils {
 public static VBox createScoreBox(Text scoreText, Runnable resignGameAction, Runnable recordGameAction) {
        XOButton resignButton = new XOButton("Resign", resignGameAction, AppConstants.xIconPath, 140, 40, AppConstants.buttonClickedTonePath);
        XOButton recordButton = new XOButton("Record", recordGameAction, AppConstants.oIconPath, 140, 40, AppConstants.buttonClickedTonePath);

        HBox buttonContainer = new HBox(20, resignButton, recordButton);
        buttonContainer.setAlignment(Pos.CENTER);

        scoreText.setFont(Font.font("", FontWeight.BOLD, 24));
        scoreText.setFill(Color.GREY);
        scoreText.setTextAlignment(TextAlignment.CENTER);

        VBox scoreBox = new VBox(20, buttonContainer, scoreText);
        scoreBox.setAlignment(Pos.CENTER);
        scoreBox.setPadding(new Insets(10));

        return scoreBox;
    }


    public static ImageView createPlayerImage(String path) {
        ImageView imageView = new ImageView(path);
        imageView.setOpacity(0.5);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        return imageView;
    }

    public static Text createPlayerText(String playerName) {
        Text text = new Text(playerName);
        text.setFont(Font.font("", FontWeight.BOLD, 24));
        return text;
    }
}
