/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Mohammed
 */
public class XOLabel extends HBox {

    private final Label label;

    public XOLabel(String iconPath, String text, double width, double height, boolean isVisible) {
        // Load the icon image
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        iconView.setFitHeight(20); // Set icon height
        iconView.setFitWidth(20);  // Set icon width

        // Create and style the label
        label = new Label(text);
        setVisible(isVisible);
        
        
        label.setStyle("-fx-padding: 10;"
                + "-fx-font-weight: bold;" // Make text bold
                + "-fx-font-size: 16px;"
                + "-fx-text-fill: red;" // Set text color to red
        );
        setMinSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);
        setPadding(new Insets(5, 10, 5, 10)); // Set padding for the label

        // Configure the HBox
        setSpacing(10); // Spacing between icon and text
        setAlignment(Pos.CENTER_LEFT); // Align content to the left
        getChildren().addAll(iconView, label);
    }

    // Method to set the text of the label
    public void setText(String text) {
        label.setText(text);
    }

    public void setLabeStyle(String style) {
        label.setStyle("-fx-padding: 10;"
                + "-fx-font-weight: bold;" // Make text bold
                + "-fx-font-size: 20px;"
                +style);
    }

    // Method to get the text of the label
    public String getText() {
        return label.getText();
    }

    public void setLabelVisible(boolean isVisible) {
        setVisible(isVisible);
    }
}
