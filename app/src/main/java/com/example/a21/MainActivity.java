package com.example.a21;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Create view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void newGameClicked(View view){
        // Go to new game settings
        Intent intent = new Intent(this, SetGameSettingsActivity.class);
        startActivity(intent);
    }
}
