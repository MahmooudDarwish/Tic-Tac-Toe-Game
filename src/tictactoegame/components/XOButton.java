package tictactoegame.components;

import java.awt.event.ActionListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;


public class XOButton extends Button {
    
    public XOButton(String label, Runnable action, String iconPath) {
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        iconView.setFitHeight(16);
        iconView.setFitWidth(16);

        Label textLabel = new Label(label); // The button text

        HBox hbox = new HBox(iconView, textLabel); // Combine icon and text
        hbox.setSpacing(20); // Spacing between icon and text
        hbox.setAlignment(Pos.CENTER_LEFT); // Align content to the left


        setGraphic(hbox); // Set the HBox as the button's graphic
        setStyle("-fx-background-color: white;"
                + " -fx-border-color: grey;"
                + " -fx-border-width: 2px;"
                + " -fx-border-radius: 10px; "
                + "-fx-background-radius: 10px; "
                + " -fx-font-size: 16px;"
                + " -fx-text-fill: black;"
        ); // Button style with rounded corners
        setMinSize(200, 40); // Minimum size of the button
        setOnAction(event -> action.run()); // Set the action
    }
}
