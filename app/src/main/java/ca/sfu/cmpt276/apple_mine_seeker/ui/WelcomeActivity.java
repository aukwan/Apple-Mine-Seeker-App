package ca.sfu.cmpt276.apple_mine_seeker.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ca.sfu.cmpt276.apple_mine_seeker.R;

public class WelcomeActivity extends AppCompatActivity {

    Button skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setupSkipButton();
    }

    private void setupSkipButton() {
        skip = findViewById(R.id.skipBtn);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}