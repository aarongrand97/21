package com.example.a21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

public class SetGameSettingsActivity extends AppCompatActivity {
    int numbersPerTurn = 3; // default the number of turns
    // inputs
    TextView numbersPerTurnTextView;
    TextView targetNumberTextView;
    Spinner firstTurnSpinner;
    SeekBar compPlayerDifficultySlider;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_game_settings);
        // find the input widgets
        numbersPerTurnTextView = findViewById(R.id.numbersPerTurnTextView);
        numbersPerTurnTextView.setText(Integer.toString(numbersPerTurn));

        targetNumberTextView = findViewById(R.id.TargetNumberInput);
        targetNumberTextView.setText("21"); // default the target number to 21

        firstTurnSpinner = findViewById(R.id.FirstTurnSpinner);
        compPlayerDifficultySlider = findViewById(R.id.compPlayerDifficultySlider);
    }
    public void incrementNumbersPerGo(View view){
        if(numbersPerTurn < 5) {
            numbersPerTurn++;
        }
        numbersPerTurnTextView.setText(Integer.toString(numbersPerTurn));
    }
    public void decrementNumbersPerGo(View view){
        if(numbersPerTurn > 3) {
            numbersPerTurn--;
        }
        numbersPerTurnTextView.setText(Integer.toString(numbersPerTurn));
    }
    public void startGameClicked(View view){
        Intent intent = new Intent(this, GameActivity.class);
        Bundle sendToGame = new Bundle();
        // target number value
        String targetNumberStr = targetNumberTextView.getText().toString();
        int targetNumber = Integer.parseInt(targetNumberStr);
        sendToGame.putInt("target_number", targetNumber);
        // numbers per turn value
        String numbersPerTurnStr = numbersPerTurnTextView.getText().toString();
        int numbersPerTurn = Integer.parseInt(numbersPerTurnStr);
        sendToGame.putInt("numbers_per_turn", numbersPerTurn);
        // first turn player
        String firstTurnPlayer = firstTurnSpinner.getSelectedItem().toString();
        sendToGame.putString("first_turn_player", firstTurnPlayer);
        // computer player difficulty
        int compPlayerDifficulty = compPlayerDifficultySlider.getProgress();
        sendToGame.putInt("comp_player_difficulty", compPlayerDifficulty);

        // send info and start activity
        intent.putExtras(sendToGame);
        startActivity(intent);
    }
}
