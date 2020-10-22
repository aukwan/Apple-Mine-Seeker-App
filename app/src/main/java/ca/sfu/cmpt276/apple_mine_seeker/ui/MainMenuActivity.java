package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.apple_mine_seeker.R;
import ca.sfu.cmpt276.apple_mine_seeker.model.Game;

/*
  Main Menu Activity screen, allow for user to start game, access options, or go to help screen
 */

public class MainMenuActivity extends AppCompatActivity {

    Button startGameBtn;
    Button optionsBtn;
    Button helpBtn;
    ImageView mineApple;
    Animation scale;
    private Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        scale = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale);
        mineApple = findViewById(R.id.mineApple2);
        mineApple.setAnimation(scale);
        game = Game.getInstance();

        setupStartGameBtn();
        setupOptionsBtn();
        setupHelpBtn();
    }

    private void setupStartGameBtn() {
        startGameBtn = findViewById(R.id.startGameBtn);
        startGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GameActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupOptionsBtn() {
        optionsBtn = findViewById(R.id.optionsBtn);
        optionsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = OptionsActivity.makeIntent(MainMenuActivity.this);
                startActivity(intent);
            }
        });
    }

    private void setupHelpBtn() {
        helpBtn = findViewById(R.id.helpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}