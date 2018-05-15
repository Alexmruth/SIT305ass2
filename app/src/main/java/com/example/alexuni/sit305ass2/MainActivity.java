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
    Button newGameBtn;
    Button continueBtn;
    static int continueGame;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameBtn = findViewById(R.id.newGameBtn);
        continueBtn = findViewById(R.id.continueBtn);

        prefs = getSharedPreferences("playerSaveData", MODE_PRIVATE);
        editor = prefs.edit();
    }

    public void onClick (View v) {
        if (v.getId() == R.id.newGameBtn) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("New Game?");
            builder.setMessage("If you start a new game, all current progress is lost!");
            builder.setPositiveButton("New Game", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    startNewGame();
                }
            });
            builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            builder.show();
        } else if (v.getId() == R.id.continueBtn){
            editor.putInt("continueGame", 1);
            Intent intent = new Intent (this, GameActivity.class);
            startActivity(intent);
        }
        editor.commit();
    }
    public void startNewGame() {
        editor.putInt("continueGame", 0);
        editor.commit();
        Intent intent = new Intent (this, GameActivity.class);
        startActivity(intent);

    }
}
