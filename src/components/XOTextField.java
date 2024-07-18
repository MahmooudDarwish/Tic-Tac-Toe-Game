/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tictactoegame.components;

import javafx.scene.control.TextField;

/**
 *
 * @author Mohammed
 */
public class XOTextField extends TextField {

    public  XOTextField(String promptText){
     setPromptText(promptText);
     setStyle("-fx-background-color: white;"
                + " -fx-border-color: grey;"
                + " -fx-border-width: 2px;"
                + " -fx-border-radius: 10px; "
                + "-fx-background-radius: 10px; "
                + " -fx-font-size: 16px;"
                + " -fx-text-fill: black;"
        ); // Button style with rounded corners
    }

}
