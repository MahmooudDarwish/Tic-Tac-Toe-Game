/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.playing_screen;

import components.XOButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import utils.constants.AppConstants;

/**
 * FXML Controller class
 *
 * @author Smart
 */
public class PlayingScreenController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    AnchorPane AnchorPane;
    
  
    boolean flag;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         flag=true;
        GridPane gp=new GridPane();
      
       for(int i=0;i<=2;i++)
           for(int j=0;j<=2;j++){
           ImageView x=new ImageView(AppConstants.xIconPath);
           x.setFitHeight(50);
            x.setFitWidth(50);
            ImageView o=new ImageView(AppConstants.oIconPath);
           o.setFitHeight(50);
            o.setFitWidth(50);
           Button b=new Button();
           b.setBorder(new Border(new BorderStroke(Color.BLACK,BorderStrokeStyle.SOLID,CornerRadii.EMPTY,BorderWidths.DEFAULT)));
           b.setPrefSize(100, 100);
           b.setBackground(new Background(new BackgroundFill(Color.WHITE,CornerRadii.EMPTY,Insets.EMPTY)));
           b.setContentDisplay(ContentDisplay.CENTER);
          gp.add(b, j, i);
           
           
            b.setOnAction( e->{ 
            if(flag){ flag=false; b.setGraphic(x); }
            else{flag=true;   b.setGraphic(o);}
            });
           }
       gp.setLayoutX(40);
       gp.setLayoutY(60);
       XOButton record = new XOButton("Record", () -> {}, AppConstants.xIconPath,200,40 );
       record.setLayoutX(20);
       record.setLayoutY(10);
XOButton resign = new XOButton("Resign", () -> {}, AppConstants.oIconPath,200,40 );
       resign.setLayoutX(250);
       resign.setLayoutY(10);
       Text player1=new Text("Player1 name: \n his Score:");
       player1.setLayoutX(400);
       player1.setLayoutY(100);
       player1.setFont(Font.font("", FontWeight.BOLD, 16));
       Text player2=new Text("Player2 name: \n his Score:");
       player2.setLayoutX(400);
       player2.setLayoutY(200);
       player2.setFont(Font.font("", FontWeight.BOLD, 16));
       AnchorPane.getChildren().addAll(gp,record,resign,player1,player2);
        // TODO
    }    
    
}
