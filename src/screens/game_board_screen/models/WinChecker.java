package screens.game_board_screen.models;

public class WinChecker {

    private Cell[][] cells;

    public WinChecker(Cell[][] cells) {
        this.cells = cells;
    }

    public int[][] checkWin(int row, int col, String player) {
        // Check for win conditions
        if (checkRow(row, player)) {
            return new int[][]{{row, 0}, {row, 1}, {row, 2}};
        }
        if (checkColumn(col, player)) {
            return new int[][]{{0, col}, {1, col}, {2, col}};
        }
        if (checkDiagonalLeft(player)) {
            return new int[][]{{0, 0}, {1, 1}, {2, 2}};
        }
        if (checkDiagonalRight(player)) {
            return new int[][]{{0, 2}, {1, 1}, {2, 0}};
        }

        return null;
    }

    private boolean checkRow(int row, String player) {
        return isCellPlayer(cells[row][0], player)
                && isCellPlayer(cells[row][1], player)
                && isCellPlayer(cells[row][2], player);
    }

    private boolean checkColumn(int col, String player) {
        return isCellPlayer(cells[0][col], player)
                && isCellPlayer(cells[1][col], player)
                && isCellPlayer(cells[2][col], player);
    }

    private boolean checkDiagonalLeft(String player) {
        return isCellPlayer(cells[0][0], player)
                && isCellPlayer(cells[1][1], player)
                && isCellPlayer(cells[2][2], player);
    }

    private boolean checkDiagonalRight(String player) {
        return isCellPlayer(cells[0][2], player)
                && isCellPlayer(cells[1][1], player)
                && isCellPlayer(cells[2][0], player);
    }

    private boolean isCellPlayer(Cell cell, String player) {
        return cell != null && player.equals(cell.getPlayer());
    }
}
