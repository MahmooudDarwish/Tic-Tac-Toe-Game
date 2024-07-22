package screens.game_board_screen.models;

import javafx.scene.image.Image;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {

    private Cell[][] cells;
    private Image aiImage;
    private String aiSymbol;
    private String opponentSymbol;
    private WinChecker winChecker;
    private static int moveCount;
    private static final int MAX_MOVES = 4;

    public AIPlayer(Cell[][] cells, Image aiImage, String aiSymbol, String opponentSymbol) {
        this.cells = cells;
        this.aiImage = aiImage;
        this.aiSymbol = aiSymbol;
        this.opponentSymbol = opponentSymbol;
        this.winChecker = new WinChecker(cells);
        this.moveCount = 0; // Initialize move count
    }

    public int[] getEasyMove() {
        
        List<int[]> availableMoves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null) {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }

        if (availableMoves.isEmpty()) {
            return null; // Indicate that no move can be made
        }

        return availableMoves.get(new Random().nextInt(availableMoves.size()));
    }

    public int[] getMediumMove() {
        
        // Check if AI can win in the next move
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null && isWinningMove(i, j, aiSymbol)) {
                    return new int[]{i, j};
                }
            }
        }

        // Check if opponent can win in the next move, block it
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null && isWinningMove(i, j, opponentSymbol)) {
                    return new int[]{i, j};
                }
            }
        }

        // Otherwise, pick a random move
        return getEasyMove();
    }

    public int[] getHardMove() {
        int[] bestMove = findBestMove();
        return bestMove;
    }

    public int[] getMove(String difficulty) {
        int[] move;
        switch (difficulty) {
            case "hard":
                move = getHardMove();
                break;
            case "medium":
                move = getMediumMove();
                break;
            case "easy":
            default:
                move = getEasyMove();
                break;
        }

        moveCount++; // Increment move counter after a successful move
        return move;
    }

    private int[] findBestMove() {
        int bestVal = Integer.MIN_VALUE;
        int[] bestMove = new int[]{-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null) {
                    cells[i][j].setPlayer(aiSymbol, aiImage);
                    int moveVal = minimax(0, false);
                    cells[i][j].setPlayer(null, null);
                    if (moveVal > bestVal) {
                        bestMove[0] = i;
                        bestMove[1] = j;
                        bestVal = moveVal;
                    }
                }
            }
        }

        // Return -1,-1 if no valid move is found
        if (bestMove[0] == -1) {
            return null;
        }

        return bestMove;
    }

    private int minimax(int depth, boolean isMax) {
        String result = checkWinner();
        if (result != null) {
            if (result.equals(aiSymbol)) {
                return 10 - depth;
            } else if (result.equals(opponentSymbol)) {
                return depth - 10;
            } else {
                return 0;
            }
        }

        if (isMax) {
            int best = Integer.MIN_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cells[i][j].getPlayer() == null) {
                        cells[i][j].setPlayer(aiSymbol, aiImage);
                        best = Math.max(best, minimax(depth + 1, !isMax));
                        cells[i][j].setPlayer(null, null);
                    }
                }
            }
            return best;
        } else {
            int best = Integer.MAX_VALUE;
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    if (cells[i][j].getPlayer() == null) {
                        cells[i][j].setPlayer(opponentSymbol, null);
                        best = Math.min(best, minimax(depth + 1, !isMax));
                        cells[i][j].setPlayer(null, null);
                    }
                }
            }
            return best;
        }
    }

    private String checkWinner() {
        for (int i = 0; i < 3; i++) {
            if (cells[i][0].getPlayer() != null
                    && cells[i][0].getPlayer().equals(cells[i][1].getPlayer())
                    && cells[i][1].getPlayer().equals(cells[i][2].getPlayer())) {
                return cells[i][0].getPlayer();
            }
        }

        for (int j = 0; j < 3; j++) {
            if (cells[0][j].getPlayer() != null
                    && cells[0][j].getPlayer().equals(cells[1][j].getPlayer())
                    && cells[1][j].getPlayer().equals(cells[2][j].getPlayer())) {
                return cells[0][j].getPlayer();
            }
        }

        if (cells[0][0].getPlayer() != null
                && cells[0][0].getPlayer().equals(cells[1][1].getPlayer())
                && cells[1][1].getPlayer().equals(cells[2][2].getPlayer())) {
            return cells[0][0].getPlayer();
        }

        if (cells[0][2].getPlayer() != null
                && cells[0][2].getPlayer().equals(cells[1][1].getPlayer())
                && cells[1][1].getPlayer().equals(cells[2][0].getPlayer())) {
            return cells[0][2].getPlayer();
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (cells[i][j].getPlayer() == null) {
                    return null;
                }
            }
        }

        return "draw";
    }

    private boolean isWinningMove(int row, int col, String player) {
        cells[row][col].setPlayer(player, null);
        boolean isWin = winChecker.checkWin(row, col, player) != null;
        cells[row][col].setPlayer(null, null);
        return isWin;
    }
    
    public static void resetMoveCount() {
        moveCount = 0;
    }
    public static int getMoveCount() {
        return moveCount ;
    }
}
