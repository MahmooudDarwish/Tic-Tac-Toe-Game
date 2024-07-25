package components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import utils.helpers.ToneManager;

public class XOButton extends Button {

    public XOButton(String label, Runnable action, String iconPath, double width, double height, String tone) {
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        iconView.setFitHeight(16);
        iconView.setFitWidth(16);
        
         setText("  "+label);
       // Label textLabel = new Label(label); // The button text

      /*  HBox hbox = new HBox(iconView); // Combine icon and text
        hbox.setSpacing(20); // Spacing between icon and text
        hbox.setAlignment(Pos.CENTER_LEFT); */// Align content to the left

        setGraphic(iconView); // Set the HBox as the button's graphic
        setStyle("-fx-background-color: white;"
                + " -fx-border-color: grey;"
                + " -fx-border-width: 2px;"
                + " -fx-border-radius: 10px; "
                + "-fx-background-radius: 10px; "
                + " -fx-font-size: 16px;"
                + " -fx-text-fill: black;"
        );
        setMinSize(width, height);

        setOnAction(event
                -> {
            ToneManager.playTone(tone);
            action.run();
        }); // Set the action
    }
    
}
