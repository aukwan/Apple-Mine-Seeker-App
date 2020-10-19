package ca.sfu.cmpt276.apple_mine_seeker.ui;

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

        resetValues();
        populateButtons();
        placeMines();
        setupTextViews();
    }

    private void resetValues() {
        game.setMinesFound(0);
        game.setNumScansUsed(0);
    }

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
        scansUsed.setText("# Scans used: " + numScansUsed);

        TextView minesFound = findViewById(R.id.minesFound);
        String totalMines = Integer.toString(game.getNumMines());
        String numMinesFound = Integer.toString(game.getMinesFound());
        minesFound.setText("Found " + numMinesFound + " of " + totalMines + " Mines");

        TextView gamesPlayed = findViewById(R.id.gamesPlayed);
        String numGamesPlayed = Integer.toString(game.getTotalGames());
        gamesPlayed.setText("Times Played: " + numGamesPlayed);
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
        scansUsed.setText("# Scans used: " + numScansUsed);
        Button button = buttons[currRow][currCol];

        lockButtonSizes();
        // Reveals a mine if one is present
        if (hasMine[currRow][currCol]) {
            game.foundMine();
            button.setBackgroundResource(R.drawable.mine_apple);
            button.setText("");
            TextView minesFound = findViewById(R.id.minesFound);
            String numMinesFound = Integer.toString(game.getMinesFound());
            minesFound.setText("Found " + numMinesFound + " of " + totalMines + " Mines");

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
            // Update all scan to show 0, as there are no more hidden mines
            for (int row = 0; row < gameRows; row++) {
                for (int col = 0; col < gameCols; col++) {
                    if (!buttons[row][col].getText().equals("")) {
                        buttons[row][col].setText("0");
                    }
                }
            }
            // Display congratulations dialog
            FragmentManager manager = getSupportFragmentManager();
            MessageFragment dialog = new MessageFragment();
            dialog.show(manager, "MessageDialog");
        }
    }
}