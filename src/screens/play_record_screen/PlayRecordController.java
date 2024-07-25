package screens.play_record_screen;

import components.XOButton;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import models.RecordName;
import models.RecordNameHolder;
import screens.game_board_screen.models.*;
import tictactoegame.TicTacToeGame;
import utils.constants.AppConstants;
import utils.game_file_manager.GameFileManager;

/**
 * FXML Controller class
 *
 * @author Mohammed
 */
public class PlayRecordController implements Initializable {

    private Cell[][] cells;
    private RecordName recordName;
    private List<String> events = new ArrayList<>();
    private GameFileManager gameFileManager = new GameFileManager();
    private Line winLine;
    private WinChecker winChecker;
    private int[][] winningCells;
    private Image xImage;
    private Image oImage;
    private XOButton exitBtn;

    @FXML
    private AnchorPane anchorPane;
  
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Initialize exit button
        exitBtn = new XOButton("Exit",
                this::handleExitButtonAction,
                AppConstants.logoutIconPath,
                200,
                40,
                AppConstants.buttonClickedTonePath);

        // Initialize the record name and load events
        RecordNameHolder recordNameHolder = RecordNameHolder.getInstance();
        recordName = recordNameHolder.getRecordName();
        events = gameFileManager.loadRecordedEvents(recordName.getName());

        // Load images for X and O
        xImage = new Image(AppConstants.xIconPath);
        oImage = new Image(AppConstants.oIconPath);

        // Initialize the cells and game grid
        cells = new Cell[3][3];
        GridPane gameGrid = createGameGrid();
        HBox mainLayout = new HBox(50, gameGrid);
        mainLayout.setAlignment(Pos.CENTER);
        VBox rootLayout = new VBox(20, mainLayout, exitBtn);
        rootLayout.setAlignment(Pos.CENTER);
        rootLayout.setPrefSize(1366, 766);
        anchorPane.getChildren().add(rootLayout);

        // Initialize winChecker with the cells array
        winChecker = new WinChecker(cells);

        // Create and play animation sequence
        SequentialTransition sequentialTransition = new SequentialTransition();

        for (int i = 0; i < events.size(); i++) {
            String fileRow = events.get(i);
            PauseTransition pause = new PauseTransition(Duration.seconds(1));
            pause.setOnFinished(event -> {
                System.out.println("Processing event: " + fileRow);
                startGameRecord(fileRow.charAt(0), fileRow.charAt(2), fileRow.charAt(4));
            });

            sequentialTransition.getChildren().add(pause);
        }

        sequentialTransition.setOnFinished(event -> System.out.println("Playback finished"));
        sequentialTransition.play();
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
                gp.add(borderButton, j, i);
            }
        }
        return gp;
    }

    private void startGameRecord(char row, char col, char moveDecider) {
        try {
            int rowIndex = Character.getNumericValue(row);
            int colIndex = Character.getNumericValue(col);
            String move = String.valueOf(moveDecider);

            if (rowIndex >= 0 && rowIndex < 3 && colIndex >= 0 && colIndex < 3) {
                Cell cell = cells[rowIndex][colIndex];
                if (cell != null) {
                    Image image = move.equals("X") ? xImage : oImage;
                    cell.setPlayer(move, image);

                    winningCells = winChecker.checkWin(rowIndex, colIndex, move);
                    if (winningCells != null) {
                        drawWinningLine(winningCells);
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
            System.err.println("Invalid move data: " + e.getMessage());
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Move out of bounds: " + e.getMessage());
        }
    }

    private void drawWinningLine(int[][] winningCells) {
        if (winningCells == null || winningCells.length < 2) {
            return;
        }

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

        anchorPane.getChildren().add(winLine);
    }

    private void handleExitButtonAction() {
        System.out.println("Navigate to player history screen");
        TicTacToeGame.changeRoot(AppConstants.playerhistoryPath);
    }
}
