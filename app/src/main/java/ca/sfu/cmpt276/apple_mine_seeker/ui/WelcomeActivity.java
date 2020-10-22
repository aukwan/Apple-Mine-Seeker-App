package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.apple_mine_seeker.R;

/*
 Welcome Activity screen, comes up when the app is launched.
 */

public class WelcomeActivity extends AppCompatActivity {

    Button skipBtn;
    ImageView mineApple;
    ImageView apple1;
    ImageView apple2;
    Animation rotate;
    Animation fadeIn;
    private static long POST_ANIM_DELAY = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupSkipButton();
        startAnimation();
    }

    private void setupSkipButton() {
        skipBtn = findViewById(R.id.skipBtn);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startAnimation() {
        mineApple = findViewById(R.id.mineApple);
        apple1 = findViewById(R.id.apple1);
        apple2 = findViewById(R.id.apple2);

        rotate = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        fadeIn = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade_in);

        apple1.setAnimation(fadeIn);
        apple2.setAnimation(fadeIn);
        mineApple.setAnimation(rotate);

        rotate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // Handler code from Stack Overflow: https://stackoverflow.com/a/29148136
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, POST_ANIM_DELAY);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finishAffinity();
    }
}