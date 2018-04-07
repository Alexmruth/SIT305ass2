package com.example.alexuni.sit305ass2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button newGameBtn;
    Button continueBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        newGameBtn = findViewById(R.id.newGameBtn);
        continueBtn = findViewById(R.id.continueBtn);
    }

    public void onClick (View v) {
        if (v.getId() == R.id.newGameBtn) {
            Intent intent = new Intent (this, GameActivity.class);
            startActivity(intent);
        } else if (v.getId() == R.id.continueBtn){
            Intent intent = new Intent (this, testActivity.class);
            startActivity(intent);
        }
    }
}
