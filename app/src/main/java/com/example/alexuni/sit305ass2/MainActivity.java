package com.example.alexuni.sit305ass2;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button newGameBtn, continueBtn;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameBtn = findViewById(R.id.newGameBtn);
        continueBtn = findViewById(R.id.continueBtn);

        //Declaring Shared prefs variables
        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Method onClick() is called when either newGameBtn or continueBtn is clicked.
    // Method signature: Public void as it is publicly accessible and returns no value. (View v) grabs the view clicked
    public void onClick(View v) {
        //If newGameBtn is clicked, this code executes
        if (v.getId() == R.id.newGameBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Game?"); //alert title
            builder.setMessage("If you start a new game, all current progress is lost!"); //alert message
            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startNewGame(); //positive reaction button response, calls startNewGame() method
                }
            });
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // negative reaction button exits back to home screen
                }
            });
            builder.show();
        }
        //If continueBtn is clicked, this code executes and loads GameActivity
        if (v.getId() == R.id.continueBtn) {
            editor.putInt("continueGame", 1); // changes continueGame variable to 1, which skips intro scene
            Intent intent = new Intent(this, GameActivity.class);
            startActivity(intent);
        }
        editor.commit(); // commits changes to shared prefs file
        if (v.getId() == R.id.aboutBtn) {
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
    }

    // Method startNewGame() responsible for triggering a new game state and loading the GameActivity.
    public void startNewGame() {
        editor.putInt("continueGame", 0); // changes continueGame variable to 0, which triggers intro scene upon entering GameActivity
        editor.commit(); // commits changes
        Intent intent = new Intent(this, GameActivity.class);
        startActivity(intent);

    }
}
