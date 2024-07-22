package tictactoegame.components;
import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import tictactoegame.components.XOButton;

public class CustomPopup extends Stage {

  public CustomPopup(ArrayList<Object> content, String title) {
        // Set the title
        setTitle(title);

        // Create a VBox for layout
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        // Add content to VBox
        for (Object obj : content) {
            if (obj instanceof javafx.scene.Node) {
                vbox.getChildren().add((javafx.scene.Node) obj);
            } else {
                // If the object is not a JavaFX Node, you can handle it differently
                vbox.getChildren().add(new Label(obj.toString()));
            }
        }

        // Check if the last element is an HBox and contains buttons
        if (!vbox.getChildren().isEmpty()) {
            Object lastElement = vbox.getChildren().get(vbox.getChildren().size() - 1);
            if (lastElement instanceof HBox) {
                HBox hbox = (HBox) lastElement;
                for (Object node : hbox.getChildren()) {
                    if (node instanceof Button) {
                        System.out.println("HBox contains a Button");
                    }
                }
            }
        }

        // Scene
        Scene scene = new Scene(vbox, 300, 200);
        setScene(scene);

        // Make the pop-up modal (block interaction with other windows until this one is closed)
        initModality(Modality.APPLICATION_MODAL);
    }
}