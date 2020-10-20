package ca.sfu.cmpt276.apple_mine_seeker.model;

// Stores game's state, high scores and games played.

public class Game {

    // Default board size 4 x 6 with 6 mines
    private int numMines = 6;
    private int minesFound = 0;
    private int numRows = 4;
    private int numCols = 6;
    private int numScansUsed = 0;
    private int totalGames = 0;

    // Array of size 12 for 12 possible configurations
    private int[] bestScores = {9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999, 9999};

    public int getNumMines() {
        return numMines;
    }

    public void setNumMines(int mines) {
        this.numMines = mines;
    }

    public int getMinesFound() {
        return minesFound;
    }

    public void setMinesFound(int minesFound) {
        this.minesFound = minesFound;
    }

    public int getNumRows() {
        return numRows;
    }

    public void setNumRows(int numRows) {
        this.numRows = numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public void setNumCols(int numCols) {
        this.numCols = numCols;
    }

    public int getNumScansUsed() {
        return numScansUsed;
    }

    public void setNumScansUsed(int numScansUsed) {
        this.numScansUsed = numScansUsed;
    }

    public int getTotalGames() {
        return totalGames;
    }

    public void setTotalGames(int totalGames) {
        this.totalGames = totalGames;
    }

    public void foundMine() {
        this.minesFound++;
    }

    public void scan() {
        this.numScansUsed++;
    }

    public void increaseTotalGames() {
        this.totalGames++;
    }

    public int[] getBestScores() {
        return bestScores;
    }

    public void setBestScores(int[] bestScores) {
        this.bestScores = bestScores;
    }

    public int getBestScoreByConfigIndex(int index) {
        return bestScores[index];
    }

    public void setBestScoreByConfigIndex(int index, int score) {
        bestScores[index] = score;
    }

    // Singleton Support
    private static Game instance;

    private Game() {
        // Private to prevent anyone else from instantiating
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }
        return instance;
    }
}
