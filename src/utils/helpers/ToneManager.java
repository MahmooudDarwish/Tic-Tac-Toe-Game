/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.helpers;

import java.util.HashMap;
import java.util.Map;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/**
 *
 * @author Mahmoud
 */

//This class for avoiding creating new objects of MediaPlayer everytime we click on button 
//No need to create object of this class to use it's functionalities its abstract 
public abstract class ToneManager {
    private static final Map<String, MediaPlayer> toneCache = new HashMap<>();

    public static void playTone(String tonePath) {
        if (!toneCache.containsKey(tonePath)) {
            Media tone = new Media(ToneManager.class.getResource(tonePath).toString());
            MediaPlayer mediaPlayer = new MediaPlayer(tone);
            toneCache.put(tonePath, mediaPlayer);
        }

        MediaPlayer mediaPlayer = toneCache.get(tonePath);
        if (mediaPlayer != null) {
            mediaPlayer.seek(Duration.ZERO); 
            mediaPlayer.play();
        }
    }
}
