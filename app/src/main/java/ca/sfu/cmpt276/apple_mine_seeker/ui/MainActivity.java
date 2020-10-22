package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.apple_mine_seeker.R;

/*
 Main Activity used to launch app
 */

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}