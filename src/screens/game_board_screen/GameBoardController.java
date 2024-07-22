package screens.game_board_screen;

import components.CustomPopup;
import components.XOButton;
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
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.text.Text;
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
                borderButton.setOnAction(e -> handleButtonClick(row, col));
                gp.add(borderButton, j, i);
            }
        }
        return gp;
    }

    private void handleButtonClick(int row, int col) {
        if (!gameActive) {
            return;
        }

        Cell cell = cells[row][col];
        if (cell.getPlayer() != null) {
            return;
        }

        Image image = xTurn ? xImage : oImage;
        cell.setPlayer(xTurn ? "X" : "O", image);

        int[][] winningCells = winChecker.checkWin(row, col, xTurn ? "X" : "O");

        if (winningCells != null) {
            drawWinningLine(winningCells);
            scoreManager.updateScore(xTurn ? "X" : "O");
            gameActive = false;
            String winnerName = xTurn ? xPlayer.getName() : (oPlayer != null ? oPlayer.getName() : "Ai");
            showVideoPopUp(winnerName, AppConstants.winVideoPath);
        } else if (isBoardFull()) {
            gameActive = false;
            showVideoPopUp("No One", AppConstants.drawVideoPath);
        } else {
            xTurn = !xTurn;
            if (!xTurn && oPlayer == null && gameActive) {
                if (AIPlayer.getMoveCount() < 4) {
                    int[] aiMove = aiPlayer.getMove(aiDifficulty);

                    handleButtonClick(aiMove[0], aiMove[1]);
                }
            }
        }
    }

    private boolean isBoardFull() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getPlayer() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    private void drawWinningLine(int[][] winningCells) {
        // Create a new Line instance
        winLine = new Line();

        // Get the position of the first and last winning cells
        Button startButton = cells[winningCells[0][0]][winningCells[0][1]].getButton();
        Button endButton = cells[winningCells[2][0]][winningCells[2][1]].getButton();

        Bounds startBounds = startButton.localToScene(startButton.getBoundsInLocal());
        Bounds endBounds = endButton.localToScene(endButton.getBoundsInLocal());

        double x1 = startBounds.getMinX() + (startBounds.getWidth() / 2);
        double y1 = startBounds.getMinY() + (startBounds.getHeight() / 2);
        double x2 = endBounds.getMinX() + (endBounds.getWidth() / 2);
        double y2 = endBounds.getMinY() + (endBounds.getHeight() / 2);

        winLine.setStartX(x1);
        winLine.setStartY(y1);
        winLine.setEndX(x2);
        winLine.setEndY(y2);

        winLine.setStroke(Color.GRAY);
        winLine.setStrokeWidth(5);
        winLine.getStrokeDashArray().addAll(10.0, 10.0);

        AnchorPane.getChildren().add(winLine);
    }

    private void resetGame() {
        xTurn = true;
        gameActive = true;
        AIPlayer.resetMoveCount();
        for (int i = 0; i <= 2; i++) {
            for (int j = 0; j <= 2; j++) {
                cells[i][j].getButton().setGraphic(null);
                cells[i][j].setPlayer(null, null);
            }
        }

        if (winLine != null) {
            AnchorPane.getChildren().remove(winLine);
            winLine = null; // Clear the reference
        }
        if (!xTurn && oPlayer == null) { // AI starts
            int[] aiMove = aiPlayer.getMove(aiDifficulty);
            handleButtonClick(aiMove[0], aiMove[1]);
        }
    }

    private void recordGame() {

    }

    private void resignGame() {
        Text areUSure = new Text("Are you Sure");
        areUSure.setFont(Font.font("", FontWeight.BOLD, 24));
        areUSure.setFill(Color.GREY);
        areUSure.setTextAlignment(TextAlignment.CENTER);
        CustomPopup cp = new CustomPopup("Resign", 130, 350, true);
        cp.addContent(areUSure);
        cp.addContent(new XOButton("Yes",
                () -> {
                    cp.close();
                    navigateGameModeScreen();
                },
                AppConstants.oIconPath,
                140,
                40,
                AppConstants.buttonClickedTonePath));
        cp.addCancelButton("No");
        cp.show();

    }

    private void configurePlayerBoxes() {
        xPlayerColumn.setSpacing(20);
        xPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(xPlayer.getName()), UiUtils.createPlayerImage(AppConstants.xIconPath));
        oPlayerColumn.setSpacing(20);
        oPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(oPlayer != null ? oPlayer.getName() : "AI"), UiUtils.createPlayerImage(AppConstants.oIconPath));

    }

    private void navigateGameModeScreen() {
        TicTacToeGame.changeRoot(AppConstants.gameModePath);
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

Name: Minimax Algorithm
Description: This is a decision-making algorithm used in game theory and
artificial intelligence. It performs an exhaustive search of all possible moves and their outcomes to choose the best move:
Maximizing Player: Tries to maximize the score.
Minimizing Player: Tries to minimize the score.
The Minimax algorithm evaluates the game tree recursively, considering both the AI's 
and the opponent's possible moves to find the optimal strategy. This makes it an unbeatable opponent in games like Tic-Tac-Toe.
 */
