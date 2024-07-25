/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.lobby_screen_mode;


import components.XOButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Smart
 */
public class LobbyScreenUiController implements Initializable {
    
    @FXML
    AnchorPane anchorPane;

    @FXML
    private ListView<XOButton> list;
   
   @FXML
   private Label availablePlayers;
    
   @Override
    public void initialize(URL url, ResourceBundle rb) {
        anchorPane.setBackground(
                new Background(new BackgroundImage(new Image(AppConstants.bachgroundImagePath), BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT)));
         availablePlayers.setText("All Available Players");
         availablePlayers.setFont(Font.font("", FontWeight.BOLD, 22));
         availablePlayers.setAlignment(Pos.CENTER);
          availablePlayers.setStyle("-fx-background-color: #E3E2DC");
        ObservableList<XOButton> listPlayers = FXCollections.observableArrayList();
        for(int i = 1; i <= 20; i++){
            listPlayers.add(new XOButton("player:  "+i+"       score:    "+2*i,
                    ()->{System.out.print("Navigate to game board screen");}
                    ,AppConstants.xIconPath,80,40,AppConstants.buttonClickedTonePath1));
        }
      
        
        list.setStyle("-fx-background-color: black");
                list.setItems(listPlayers);
        
        
        
        
    }    
    
}
