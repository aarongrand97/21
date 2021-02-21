package com.example.a21;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    int targetValue, numbersPerTurn;
    boolean playerTurn = false;
    int currentValue = 0;
    TextView currentValueView, winLoseView;
    Button[] buttonArray; // array of the number input buttons
    Button submitButton, quitButton, playAgainButton;
    int selectedInput = -1;
    String firstTurnPlayer;
    ComputerPlayer computerPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // find UI features and set initial values
        currentValueView = findViewById(R.id.currentValueView);
        currentValueView.setText(Integer.toString(currentValue));

        targetValue = getIntent().getIntExtra("target_number", 21);

        firstTurnPlayer = getIntent().getStringExtra("first_turn_player");
        if (firstTurnPlayer.equals("Human")) {
            playerTurn = true;
        }
        numbersPerTurn = getIntent().getIntExtra("numbers_per_turn", 3);

        buttonArray = new Button[numbersPerTurn];
        assignButtons();

        submitButton = findViewById(R.id.submitButton);
        quitButton = findViewById(R.id.quitButton);
        playAgainButton = findViewById(R.id.playAgainButton);
        winLoseView = findViewById(R.id.WinLoseTextView);

        // Create the computer player
        int compPlayerDifficulty = getIntent().getIntExtra("comp_player_difficulty", 3);
        computerPlayer = new ComputerPlayer(compPlayerDifficulty, targetValue, numbersPerTurn);

        removeUnusedButtons(); // Get rid of input buttons over numbersPerTurn
        createSubmitClickEvent(); // On click listener for submit button
        startGame();
    }

    private void assignButtons() { // Gives each number input button its ID
        for (int ind = 0; ind < numbersPerTurn; ind++) {
            String buttonID = "inputButton" + ind;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttonArray[ind] = findViewById(resID);
            buttonArray[ind].setOnClickListener(this);
        }
    }

    private void removeUnusedButtons() { // Removes buttons beyond numbersPerTurn
        for (int ind = numbersPerTurn; ind < 5; ind++) {
            String buttonID = "inputButton" + ind;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            Button tempButton = findViewById(resID);
            tempButton.setVisibility(View.GONE);
        }
    }

    private void setInputNumbers() { // Add the number to each input button
        for (int ind = 0; ind < numbersPerTurn; ind++) {
            if (playerTurn) {
                int tempVal = currentValue + 1 + ind;
                if(tempVal<=21)
                    buttonArray[ind].setText(Integer.toString(tempVal));
                else {
                    buttonArray[ind].setText("-");
                    buttonArray[ind].setClickable(false);
                }
            } else {
                buttonArray[ind].setText("-");
            }
        }
    }

    private void enableInputNumbers() { // make input buttons clickable/unclickable
        for (int ind = 0; ind < numbersPerTurn; ind++) {
            buttonArray[ind].setClickable(playerTurn);
        }
    }

    private void setBackgroundColors(){ // Resets buttons to standard background colour
        for (int ind = 0; ind < numbersPerTurn; ind++) {
            buttonArray[ind].setBackgroundResource(android.R.drawable.btn_default);
        }
    }

    private void startGame() {
        enableInputNumbers();
        setInputNumbers();
        if(!playerTurn){
            makeComputerTurn();
        }
    }

    @Override // Number input buttons click listener
    public void onClick(View view) {
        if(!playerTurn){ // ignore if user pressing whilst waiting for go
            return;
        }

        Button clickedButton = (Button)view;

        String numSelected = clickedButton.getText().toString();
        if(numSelected.equals("-")){ // ignore if beyond targetNumber
            return;
        }

        setBackgroundColors(); // set all to standard background
        clickedButton.setBackgroundColor(getResources().getColor(R.color.selectedInput)); // highlight clicked button
        selectedInput = Integer.parseInt(numSelected); // store the selected input
    }

    private void createSubmitClickEvent(){ // submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedInput == -1){ // nothing been selected
                    return;
                }

                currentValue = selectedInput; // get the number previously selected

                if(checkGameOver()){
                    return;
                }

                currentValueView.setText(Integer.toString(currentValue)); // set screen value
                selectedInput = -1; // reset stored input

                // reset UI features
                playerTurn = false;
                enableInputNumbers();
                setInputNumbers();
                setBackgroundColors();

                // delay computer turn to mimick thinking
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        makeComputerTurn();
                    }
                }, 1500);
            }
        });
    }

    private void makeComputerTurn(){
        final int compChoice = computerPlayer.makeMove(this.currentValue); // get move from computer player
        currentValue += compChoice;
        if(checkGameOver()){
            return;
        }
        currentValueView.setText(Integer.toString(currentValue)); // set screen value

        // reset UI features
        playerTurn = true;
        setInputNumbers();
        enableInputNumbers();
    }

    private boolean checkGameOver(){
        if(currentValue >= targetValue){
            gameOver();
            return true;
        }
        else{
            return false;
        }
    }
    private void gameOver(){
        // Show target value has been reached
        currentValue = targetValue;
        currentValueView.setText(Integer.toString(currentValue));

        // Display win/lose
        winLoseView.setVisibility(View.VISIBLE);
        if(playerTurn){ // loss
            winLoseView.setText(R.string.loser);
        }
        else{
            winLoseView.setText(R.string.winner);
        }
        // show end of game options
        playAgainButton.setVisibility(View.VISIBLE);
        quitButton.setVisibility(View.VISIBLE);
    }

    public void playAgainClicked(View view){
        // remove end of game options
        playAgainButton.setVisibility(View.INVISIBLE);
        quitButton.setVisibility(View.INVISIBLE);
        winLoseView.setVisibility(View.INVISIBLE);
        // reset UI features
        setBackgroundColors();
        currentValue = 0;
        currentValueView.setText(Integer.toString(currentValue));
        // Set up whos turn it is, change who goes first from previous game
        if(firstTurnPlayer.equals("Human")){
            playerTurn = false;
            firstTurnPlayer = "Computer";
        }
        else{
            playerTurn = true;
            firstTurnPlayer = "Human";
        }
        computerPlayer.createListOfKeyNumbers(); // Have to reset the key numbers array list
        startGame();
    }

    public void quitClicked(View view){ // return to game settings activity
        finish();
    }
}
