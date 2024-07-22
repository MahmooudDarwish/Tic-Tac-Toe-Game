/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package screens.game_board_screen.models;

/**
 *
 * @author Mahmoud
 */
import javafx.scene.text.Text;

public class ScoreManager {

    private Text scoreText;
    private int xScore;
    private int oScore;

    public ScoreManager(Text scoreText) {
        this.scoreText = scoreText;
        this.xScore = 0;
        this.oScore = 0;
        updateScoreText();
    }

    public void updateScore(String winner) {
        if (winner.equals("X")) {
            xScore++;
        } else if (winner.equals("O")) {
            oScore++;
        }
        updateScoreText();
    }

    private void updateScoreText() {
        scoreText.setText(xScore + " — " + oScore);
    }
}
