//package components;
//
//import javafx.geometry.Pos;
//import javafx.scene.control.Button;
//import javafx.scene.control.Label;
//import javafx.scene.image.Image;
//import javafx.scene.image.ImageView;
//import javafx.scene.layout.HBox;
//import javafx.scene.media.Media;
//import javafx.scene.media.MediaPlayer;
//import javafx.util.Duration;
//import utils.helpers.ToneManager;
//
//public class XOButton extends Button {
//
//    public XOButton(String label, Runnable action, String iconPath, double width, double height, String tone) {
//        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
//        iconView.setFitHeight(16);
//        iconView.setFitWidth(16);
//        
//         setText("  "+label);
//        Label textLabel = new Label(label); // The button text
//
//      /*  HBox hbox = new HBox(iconView); // Combine icon and text
//        hbox.setSpacing(20); // Spacing between icon and text
//        hbox.setAlignment(Pos.CENTER_LEFT); */// Align content to the left
//
//        setGraphic(iconView); // Set the HBox as the button's graphic
//        setStyle("-fx-background-color: white;"
//                + " -fx-border-color: grey;"
//                + " -fx-border-width: 2px;"
//                + " -fx-border-radius: 10px; "
//                + "-fx-background-radius: 10px; "
//                + " -fx-font-size: 16px;"
//                + " -fx-text-fill: black;"
//        );
//        setMinSize(width, height);
//
//        setOnAction(event
//                -> {
//            ToneManager.playTone(tone);
//            action.run();
//        }); // Set the action
//    }
//    
//}

package components;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import utils.constants.BasicColors;
import utils.helpers.ToneManager;

public class XOButton extends Button {

    public XOButton(String label, Runnable action, String iconPath, double width, double height, String tone) {
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        iconView.setFitHeight(16);
        iconView.setFitWidth(16);

        Label textLabel = new Label(label);

        HBox hbox = new HBox(iconView, textLabel);
        hbox.setSpacing(20);
        hbox.setAlignment(Pos.CENTER_LEFT);

        setGraphic(hbox);
        String cssFormat = BasicColors.generateStyle(4, 6, 15, 20);
        setStyle(cssFormat);
        setMinSize(width, height);

        setOnAction(event -> {
            ToneManager.playTone(tone);
            action.run();
        }); // Set the action

        addHoverEffect(this, 10, 0, 300); // Add hover effect to move the button by 10 pixels on the X-axis
    }

    private void addHoverEffect(Button button, double moveByX, double moveByY, double durationMillis) {
        TranslateTransition translateTransition = new TranslateTransition(Duration.millis(durationMillis), button);

        button.setOnMouseEntered(event -> {
            translateTransition.setToX(moveByX);
            translateTransition.setToY(moveByY);
            translateTransition.playFromStart();
        });

        button.setOnMouseExited(event -> {
            translateTransition.setToX(0);
            translateTransition.setToY(0);
            translateTransition.playFromStart();
        });
    }

    public static void applyButtonStyle(Button button) {
        String cssFormat = String.format("-fx-background-color: #%06X; -fx-text-fill: #%06X;", BasicColors.BLUE, BasicColors.WHITE);
        button.setStyle(cssFormat);
    }
}


