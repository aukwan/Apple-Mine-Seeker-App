package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.apple_mine_seeker.R;
import ca.sfu.cmpt276.apple_mine_seeker.model.Game;

public class OptionsActivity extends AppCompatActivity {

    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options);

        game = Game.getInstance();
        createRadioButtons();
    }

    private void createRadioButtons() {
        RadioGroup boardSizeGroup = findViewById(R.id.boardSize);
        RadioGroup numMinesGroup = findViewById(R.id.numMines);

        // Space out radio buttons for better look
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT,
                1.0f);

        int[] num_mines = getResources().getIntArray(R.array.num_mines);
        String[] board_size = getResources().getStringArray(R.array.board_size);

        for (int i = 0; i < num_mines.length; i++) {
            final int numMines = num_mines[i];

            RadioButton button = new RadioButton(this);
            button.setText(numMines + getString(R.string.mines));
            button.setTextSize(18);
            button.setTextColor(Color.WHITE);
            button.setLayoutParams(param);
            // Code found at https://stackoverflow.com/questions/17120199/change-circle-color-of-radio-button
            ColorStateList colorStateList = new ColorStateList(
                    new int[][] {
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_enabled}
                    }, new int[] {
                            Color.WHITE,
                            Color.WHITE
                    }
            );
            button.setButtonTintList(colorStateList);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, numMines + getString(R.string.mines_selected), Toast.LENGTH_SHORT).show();
                    game.setNumMines(numMines);
                }
            });

            numMinesGroup.addView(button);
        }

        for (int i = 0; i < board_size.length; i++) {
            final String boardSize = board_size[i];

            RadioButton button = new RadioButton(this);
            button.setText(boardSize);
            button.setTextSize(18);
            button.setTextColor(Color.WHITE);
            button.setLayoutParams(param);
            ColorStateList colorStateList = new ColorStateList(
                    new int[][] {
                            new int[]{-android.R.attr.state_enabled},
                            new int[]{android.R.attr.state_enabled}
                    }, new int[] {
                    Color.WHITE,
                    Color.WHITE
            }
            );
            button.setButtonTintList(colorStateList);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(OptionsActivity.this, boardSize + getString(R.string.selected), Toast.LENGTH_SHORT).show();
                    if (boardSize.equals("4 rows by 6 columns")) {
                        game.setNumRows(4);
                        game.setNumCols(6);
                    } else if (boardSize.equals("5 rows by 10 columns")) {
                        game.setNumRows(5);
                        game.setNumCols(10);
                    } else {
                        game.setNumRows(6);
                        game.setNumCols(15);
                    }

                }
            });

            boardSizeGroup.addView(button);
        }
    }

    public static Intent makeIntent(Context context) {
        return new Intent(context, OptionsActivity.class);
    }
}