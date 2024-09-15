package screens.online_game_board_screen;

import components.CustomPopup;
import components.XOButton;
import components.XOTextField;
import handlingplayerrequests.HandelPlayerMoveInterface;
import handlingplayerrequests.PlayerRequestHandler;
import java.io.File;
import java.io.IOException;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import screens.game_board_screen.models.ScoreManager;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import javafx.util.Duration;
import models.OnlineLoginPlayerHolder;
import models.Player;
import models.OnlinePlayerHolder;
import models.Response;
import tictactoegame.TicTacToeGame;
import utils.constants.BasicColors;
import utils.game_file_manager.GameFileManager;
import utils.helpers.ToneManager;
import utils.jsonutil.JsonSender;
import utils.jsonutil.JsonUtil;

public class OnlineGameBoardScreenController implements Initializable, HandelPlayerMoveInterface {

    private boolean xTurn;
    private boolean turn;

    private boolean gameActive;
    private Player xPlayer;
    private Player oPlayer;
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
    private GameFileManager gameFileManager = new GameFileManager();
    private static boolean isRecordOn = false;
    private static boolean isRecordabale = true;
    private int[][] winningCells;
    OnlineLoginPlayerHolder onlineLoginPlayerHolder;
    private PlayerRequestHandler requestHandler;

    @Override
    public void initialize(URL url, ResourceBundle rb) {


        OnlinePlayerHolder onlinePlayerHolder = OnlinePlayerHolder.getInstance();
        onlineLoginPlayerHolder = OnlineLoginPlayerHolder.getInstance();
        

        requestHandler = new PlayerRequestHandler(null, null, null, this);

        xPlayer = onlinePlayerHolder.getXPlayer();
        oPlayer = onlinePlayerHolder.getOPlayer();

        System.out.println("=====================================================");
        System.out.println(xPlayer.getUserName());
        System.out.println(oPlayer.getUserName());
        System.out.println("=====================================================");

        xTurn = xPlayer.getUserName().equals(onlineLoginPlayerHolder.getPlayer().getUserName());
        turn = xPlayer.getUserName().equals(onlineLoginPlayerHolder.getPlayer().getUserName());
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

        UiUtils.setRecordBtuText("Record");

       

    }

