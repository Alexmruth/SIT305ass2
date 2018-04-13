package com.example.alexuni.sit305ass2;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {
    int intSwitch = 1;
    int i = 0;
    Button nextBtn;
    TextView textJSON;
    TextView nameJSON;

    private Timer timer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textJSON = findViewById(R.id.textJSON);
        nextBtn = findViewById(R.id.nextBtn);
        nameJSON = findViewById(R.id.nameJSON);


        //Opens the getText method
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void startActivity(View v) {
        if(v.getId() == R.id.button1) {
            Intent intent = new Intent(this, LevelsActivity.class);
            startActivity(intent);
        }
    }

    public String loadJSON() {
        String json = null;
        try {
            InputStream file = getAssets().open("dialogue.json");
            int size = file.available();
            byte[] buffer = new byte[size];
            file.read(buffer);
            file.close();
            json = new String(buffer, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public void nextDialogue(View view) {
        intSwitch++;
            try {
                getText();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    // This method is a working switch statement and JSON reader
    public void getText() throws JSONException {


        JSONObject obj = new JSONObject(loadJSON());
        JSONArray chars = obj.getJSONArray("Characters");
        JSONObject c = chars.getJSONObject(i); //THIS number changes which object in the array that is called
        String dialogue;
        String charName;

        switch (intSwitch) {
            case 0: //Intro text 1
                nameJSON.setText("");
                textJSON.setText("");

                break;
            case 1: //Intro text 1
                charName = c.getString("character1");
                dialogue = c.getString("introText1");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 2: //Intro text 2
                charName = c.getString("character2");
                dialogue = c.getString("introText2");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 3: //Intro text 3
                charName = c.getString("character1");
                dialogue = c.getString("introText3");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 4: //Intro text 4
                charName = c.getString("character2");
                dialogue = c.getString("introText4");
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;


        }

    }

}



