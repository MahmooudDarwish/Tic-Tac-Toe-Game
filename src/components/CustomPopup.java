package components;
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
import utils.constants.AppConstants;

public class CustomPopup extends Stage {

    public CustomPopup(ArrayList<Object> content, String title, double height, double width) {
        // Set the title
        setTitle(title);
        //create cancel button 
       
        XOButton cancelButton = new XOButton("Cancel", () -> cancelAction() , AppConstants.xIconPath, 140, 40);
        // Create a VBox for layout
        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        // Add content to VBox
        for (Object obj : content) {
            if (obj instanceof javafx.scene.Node) {
                vbox.getChildren().add((javafx.scene.Node) obj);
            } else {
                // If the object is not a JavaFX Node, handle it differently
                vbox.getChildren().add(new Label(obj.toString()));
            }
        }

        // Create an HBox for buttons
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // Add buttons to the HBox
        for (Object obj : content) {
            if (obj instanceof Button) {
                buttonBox.getChildren().add((Button) obj);
            }
        }
        buttonBox.getChildren().add(cancelButton);
        // Add the buttonBox to the VBox
        vbox.getChildren().add(buttonBox);

        // Scene
        Scene scene = new Scene(vbox, width, height);
        setResizable(false);
        setScene(scene);

        // Make the pop-up modal (block interaction with other windows until this one is closed)
        initModality(Modality.APPLICATION_MODAL);
    }
    
     private void cancelAction() {
        close();
    }
    
}
