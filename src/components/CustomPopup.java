package components;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import utils.constants.AppConstants;

public class CustomPopup extends Stage {

    private ArrayList<Object> content = new ArrayList<>();
    private VBox vbox;

    public CustomPopup(String title, double height, double width) {

        XOButton cancelButton = new XOButton(
                "Cancel",
                this::cancelAction,
                AppConstants.xIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);

        setTitle(title);
        vbox = new VBox(20);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(vbox, width, height);
        setResizable(false);
        setScene(scene);

        initModality(Modality.APPLICATION_MODAL);
    }

    public void addContent(Object object) {
        content.add(object);
        vbox.getChildren().add((Node) object);
    }

    public void addCancelButton(String label) {
        if (!content.isEmpty()) {
            Object lastElement = content.get(content.size() - 1);
            if (lastElement instanceof javafx.scene.Node) {
                HBox hBox = new HBox(20);
                hBox.setAlignment(Pos.CENTER);
                hBox.getChildren().add((javafx.scene.Node) lastElement);

                XOButton cancelButton = new XOButton(
                        label,
                        this::cancelAction,
                        AppConstants.xIconPath,
                        140,
                        40,
                        AppConstants.buttonClickedTonePath);

                hBox.getChildren().add(cancelButton);
                vbox.getChildren().remove(lastElement); 
                vbox.getChildren().add(hBox); 
            }
        }
    }

    private void cancelAction() {
        close();
    }
}
