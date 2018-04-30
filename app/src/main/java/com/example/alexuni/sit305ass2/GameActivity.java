package com.example.alexuni.sit305ass2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Timer;

public class GameActivity extends AppCompatActivity {

    boolean introText;
    int henrySwitch = 0;
    int jc = 0; //short for JSON counter
    int LLcount = 0;
    int continueGame;
    static int goldCount;
    LinearLayout LL1;
    LinearLayout LL3;
    Button nextBtn;
    TextView textJSON;
    TextView nameJSON;
    TextView goldText;
    ImageView textImage;
    String dialogue;
    String charName;
    JSONObject o;
    JSONArray ja;
    int i = 0;

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
        goldText = findViewById(R.id.goldText);
        LL1 = findViewById(R.id.LL1);
        LL3 = findViewById(R.id.LL3);
        continueGame = MainActivity.continueGame;

        if (continueGame == 1) {
            LLcount = 1;
            introText = false;
        } else {
            LL1.setVisibility(View.INVISIBLE);
            LL3.setVisibility(View.INVISIBLE);
            introText = true;
        }

        goldText.setText(String.valueOf(goldCount));

        //Opens the getText method
        try {
            getText();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void onMenuBtnClick(View v) {
        if(v.getId() == R.id.button1) {
            Intent intent = new Intent(this, LevelsActivity.class);
            startActivity(intent);
        } else if(v.getId() == R.id.shopBtn) {
            Intent intent = new Intent(this, ShopActivity.class);
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
    /* loadJSON() is responsible for grabbing all content from the specified JSON file and converting
        it into a string to be used as a JSONObject within the getText() method. */
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

    public void nextDialogue(View view) throws JSONException {
        if (i <= 8 && introText) {
            i++;
            getText();
        } else {
            introText = false;
            LLcount = 1;
            nameJSON.setText("");
            textJSON.setText("");
            textImage.setBackground(null);
        }

        if(LLcount == 1){
            LL1.setVisibility(View.VISIBLE);
            LL3.setVisibility(View.VISIBLE);
        }

    }


    // This method is a working switch statement and JSON reader
    public void getText() throws JSONException {

        JSONObject obj = new JSONObject(loadJSON()); //defines JSONObject as the result of loadJSON method
        ja = obj.getJSONArray("IntroText");


        if(jc == 0) {
            o = ja.getJSONObject(i);
        } else if(jc ==1) {
            o = obj.getJSONObject("HenryText");
        }

        charName = o.getString("character");
        dialogue = o.getString("text");

        if (introText) {
            nameJSON.setText(charName);
            textJSON.setText(dialogue);

            if(o.getString("character").equals("Henry the Villager")) {
                textImage.setBackgroundResource(R.drawable.c_henryvillager);
            } else if (o.getString("character").equals("Archer Bones (you)")) {
                textImage.setBackgroundResource(R.drawable.c_archerbones);
            }
        }

    }

}



