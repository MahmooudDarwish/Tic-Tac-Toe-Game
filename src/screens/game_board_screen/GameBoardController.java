package screens.game_board_screen;

import components.CustomPopup;
import components.XOButton;
import java.io.File;
import components.XOTextField;
import java.io.File;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import screens.game_board_screen.models.ScoreManager;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import models.OfflinePlayer;
import models.OfflinePlayerHolder;
import screens.game_board_screen.models.Cell;
import screens.game_board_screen.models.UiUtils;
import screens.game_board_screen.models.WinChecker;
import utils.constants.AppConstants;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import screens.ai_mode_screen.AiModeController;
import screens.game_board_screen.models.AIPlayer;
import tictactoegame.TicTacToeGame;

public class GameBoardController implements Initializable {

    private boolean xTurn;
    private boolean gameActive;

    private OfflinePlayer xPlayer;
    private OfflinePlayer oPlayer;

    private Image xImage;
    private Image oImage;

    private Cell[][] cells;
    private WinChecker winChecker;
    private ScoreManager scoreManager;

    @FXML
    private AnchorPane AnchorPane;
    @FXML
    private VBox xPlayerColumn;
    @FXML
    private VBox oPlayerColumn;

    private Line winLine;

    private AIPlayer aiPlayer;
    private String aiDifficulty;
    private int turnCounter = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        OfflinePlayerHolder offlinePlayerHolder = OfflinePlayerHolder.getInstance();
        xPlayer = offlinePlayerHolder.getXPlayer();
        oPlayer = offlinePlayerHolder.getOPlayer();

        xTurn = true;
        gameActive = true;

        xImage = new Image(AppConstants.xIconPath);
        oImage = new Image(AppConstants.oIconPath);

        cells = new Cell[3][3];
        winChecker = new WinChecker(cells);

        Text scoreText = new Text("0 — 0"); // Initialize the scoreText
        scoreManager = new ScoreManager(scoreText);

        VBox scoreBox = UiUtils.createScoreBox(scoreText, this::resignGame, this::recordGame);

        GridPane gameGrid = createGameGrid();
        configurePlayerBoxes();

        if (oPlayer == null) {
            aiPlayer = new AIPlayer(cells, oImage, "O", "X");
            aiDifficulty = AiModeController.getAiMode(); // Default difficulty level
        }

        HBox mainLayout = new HBox(50, xPlayerColumn, gameGrid, oPlayerColumn);
        mainLayout.setAlignment(Pos.CENTER);

        VBox rootLayout = new VBox(20, scoreBox, mainLayout);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPrefSize(1366, 766);

        AnchorPane.getChildren().add(rootLayout);

        // Initialize win line
        winLine = new Line();
        winLine.setStrokeWidth(5);
        winLine.setStroke(Color.RED);
        AnchorPane.getChildren().add(winLine);
    }

    private GridPane createGameGrid() {
        GridPane gp = new GridPane();
        gp.setAlignment(Pos.CENTER);
        gp.setHgap(5);
        gp.setVgap(5);
        gp.setPadding(new Insets(20));

        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                cells[i][j] = new Cell();
                Button borderButton = cells[i][j].getButton();
                final int row = i;
                final int col = j;
                //TODO: handle button click 
                gp.add(borderButton, j, i);
            }
        }
  
        Text xPlayerText = new Text(xPlayer.getName());
        xPlayerText.setFont(Font.font("", FontWeight.BOLD, 24));

        Text oPlayerText = new Text((oPlayer != null ? oPlayer.getName() : "AI"));
        oPlayerText.setFont(Font.font("", FontWeight.BOLD, 24));

        Text scoreText = new Text("2 — 3");
        scoreText.setFont(Font.font("", FontWeight.BOLD, 24));
        scoreText.setFill(Color.GREY);
        return gp;
    }

   
    private void configurePlayerBoxes() {
        xPlayerColumn.setSpacing(20);
        xPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(xPlayer.getName()), UiUtils.createPlayerImage(AppConstants.xIconPath));
        oPlayerColumn.setSpacing(20);
        oPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(oPlayer != null ? oPlayer.getName() : "AI"), UiUtils.createPlayerImage(AppConstants.oIconPath));

    }
    
      private void resignGame() {
        
    }
      
          private void recordGame() {
        
    }

}

/*

Here are the names and descriptions of the algorithms typically used for different difficulty levels in a Tic-Tac-Toe AI:

Easy Algorithm: Random Move

Name: Random Move Algorithm
Description: This algorithm selects a move randomly from the available positions. 
It doesn't consider the current state of the game, making it the easiest level as it can make poor decisions.
Medium Algorithm: Rule-Based Heuristic

Name: Rule-Based Heuristic Algorithm
Description: This algorithm follows a set of simple, predefined rules to decide the next move:
First, check for a winning move.
If no winning move is available, check for a blocking move to prevent the opponent from winning.
If neither is found, choose a random move from the available positions.
This provides a more challenging game than the easy algorithm but is not unbeatable.
Hard Algorithm: Minimax Algorithm

        AnchorPane.getChildren().add(rootLayout);
    }
    private void showVideoPopUp(String winnerName, String videoPath) {

        File videoFile = new File(videoPath);

        Media media = new Media(videoFile.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        // Set the desired width and height for the MediaView
        mediaView.setFitWidth(450);
        mediaView.setFitHeight(300);

        String popUpTitle = winnerName + " Wins";
        Text winnerText = new Text(popUpTitle);

        winnerText.setFont(Font.font("", FontWeight.BOLD, 24));
        winnerText.setFill(Color.GREY);
        winnerText.setTextAlignment(TextAlignment.CENTER);

        CustomPopup popup = new CustomPopup(popUpTitle, 400, 450, false);

        XOButton playAgainButton = new XOButton("Play Again",
                () -> {
                    popup.close();
                    mediaPlayer.dispose();
                    resetGame();
                },
                AppConstants.xIconPath,
                100,
                40,
                AppConstants.buttonClickedTonePath);

        XOButton exitButton = new XOButton("Exit",
                () -> {
                    popup.close();
                    mediaPlayer.dispose();
                    navigateGameModeScreen();
                },
                AppConstants.oIconPath,
                100,
                40,
                AppConstants.buttonClickedTonePath);

        popup.addContent(winnerText);
        popup.addContent(mediaView);
        popup.addContent(playAgainButton);
        popup.addContent(exitButton);

        popup.show();
        mediaPlayer.play();
    }
}

Name: Minimax Algorithm
Description: This is a decision-making algorithm used in game theory and
artificial intelligence. It performs an exhaustive search of all possible moves and their outcomes to choose the best move:
Maximizing Player: Tries to maximize the score.
Minimizing Player: Tries to minimize the score.
The Minimax algorithm evaluates the game tree recursively, considering both the AI's 
and the opponent's possible moves to find the optimal strategy. This makes it an unbeatable opponent in games like Tic-Tac-Toe.
 */
