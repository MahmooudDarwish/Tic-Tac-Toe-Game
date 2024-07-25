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

/**
 *
 * @author Mohammed
 */
public class XOLabel extends HBox {

    private final Label label;

    public XOLabel(String iconPath, String text, double width, double height, boolean isVisible) {
        // Load the icon image
        ImageView iconView = new ImageView(new Image(getClass().getResourceAsStream(iconPath)));
        iconView.setFitHeight(20);
        iconView.setFitWidth(20);

        // Create and style the label
        label = new Label(text);
        setVisible(isVisible);

        label.setStyle("-fx-padding: 10;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 16px;"
                + "-fx-text-fill: black;"
        );
        setMinSize(width, height);
        setPrefSize(width, height);
        setMaxSize(width, height);
        setPadding(new Insets(5, 10, 5, 10));

        setSpacing(10);
        setAlignment(Pos.CENTER_LEFT);
        getChildren().addAll(iconView, label);
    }

    public void setText(String text) {
        label.setText(text);
    }

    public void setLabeStyle(String style) {
        label.setStyle("-fx-padding: 10;"
                + "-fx-font-weight: bold;"
                + "-fx-font-size: 20px;"
                + style);
    }
    public String getText() {
        return label.getText();
    }

    public void setLabelVisible(boolean isVisible) {
        setVisible(isVisible);
    }
}
