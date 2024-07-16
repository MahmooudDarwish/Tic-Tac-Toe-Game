package tictactoegame;

import java.awt.event.ActionListener;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import tictactoegame.components.XOButton;
import utils.constants.AppConstants;


/**
 * FXML Controller class.
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private VBox buttonContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    
        // Create the custom buttons and add them to the VBox
        XOButton vsComputerBtn = new XOButton("Vs Computer", () -> handleVsComputerAction(), AppConstants.xIconPath );
        XOButton vsFriendBtn = new XOButton("Vs Friend", () -> handleVsFriendAction(), AppConstants.oIconPath);
        
        buttonContainer.getChildren().addAll(vsComputerBtn, vsFriendBtn);
    }
    
    private void handleVsComputerAction() {
        System.out.println("Show Vs Computer alert dialog");
    }
    
    private void handleVsFriendAction() {
        System.out.println("Show Vs Friend alert dialog");
    }
}