    private void startGameRecord(String moveData) {
        try {
            // Log the incoming move data
            System.out.println("Received move data: " + moveData);

            // Clean and parse the move data
            moveData = moveData.replace("move", "").trim();
            String[] parts = moveData.split(",");

            // Parse row and column indices and the player's move
            int rowIndex = Integer.parseInt(parts[0].trim());
            int colIndex = Integer.parseInt(parts[1].trim());
            String move = parts[2].trim();

            System.out.println("Parsed move data: rowIndex=" + rowIndex + ", colIndex=" + colIndex + ", move=" + move);

            // Validate the parsed indices
            if (rowIndex >= 0 && rowIndex < 3 && colIndex >= 0 && colIndex < 3) {
                Cell cell = cells[rowIndex][colIndex];

                if (cell != null) {
                    // Set the player's move on the cell
                    Image image = "X".equals(move) ? xImage : oImage;
                    cell.setPlayer(move, image);

                    System.out.println("Player " + move + " moved to cell (" + rowIndex + ", " + colIndex + ")");

                    // Check for a winning condition
                    winningCells = winChecker.checkWin(rowIndex, colIndex, move);
                    if (winningCells != null) {
                        drawWinningLine(winningCells);
                        System.out.println("Winning condition met.");
                    } else {
                        System.out.println("No winning condition met.");
                    }
                } else {
                    System.err.println("Cell is null at (" + rowIndex + ", " + colIndex + ")");
                }
            } else {
                System.err.println("Invalid indices: (" + rowIndex + ", " + colIndex + ")");
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format in move data: " + moveData + ". Error: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Move data out of bounds: " + moveData + ". Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error processing move data: " + e.getMessage());
        }
    }

    private void handleButtonClick(int row, int col) {
        if (!gameActive) {
            System.out.println("handleButtonClick - Game is not active.");
            return;
        }

        ToneManager.playTone(AppConstants.buttonClickedTonePath1);

        Cell cell = cells[row][col];
        if (cell.getPlayer() != null) {
            System.out.println("handleButtonClick - Cell (" + row + ", " + col + ") is already occupied.");
            return;
        }

        Image image = xTurn ? xImage : oImage;
        cell.setPlayer(xTurn ? "X" : "O", image);
        System.out.println("handleButtonClick - Player " + (xTurn ? "X" : "O") + " moved to cell (" + row + ", " + col + ")");

        sendMoveToServer(row, col);
    
        
        isRecordabale = false;
        if (!isRecordabale) {
            UiUtils.setRecordBtuStatus(!isRecordabale);
            System.out.println("handleButtonClick - Record button status set to disabled.");
        }

        if (isRecordOn) {
            gameFileManager.recordEvent(row, col, xTurn ? "X" : "O");
            System.out.println("handleButtonClick - Event recorded for player " + (xTurn ? "X" : "O"));
        }

        winningCells = winChecker.checkWin(row, col, xTurn ? "X" : "O");

        if (winningCells != null) {
            drawWinningLine(winningCells);
            scoreManager.updateScore(xTurn ? "X" : "O");
            gameActive = false;
            System.out.println("handleButtonClick - Winning condition met. Game over.");

            String winnerName = xTurn ? xPlayer.getUserName() : oPlayer.getUserName();

            stopRecording();

            if (xPlayer.getUserName().equals(winnerName) && xTurn) {
                showVideoPopUp(winnerName, AppConstants.winVideoPath);
            } else if (oPlayer.getUserName().equals(winnerName) && !xTurn) {
                showVideoPopUp(winnerName, AppConstants.winVideoPath);
            } else if (oPlayer.getUserName().equals(winnerName) && xTurn) {
                showVideoPopUp(winnerName, AppConstants.loseVideoPath);
            } else {
                showVideoPopUp(winnerName, AppConstants.loseVideoPath);

            }
        } else if (isBoardFull()) {
            gameActive = false;
            System.out.println("handleButtonClick - Board is full. Game over with no winner.");
            showVideoPopUp("No One", AppConstants.drawVideoPath);
            stopRecording();
        }
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
                String cssFormat = String.format("-fx-background-color: #%06X; -fx-text-fill: #%06X;", BasicColors.BLUE, BasicColors.WHITE);
                borderButton.setStyle(cssFormat);
                final int row = i;
                final int col = j;
                borderButton.setOnAction(e -> handleButtonClick(row, col));
                gp.add(borderButton, j, i);
            }
        }

        return gp;
    }

    private void sendMoveToServer(int row, int col) {
        Player onlinePlayer = new Player();
        onlinePlayer.setUserName(xTurn ? oPlayer.getUserName() : xPlayer.getUserName());
        String moveDecider = row + "," + col + "," + (xTurn ? "X" : "O");
        requestHandler.sendMove(moveDecider, onlinePlayer.getUserName());
        
        

    }

    private void disableGameButtons() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                cells[i][j].getButton().setDisable(true);
            }
        }
    }

    private void enableGameButtons() {
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[i].length; j++) {
                if (cells[i][j].getPlayer() == null) { // Enable only empty cells
                    cells[i][j].getButton().setDisable(false);
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
        isRecordabale = true;
        xTurn = true;
        gameActive = true;
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

    }

    private void recordGame() {
        //start recording
        isRecordOn = true;
        UiUtils.setRecordBtuText("Recording...");
        UiUtils.setRecordBtuStatus(isRecordOn);
        gameFileManager.startRecordingGame();
    }

    private void resignGame() {
        // System.out.println("heree***** "+onlineLoginPlayerHolder.getServerMessage().);
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
                    //End Recording
                    stopRecording();
                    //delete un compleate recorde
                    gameFileManager.deleteRecordedFile();
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
        xPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(xPlayer.getUserName()), UiUtils.createPlayerImage(AppConstants.xIconPath));
        oPlayerColumn.setSpacing(20);
        oPlayerColumn.getChildren().addAll(UiUtils.createPlayerText(oPlayer != null ? oPlayer.getUserName() : "AI"), UiUtils.createPlayerImage(AppConstants.oIconPath));

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

    private void stopRecording() {

        isRecordOn = false;
        UiUtils.setRecordBtuText("Record");
        UiUtils.setRecordBtuStatus(isRecordOn);
        gameFileManager.endRecordingGame();

    }
    // Method to update the current player's turn display



    @Override
    public void setPlayerMove(String move) {

        System.out.println("=================================" + move);
        if (move != null) {
            Platform.runLater(() -> startGameRecord(move));
        }
        

    }

}
