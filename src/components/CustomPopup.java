package components;

import java.util.ArrayList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import utils.constants.AppConstants;

public class CustomPopup extends Stage {

    private ArrayList<Object> content = new ArrayList<>();
    private VBox mainVBox;
    private HBox buttonBox;
    private double offsetX;
    private double offsetY;

    public CustomPopup(String title, double height, double width, boolean isClosable) {

        setTitle(title);
        mainVBox = new VBox(20);
        mainVBox.setPadding(new Insets(20));
        mainVBox.setAlignment(Pos.TOP_CENTER);

        buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().add(buttonBox);

        Scene scene = new Scene(mainVBox, width, height);
        scene.setFill(null); // Make the scene background transparent
        setResizable(false);
        setScene(scene);

        if (isClosable == false) {
            setOnCloseRequest(event -> event.consume());
            initStyle(StageStyle.UNDECORATED);
        }

        initModality(Modality.APPLICATION_MODAL);
    }

    public void addContent(Object object) {
        if (object instanceof Button) {
            buttonBox.getChildren().add((Node) object);
        } else {
            content.add(object);
            // Add content before the buttonBox to ensure buttonBox stays at the bottom
            mainVBox.getChildren().add(mainVBox.getChildren().size()-1, (Node) object);
        }
    }

    public void addCancelButton(String label) {
        XOButton cancelButton = new XOButton(
                label,
                this::cancelAction,
                AppConstants.xIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath);

        buttonBox.getChildren().add(cancelButton);
    }

    private void cancelAction() {
        close();
    }
}
