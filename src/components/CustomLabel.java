package components;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class CustomLabel extends HBox {

    private final Label textLabel;

    public CustomLabel(String text, String imagePath) {
        ImageView imageView = new ImageView(new Image(imagePath));
        imageView.setFitWidth(30);  
        imageView.setFitHeight(30);
        
        textLabel = new Label(text);
        textLabel.setStyle("-fx-padding: 10;");
       
        
        getChildren().addAll(imageView, textLabel);
        setSpacing(10);
        setPadding(new Insets(10));
        setMinSize(140, 50);
        setStyle("-fx-background-color: white;"
                + " -fx-border-color: transparent grey grey grey;"
                + " -fx-border-width: 2;"
                + " -fx-background-radius: 25;"
                + " -fx-border-radius: 0 0 25 25;");
    }


}
