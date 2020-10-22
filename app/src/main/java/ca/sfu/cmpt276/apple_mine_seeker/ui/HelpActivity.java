package ca.sfu.cmpt276.apple_mine_seeker.ui;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.apple_mine_seeker.R;

/*
Help Activity screen which shows about the author, how to play, and citations to resources used.
 */

public class HelpActivity extends AppCompatActivity {

    TextView helpText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        helpText = findViewById(R.id.helpText);
        helpText.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}