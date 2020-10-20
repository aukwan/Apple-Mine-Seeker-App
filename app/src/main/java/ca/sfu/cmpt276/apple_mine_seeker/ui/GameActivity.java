package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.Random;

import ca.sfu.cmpt276.apple_mine_seeker.R;
import ca.sfu.cmpt276.apple_mine_seeker.model.Game;

public class GameActivity extends AppCompatActivity {

    private Game game = Game.getInstance();
    private int gameRows = game.getNumRows();
    private int gameCols = game.getNumCols();
    private int totalMines = game.getNumMines();

    private boolean[][] hasMine = new boolean[gameRows][gameCols];
    Button[][] buttons = new Button[gameRows][gameCols];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        retrieveNumGamesAndScores();
        resetValuesForMinesFoundAndScansUsed();
        populateButtons();
        placeMines();
        setupTextViews();
    }

    private void resetValuesForMinesFoundAndScansUsed() {
        game.setMinesFound(0);
        game.setNumScansUsed(0);
    }

    // Populate table of buttons with images and text
    private void populateButtons() {
        TableLayout table = findViewById(R.id.tableForButtons);

        for (int row = 0; row < gameRows; row++) {
            TableRow tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,
                    TableLayout.LayoutParams.MATCH_PARENT,
                    1.0f));
            table.addView(tableRow);

            for (int col = 0; col < gameCols; col++) {
                final int FINAL_COL = col;
                final int FINAL_ROW = row;
                Button button = new Button(this);
                button.setText("");
                button.setTypeface(null, Typeface.BOLD);
                button.setTextSize(20);
                button.setTextColor(Color.WHITE);
                button.setLayoutParams(new TableRow.LayoutParams(
                        TableRow.LayoutParams.MATCH_PARENT,
                        TableRow.LayoutParams.MATCH_PARENT,
                        1.0f));
                button.setBackgroundResource(R.drawable.apple);
                // Make text not clip on small buttons
                button.setPadding(0,0,0,0);
                button.setOnClickListener(new View.OnClickListener() {
                    private boolean beenScanned = false;
                    @Override
                    public void onClick(View v) {
                        if (!beenScanned) {
                            scanRowAndColAtPoint(FINAL_COL, FINAL_ROW);
                            beenScanned = true;
                            // Allows for a revealed mine to be scanned for other hidden mines
                            if (hasMine[FINAL_ROW][FINAL_COL]) {
                                hasMine[FINAL_ROW][FINAL_COL] = false;
                                beenScanned = false;
                                game.scan();
                            }
                        }
                    }
                });
                tableRow.addView(button);
                buttons[row][col] = button;
            }
        }
    }

    private void setupTextViews() {
        TextView scansUsed = findViewById(R.id.scansUsed);
        String numScansUsed = Integer.toString(game.getNumScansUsed());
        scansUsed.setText(getString(R.string.num_scans) + numScansUsed);

        TextView minesFound = findViewById(R.id.minesFound);
        String totalMines = Integer.toString(game.getNumMines());
        String numMinesFound = Integer.toString(game.getMinesFound());
        minesFound.setText(getString(R.string.found) + numMinesFound + getString(R.string.of) + totalMines + getString(R.string.mines_2));

        TextView gamesPlayed = findViewById(R.id.gamesPlayed);
        String numGamesPlayed = Integer.toString(game.getTotalGames());
        gamesPlayed.setText(getString(R.string.times_played) + numGamesPlayed);
    }

    private void placeMines() {
        // Initialize a 2d boolean array to keep track of where mines are
        for (int row = 0; row < gameRows; row++) {
            for (int col = 0; col < gameCols; col++) {
                hasMine[row][col] = false;
            }
        }
        // Randomly place mines on game board
        for (int i = 0; i < totalMines; i++) {
            Random random = new Random();
            int randomRow = random.nextInt(gameRows);
            int randomCol = random.nextInt(gameCols);
            // If cell already has mine, pick another random cell
            while (hasMine[randomRow][randomCol]) {
                randomRow = random.nextInt(gameRows);
                randomCol = random.nextInt(gameCols);
            }
            hasMine[randomRow][randomCol] = true;
        }
    }

    private void scanRowAndColAtPoint(int currCol, int currRow) {
        TextView scansUsed = findViewById(R.id.scansUsed);
        String numScansUsed = Integer.toString(game.getNumScansUsed());
        scansUsed.setText(getString(R.string.num_scans) + numScansUsed);
        Button button = buttons[currRow][currCol];

        lockButtonSizes();
        // Reveals a mine if one is present
        if (hasMine[currRow][currCol]) {
            game.foundMine();
            button.setBackgroundResource(R.drawable.mine_apple);
            button.setText("");
            TextView minesFound = findViewById(R.id.minesFound);
            String numMinesFound = Integer.toString(game.getMinesFound());
            minesFound.setText(getString(R.string.found) + numMinesFound + getString(R.string.of) + totalMines + getString(R.string.mines_2));

            // Update revealed cells' number of hidden mines in same row and column
            for (int row = 0; row < gameRows; row++) {
                if (!buttons[row][currCol].getText().equals("")) {
                    String currentNumOnBtn = (String) buttons[row][currCol].getText();
                    int updatedNum = Integer.parseInt(currentNumOnBtn);
                    updatedNum--;
                    String updatedNumOnBtn = Integer.toString(updatedNum);
                    buttons[row][currCol].setText(updatedNumOnBtn);
                }
            }

            for (int col = 0; col < gameCols; col++) {
                if (!buttons[currRow][col].getText().equals("")) {
                    String currentNumOnBtn = (String) buttons[currRow][col].getText();
                    int updatedNum = Integer.parseInt(currentNumOnBtn);
                    updatedNum--;
                    String updatedNumOnBtn = Integer.toString(updatedNum);
                    buttons[currRow][col].setText(updatedNumOnBtn);
                }
            }

        // Performs a scan, revealing number of hidden mines in same row and column
        } else {
            game.scan();
            int minesHiddenInRowAndCol = 0;

            for (int row = 0; row < gameRows; row++) {
                if (hasMine[row][currCol]) {
                    minesHiddenInRowAndCol++;
                }
                if (buttons[row][currCol].isSelected()) {
                    minesHiddenInRowAndCol--;
                }
            }

            for (int col = 0; col < gameCols; col++) {
                if (hasMine[currRow][col]) {
                    minesHiddenInRowAndCol++;
                }
                if (buttons[currRow][col].isSelected()) {
                    minesHiddenInRowAndCol--;
                }
            }
            String numMinesOnSameRowAndCol = Integer.toString(minesHiddenInRowAndCol);
            button.setText(numMinesOnSameRowAndCol);
        }
        isGameOver();
    }

    // Lock button sizes so they do not change in size after being pressed
    private void lockButtonSizes() {
        for (int row = 0; row < gameRows; row++) {
            for (int col = 0; col < gameCols; col++) {
                Button button = buttons[row][col];

                int width = button.getWidth();
                button.setMinWidth(width);
                button.setMaxWidth(width);

                int height = button.getHeight();
                button.setMinHeight(height);
                button.setMaxHeight(height);
            }
        }
    }

    // Checks if all mines have been found. If all found, congratulate player.
    private void isGameOver() {
        if (game.getMinesFound() == game.getNumMines()) {
            game.increaseTotalGames();
            // Update all scan to show 0, as there are no more hidden mines
            for (int row = 0; row < gameRows; row++) {
                for (int col = 0; col < gameCols; col++) {
                    if (!buttons[row][col].getText().equals("")) {
                        buttons[row][col].setText("0");
                    }
                }
            }
            // Save game score and games played
            int score = game.getNumScansUsed();
            setBestScore(score);

            saveScoreAndScans();

            // Display congratulations dialog
            FragmentManager manager = getSupportFragmentManager();
            MessageFragment dialog = new MessageFragment();
            dialog.show(manager, "MessageDialog");
        }
    }


    // Save high scores and number of games played through SharedPreferences
    private void saveScoreAndScans() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("Times Played", game.getTotalGames());
        // Code found at https://stackoverflow.com/questions/7175880/how-can-i-store-an-integer-array-in-sharedpreferences
        StringBuilder str = new StringBuilder();
        int bestScoresLength = game.getBestScores().length;
        for (int bestScore = 0; bestScore < bestScoresLength; bestScore++) {
            str.append(game.getBestScoreByConfigIndex(bestScore)).append(",");
        }
        editor.putString("Best Scores", str.toString());
        editor.apply();
    }

    public void retrieveNumGamesAndScores() {
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        int numGames = prefs.getInt("Times Played", 0);
        // Code found at https://stackoverflow.com/questions/7175880/how-can-i-store-an-integer-array-in-sharedpreferences
        String bestScoresString = prefs.getString("Best Scores", "");
        if (!bestScoresString.equals("")) {
            String[] bestScoresSplit = bestScoresString.split("\\,");

            for (int i = 0; i < 12; i++) {
                game.setBestScoreByConfigIndex(i, Integer.parseInt(bestScoresSplit[i]));
            }
        }

        game.setTotalGames(numGames);
        int bestScoreByConfigIndex = 0;
        bestScoreByConfigIndex = getBestScoreByConfigIndex(bestScoreByConfigIndex);

        TextView highScoreForConfig = findViewById(R.id.bestScore);
        highScoreForConfig.setText(getString(R.string.best_score_for)
                + gameRows + getString(R.string.rows_by) + gameCols + getString(R.string.columns)
                + game.getBestScoreByConfigIndex(bestScoreByConfigIndex));

        TextView totalGames = findViewById(R.id.gamesPlayed);
        totalGames.setText(getString(R.string.times_played) + numGames);
    }

    private void setBestScore(int score) {
        // 4 rows by 6 columns with 6 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 6) {
            if (score < game.getBestScoreByConfigIndex(0)) {
                game.setBestScoreByConfigIndex(0, score);
            }
        }
        // 4 rows by 6 columns with 10 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 10) {
            if (score < game.getBestScoreByConfigIndex(1)) {
                game.setBestScoreByConfigIndex(1, score);
            }
        }
        // 4 rows by 6 columns with 15 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 15) {
            if (score < game.getBestScoreByConfigIndex(2)) {
                game.setBestScoreByConfigIndex(2, score);
            }
        }
        // 4 rows by 6 columns with 20 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 20) {
            if (score < game.getBestScoreByConfigIndex(3)) {
                game.setBestScoreByConfigIndex(3, score);
            }
        }
        // 5 rows by 10 columns with 6 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 6) {
            if (score < game.getBestScoreByConfigIndex(4)) {
                game.setBestScoreByConfigIndex(4, score);
            }
        }
        // 5 rows by 10 columns with 10 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 10) {
            if (score < game.getBestScoreByConfigIndex(5)) {
                game.setBestScoreByConfigIndex(5, score);
            }
        }
        // 5 rows by 10 columns with 15 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 15) {
            if (score < game.getBestScoreByConfigIndex(6)) {
                game.setBestScoreByConfigIndex(6, score);
            }
        }
        // 5 rows by 10 columns with 20 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 20) {
            if (score < game.getBestScoreByConfigIndex(7)) {
                game.setBestScoreByConfigIndex(7, score);
            }
        }
        // 6 rows by 15 columns with 6 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 6) {
            if (score < game.getBestScoreByConfigIndex(8)) {
                game.setBestScoreByConfigIndex(8, score);
            }
        }
        // 6 rows by 15 columns with 10 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 10) {
            if (score < game.getBestScoreByConfigIndex(9)) {
                game.setBestScoreByConfigIndex(9, score);
            }
        }
        // 6 rows by 15 columns with 15 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 15) {
            if (score < game.getBestScoreByConfigIndex(10)) {
                game.setBestScoreByConfigIndex(10, score);
            }
        }
        // 6 rows by 15 columns with 20 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 20) {
            if (score < game.getBestScoreByConfigIndex(11)) {
                game.setBestScoreByConfigIndex(11, score);
            }
        }
    }

    private int getBestScoreByConfigIndex(int bestScoreByConfigIndex) {
        if (gameRows == 4 && gameCols == 6 && totalMines == 6) {
            bestScoreByConfigIndex = 0;
        }
        // 4 rows by 6 columns with 10 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 10) {
            bestScoreByConfigIndex = 1;
        }
        // 4 rows by 6 columns with 15 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 15) {
            bestScoreByConfigIndex = 2;
        }
        // 4 rows by 6 columns with 20 mines
        if (gameRows == 4 && gameCols == 6 && totalMines == 20) {
            bestScoreByConfigIndex = 3;
        }
        // 5 rows by 10 columns with 6 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 6) {
            bestScoreByConfigIndex = 4;
        }
        // 5 rows by 10 columns with 10 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 10) {
            bestScoreByConfigIndex = 5;
        }
        // 5 rows by 10 columns with 15 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 15) {
            bestScoreByConfigIndex = 6;
        }
        // 5 rows by 10 columns with 20 mines
        if (gameRows == 5 && gameCols == 10 && totalMines == 20) {
            bestScoreByConfigIndex = 7;
        }
        // 6 rows by 15 columns with 6 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 6) {
            bestScoreByConfigIndex = 8;
        }
        // 6 rows by 15 columns with 10 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 10) {
            bestScoreByConfigIndex = 9;
        }
        // 6 rows by 15 columns with 15 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 15) {
            bestScoreByConfigIndex = 10;
        }
        // 6 rows by 15 columns with 20 mines
        if (gameRows == 6 && gameCols == 15 && totalMines == 20) {
            bestScoreByConfigIndex = 11;
        }
        return bestScoreByConfigIndex;
    }
}