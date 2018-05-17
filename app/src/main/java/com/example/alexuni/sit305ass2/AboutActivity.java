package com.example.alexuni.sit305ass2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
/*AboutActivity class is used for the about page, which is loaded after clicking on the about button on
MainActivity*/
public class AboutActivity extends AppCompatActivity {

    TextView attirbution;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        attirbution = findViewById(R.id.attribution);
        //Displays legal text from string resource
        attirbution.setText(R.string.credits);
    }
}
