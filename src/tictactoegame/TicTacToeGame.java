/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoegame;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

/**
 *
 * @author Mahmoud
 */
public class TicTacToeGame extends Application {
    

    private static Stage primaryStage;
            
    @Override
    public void start(Stage stage) throws Exception {


        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/screens/connection_mode_screen/ConnectionModeScreen.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
      public static void changeRoot(String fxmlFile)  {
         try {
            Parent newRoot = FXMLLoader.load(TicTacToeGame.class.getResource(fxmlFile));
            primaryStage.getScene().setRoot(newRoot);
        } catch (IOException ex) {
            Logger.getLogger(TicTacToeGame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

  

  

}
