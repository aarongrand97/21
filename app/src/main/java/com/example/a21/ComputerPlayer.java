package com.example.a21;

import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer {
    int difficulty;
    int targetValue;
    int numbersPerTurn;
    ArrayList<Integer> keyNumbers = new ArrayList<>();

    public ComputerPlayer(int diff, int targVal, int numsPrTurn){
        this.difficulty = diff;
        this.targetValue = targVal;
        this.numbersPerTurn = numsPrTurn;
        createListOfKeyNumbers();
    }

    public void createListOfKeyNumbers(){ // Store numbers to aim for to play perfect game
        keyNumbers.clear();
        int winningNumber = targetValue - 1;
        while(winningNumber > 0){
            keyNumbers.add(winningNumber);
            winningNumber -= (numbersPerTurn + 1);
        }
    }

    public int makeMove(int currentNumber){
        if(currentNumber == targetValue - 1){ // only one possible move
            return 1;
        }

        int ret;
        // remove any key numbers no longer needed
        while(!keyNumbers.isEmpty() && keyNumbers.get(keyNumbers.size() - 1) <= currentNumber){
            keyNumbers.remove(keyNumbers.size() - 1);
        }

        int correctMove = keyNumbers.get(keyNumbers.size() - 1) - currentNumber; // get the ideal move

        if (difficulty == 0 || correctMove > numbersPerTurn){ // make a random move
            ret = new Random().nextInt(numbersPerTurn) + 1;
        }
        else {
            int rndm = new Random().nextInt(5); // random number between 0,4
            if(rndm <= difficulty){ // return correct move
                ret = correctMove;
            }
            else{ // return a random move
                ret = new Random().nextInt(numbersPerTurn) + 1;
            }
        }

        return ret;
    }


}
