package com.example.alexuni.sit305ass2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {
    int introSwitch = 2;
    int henrySwitch = 0;
    int jc = 0;
    Button nextBtn;
    TextView textJSON;
    TextView nameJSON;
    ImageView textImage;
    String dialogue;
    String charName;
    JSONObject c;

    private Timer timer = new Timer();

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        textJSON = findViewById(R.id.textJSON);
        nextBtn = findViewById(R.id.nextBtn);
        nameJSON = findViewById(R.id.nameJSON);
        textImage = findViewById(R.id.textImage);


        //Opens the getText method
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void lvlsActivity(View v) {
        if(v.getId() == R.id.button1) {
            Intent intent = new Intent(this, LevelsActivity.class);
            startActivity(intent);
        }
    }

    public void talkHenry(View v) {
        jc = 1;
        henrySwitch = 1;

        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
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
        if (introSwitch > 1 && introSwitch <= 10) {
            introSwitch++;
        } else {
            introSwitch = 1;
        }
            try {
                getText();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    // This method is a working switch statement and JSON reader
    public void getText() throws JSONException {

        JSONObject obj = new JSONObject(loadJSON());
        if(jc == 0) {
            c = obj.getJSONObject("IntroText");
        } else if(jc ==1) {
            c = obj.getJSONObject("HenryText");
        }

        switch (introSwitch) {
            case 1: //Intro text 1
                nameJSON.setText("");
                textJSON.setText("");
                textImage.setBackground(null);
                break;
            case 2: //Intro text 1 -- Henry villager
                charName = c.getString("character2");
                dialogue = c.getString("introText1");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 3: //Intro text 2 -- Archer Bones
                charName = c.getString("character1");
                dialogue = c.getString("introText2");
                textImage.setBackgroundResource(R.drawable.c_archerbones);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 4: //Intro text 3 -- Henry villager
                charName = c.getString("character2");
                dialogue = c.getString("introText3");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 5: //Intro text 4 -- Archer Bones
                charName = c.getString("character1");
                dialogue = c.getString("introText4");
                textImage.setBackgroundResource(R.drawable.c_archerbones);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 6: //Intro text 5 -- Henry Villager
                charName = c.getString("character2");
                dialogue = c.getString("introText5");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 7: //Intro text 5.1 -- Henry Villager
                charName = c.getString("character2");
                dialogue = c.getString("introText5.1");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 8: //Intro text 6 -- Archer Bones
                charName = c.getString("character1");
                dialogue = c.getString("introText6");
                textImage.setBackgroundResource(R.drawable.c_archerbones);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 9: //Intro text 7 -- Henry Villager
                charName = c.getString("character2");
                dialogue = c.getString("introText7");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 10: //Intro text 7.1 -- Henry Villager
                charName = c.getString("character2");
                dialogue = c.getString("introText7.1");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
            case 11: //Intro text 7.2 -- Henry Villager
                charName = c.getString("character2");
                dialogue = c.getString("introText7.2");
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
                nameJSON.setText(charName);
                textJSON.setText(dialogue);
                break;
        }
            switch (henrySwitch) {
                case 0:

                    break;
                case 1:
                    charName = c.getString("character2");
                    dialogue = c.getString("text1");
                    textImage.setBackgroundResource(R.drawable.c_henryvillager);
                    nameJSON.setText(charName);
                    textJSON.setText(dialogue);
                    break;

            }
    }

}



