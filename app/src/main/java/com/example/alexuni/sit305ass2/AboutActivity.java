package com.example.alexuni.sit305ass2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class AboutActivity extends AppCompatActivity {

    TextView attirbution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        attirbution = findViewById(R.id.attribution);

        attirbution.setText(R.string.credits);
    }
}
