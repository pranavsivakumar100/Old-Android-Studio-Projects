package com.example.dodgegameattempt5;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class EndActivity extends AppCompatActivity {

    private TextView gameOver, score, reason;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);

        gameOver = findViewById(R.id.gameOver);
        score = findViewById(R.id.score);
        reason = findViewById(R.id.reason);

        Intent intent = getIntent();
        final int scoreValue = intent.getExtras().getInt("score");
        final int reasonValue = intent.getExtras().getInt("message");

        score.setText("Score: " + scoreValue);
        reason.setText(reasonValue);
    }
}